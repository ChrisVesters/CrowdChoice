import type { Election } from "./ElectionTypes";

export class ElectionClient {
	public static async getAll(): Promise<Array<Election>> {
		return fetch(getEndpoint()).then(res => res.json());
	}

	public static async get(id: number): Promise<Election> {
		return fetch(`${getEndpoint()}/${id}`).then(res => res.json());
	}

	public static async create(topic: string): Promise<Election> {
		return fetch(getEndpoint(), {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({ topic })
		}).then(res => res.json());
	}

	public static async delete(id: number): Promise<void> {
		return fetch(`${getEndpoint()}/${id}`, {
			method: "DELETE"
		}).then();
	}
}

function getEndpoint(): string {
	return `${import.meta.env.VITE_API_URL}/elections`;
}
