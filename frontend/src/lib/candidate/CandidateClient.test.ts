import { test, expect, vi } from "vitest";

import { CandidateClient } from "./CandidateClient";
import type { Candidate } from "./CandidateTypes";

afterEach(() => {
	vi.clearAllMocks();
});

const BASE_URL = "http://localhost:7000/api";

const candidates = [
	{ id: 1, name: "Micronaut", description: "Getting rid of reflection" },
	{ id: 2, name: "Lombok", description: "No more boilerplate" }
];

test.each([
	["Empty", []],
	["Single", [candidates[0]]],
	["Multiple", [candidates[0], candidates[1]]]
])("Get All - %s", async (_, candidates: Array<Candidate>) => {
	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(candidates)
		})
	);

	const response: Array<Candidate> = await CandidateClient.getAll(1);

	expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/elections/1/candidates`);

	expect(response).toHaveLength(candidates.length);
	expect(response).toEqual(candidates);
});

test("Create", async () => {
	const electionId = 1;
	const candidate: Candidate = candidates[0];

	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(candidate)
		})
	);

	const response = await CandidateClient.create(electionId, {
		name: candidate.name,
		description: candidate.description
	});

	expect(fetch).toHaveBeenCalledWith(
		`${BASE_URL}/elections/${electionId}/candidates`,
		{
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				name: candidate.name,
				description: candidate.description
			})
		}
	);

	expect(response).toEqual(candidate);
});

test("Delete", async () => {
	const electionId = 1;
	const candidateId = 2;

	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(undefined)
		})
	);

	await CandidateClient.delete(electionId, candidateId);

	expect(fetch).toHaveBeenCalledWith(
		`${BASE_URL}/elections/${electionId}/candidates/${candidateId}`,
		{
			method: "DELETE"
		}
	);
});
