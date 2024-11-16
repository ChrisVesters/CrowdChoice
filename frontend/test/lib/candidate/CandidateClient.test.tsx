import { test, expect, vi } from 'vitest';

import { CandidateClient } from "$lib/candidate/CandidateClient";
import { Candidate } from "$lib/candidate/CandidateTypes";

afterEach(() => {
	vi.clearAllMocks();
});

test.each([
	["Empty", []],
	["Single", [{ id: 1, name: "Micronaut" }]],
	["Multiple", [{ id: 1, topic: "Micronaut" }, { id: 2, topic: "Lombok" }]]
])("Get All - %s", async (_, candidates: Array<Candidate>) => {
	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(candidates)
		})
	);

	const response: Array<Candidate> = await CandidateClient.getAll(1);

	expect(fetch).toHaveBeenCalledWith(
		"http://localhost:7000/api/elections/1/candidates"
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
		`http://localhost:7000/api/elections/${electionId}/candidates`,
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
