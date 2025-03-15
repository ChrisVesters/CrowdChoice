import type { CreateVoteRequest, Vote, VoteCount } from "./VoteTypes";

export class VoteClient {
	public static async getCounts(
		electionId: number
	): Promise<Array<VoteCount>> {
		return fetch(`${getEndpoint(electionId)}`).then(res => res.json());
	}

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
