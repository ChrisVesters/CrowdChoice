import type { Candidate } from "./CandidateTypes";

export class CandidateClient {
	// TODO: or create instance with the correct electionId?

	public static async getAll(electionId: number): Promise<Array<Candidate>> {
		return fetch(getEndpoint(electionId)).then(res => res.json());
	}

	public static async create(electionId: number, name: string): Promise<Candidate> {
		return fetch(getEndpoint(electionId), {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({ name })
		}).then(res => res.json());
	}

	public static async delete(electionId: number, candidateId: number): Promise<void> {
		return fetch(`${getEndpoint(electionId)}/${candidateId}`, {
			method: "DELETE",
			headers: {
				"Content-Type": "application/json"
			}
		}).then();
	}
}

function getEndpoint(electionId: number): string {
	return `${import.meta.env.VITE_API_URL}/elections/${electionId}/candidates`;
}
