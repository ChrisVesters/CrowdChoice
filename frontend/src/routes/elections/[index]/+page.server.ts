import type { Candidate } from "$lib/candidate/CandidateTypes.js";
import { CandidateClient } from "$lib/candidate/CandidateClient.js";
import { ElectionClient } from "$lib/election/ElectionClient";
import type { Election } from "$lib/election/ElectionTypes.js";

export type ElectionPageProps = {
	info: Election,
	candidates: Array<Candidate>
};

export async function load({ params }): Promise<ElectionPageProps> {
	const id = Number(params.index);
	if (!id) {
		throw new Error("Invalid ID");
	}

	return {
		info: await ElectionClient.get(id),
		candidates: await CandidateClient.getAll(id)
	}
}
