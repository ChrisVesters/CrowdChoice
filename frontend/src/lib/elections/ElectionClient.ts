export type Election = {
	id: number;
	topic: string;
};

const BASE_URL = `${import.meta.env.VITE_API_URL}/elections`;

export class ElectionClient {
	public static async getAll(): Promise<Array<Election>> {
		return fetch(BASE_URL).then(res => res.json());
	}

	public static async create(topic: string): Promise<Election> {
		return fetch(BASE_URL, {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({ topic })
		}).then(res => res.json());
	}
}
