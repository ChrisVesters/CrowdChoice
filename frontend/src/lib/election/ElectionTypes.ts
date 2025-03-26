export type Election = {
	id: number;
	topic: string;
	description: string;
	startedOn: string | null;
	endedOn: string | null;
};

export type CreateElectionRequest = {
	topic: string;
	description: string;
	startedOn: string | null;
	endedOn: string | null;
};