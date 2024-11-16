import { test, expect, vi } from 'vitest';

import { ElectionClient } from "$lib/election/ElectionClient";
import { Election } from "$lib/election/ElectionTypes";

afterEach(() => {
	vi.clearAllMocks();
});

test.each([
	["Empty", []],
	["Single", [{ id: 1, topic: "Topics" }]],
	["Multiple", [{ id: 1, topic: "Topics" }, { id: 2, topic: "Federal Elections 2024" }]]
])("Get All - %s", async (_, elections: Array<Election>) => {
	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(elections)
		})
	);

	const response: Array<Election> = await ElectionClient.getAll();

	expect(fetch).toHaveBeenCalledWith(
		"http://localhost:7000/api/elections"
	);

	expect(response).toHaveLength(elections.length);
	expect(response).toEqual(elections);
});

test("Get", async () => {
	const election: Election = { id: 1, topic: "Topics" };

	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(election)
		})
	);

	const response: Election = await ElectionClient.get(1);

	expect(fetch).toHaveBeenCalledWith(
		"http://localhost:7000/api/elections/1"
	);

	expect(response).toEqual(election);
});

test("Create", async () => {
	const election: Election = { id: 1, topic: "Topics" };

	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(election)
		})
	);

	const response = await ElectionClient.create(election.topic);

	expect(fetch).toHaveBeenCalledWith(
		"http://localhost:7000/api/elections",
		{
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({ topic: election.topic })
		}
	);

	expect(response).toEqual(election);
});
