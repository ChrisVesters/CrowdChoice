import { test, expect, vi } from "vitest";

import { ElectionClient } from "./ElectionClient";
import type { Election } from "./ElectionTypes";

afterEach(() => {
	vi.clearAllMocks();
});

const BASE_URL = import.meta.env.VITE_API_URL;

test.each([
	["Empty", []],
	["Single", [{ id: 1, topic: "Topics", description: "" }]],
	[
		"Multiple",
		[
			{ id: 1, topic: "Topics", description: "" },
			{
				id: 2,
				topic: "Federal Elections 2024",
				description: "Vote for the lesser evil"
			}
		]
	]
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
	const election: Election = { id: 1, topic: "Topics", description: "" };

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
	const election: Election = { id: 1, topic: "Topics", description: "" };

	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(election)
		})
	);

	const response = await ElectionClient.create({
		topic: election.topic,
		description: election.description
	});

	expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/elections`, {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify({ topic: election.topic })
	});

	expect(response).toEqual(election);
});

test("Delete", async () => {
	global.fetch = vi.fn(() =>
		Promise.resolve({
			json: () => Promise.resolve(undefined)
		})
	);

	await ElectionClient.delete(1);

	expect(fetch).toHaveBeenCalledWith(`${BASE_URL}/elections/1`, {
		method: "DELETE"
	});
});
