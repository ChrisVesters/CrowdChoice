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

export type UpdateElectionRequest = {
	topic: string;
	description: string;
	startedOn: string | null;
	endedOn: string | null;
};


export abstract class ElectionActionRequest {
	private readonly action: string;

	constructor(action: string) {
		this.action = action;
	}
}

export class StartElectionRequest extends ElectionActionRequest {

	constructor() {
		super("start");
	}
}

export class EndElectionequest extends ElectionActionRequest {

	constructor() {
		super("end");
	}
}

export class ScheduleElectionRequest extends ElectionActionRequest {

	private readonly startOn: string | null;
	private readonly endOn: string | null;

	constructor(startOn: string | null, endOn: string | null) {
		super("schedule");

		this.startOn = startOn;
		this.endOn = endOn;
	}
}