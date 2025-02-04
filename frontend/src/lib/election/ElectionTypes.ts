export type Election = {
	id: number;
	topic: string;
	description: string;
};

export type CreateElectionRequest = {
	topic: string;
	description: string;
};