export type Vote = {
	id: number;
	castedOn: string;
	candidateId: number;
};

export type CreateVoteRequest = {
	candidateId: number;
}

export type VoteCount = {
	candidateId: number;
	voteCount: number;
}