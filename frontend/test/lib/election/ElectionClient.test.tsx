import { test, expect, vi } from 'vitest';

import { ElectionClient } from "$lib/election/ElectionClient";
import { Election } from "$lib/election/ElectionTypes";

afterEach(() => {
	vi.clearAllMocks();
});

const BASE_URL = import.meta.env.VITE_API_URL;

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

	expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/elections`);

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

	expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/elections/1`);

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
		`${BASE_URL}/elections`,
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

test("Delete", async () => {

	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(undefined)
		})
	);

	await ElectionClient.delete(1);

	expect(fetch).toHaveBeenCalledWith(
		`${BASE_URL}/elections/1`,
		{
			method: "DELETE"
		}
	);
});
