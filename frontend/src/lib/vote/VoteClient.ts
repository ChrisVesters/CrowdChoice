import type { CreateVoteRequest, Vote } from "./VoteTypes";

export class VoteClient {
	public static async create(
		electionId: number,
		request: CreateVoteRequest
	): Promise<Vote> {
		return fetch(getEndpoint(electionId), {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(request)
		}).then(res => res.json());
	}
}

function getEndpoint(electionId: number): string {
	return `${import.meta.env.VITE_API_URL}/elections/${electionId}/votes`;
}
