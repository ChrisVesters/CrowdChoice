import { CandidateClient } from "$lib/candidate/CandidateClient";
import type { Candidate } from "$lib/candidate/CandidateTypes";
import { ElectionClient } from "$lib/election/ElectionClient";
import type { Election } from "$lib/election/ElectionTypes";
import { error } from "@sveltejs/kit";

export type VotePageProps = {
	election: Election;
	candidates: Array<Candidate>;
};

export async function load({ url }): Promise<VotePageProps> {
	const id = Number(url.searchParams.get("electionId"));
	if (!id || isNaN(id)) {
		throw error(400, "Invalid ID");
	}

	// TODO: translations?
	const election = await ElectionClient.get(id).catch(() => {
		throw error(404, "Election not found");
	});
	const candidates = await CandidateClient.getAll(id).catch(() => {
		throw error(404, "Candidates not found");
	});

	return {
		election,
		candidates
	};
}
