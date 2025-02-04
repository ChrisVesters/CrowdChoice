export type Candidate = {
	id: number;
	name: string;
	description: string;
};

export type CreateCandidateRequest = {
	name: string;
	description: string;
}