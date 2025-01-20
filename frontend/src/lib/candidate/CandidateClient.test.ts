import { test, expect, vi } from 'vitest';

import { CandidateClient } from "./CandidateClient";
import type { Candidate } from "./CandidateTypes";

afterEach(() => {
	vi.clearAllMocks();
});

const BASE_URL = "http://localhost:7000/api";

test.each([
	["Empty", []],
	["Single", [{ id: 1, name: "Micronaut" }]],
	["Multiple", [{ id: 1, name: "Micronaut" }, { id: 2, name: "Lombok" }]]
])("Get All - %s", async (_, candidates: Array<Candidate>) => {
	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(candidates)
		})
	);

	const response: Array<Candidate> = await CandidateClient.getAll(1);

	expect(fetch).toHaveBeenCalledWith(
		`${BASE_URL}/elections/1/candidates`
	);

	expect(response).toHaveLength(candidates.length);
	expect(response).toEqual(candidates);
});


test("Create", async () => {
	const electionId = 1;
	const candidate: Candidate = { id: 1, name: "Micronaut" };

	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(candidate)
		})
	);

	const response = await CandidateClient.create(electionId, candidate.name);

	expect(fetch).toHaveBeenCalledWith(
		`${BASE_URL}/elections/${electionId}/candidates`,
		{
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({ name: candidate.name })
		}
	);

	expect(response).toEqual(candidate);
});


test("Delete", async() => {
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
