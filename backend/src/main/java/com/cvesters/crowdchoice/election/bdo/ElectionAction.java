package com.cvesters.crowdchoice.election.bdo;

import lombok.Getter;

public class ElectionAction {

	@Getter
	private final long electionId;

	public ElectionAction(final long electionId) {
		this.electionId = electionId;
	}

	public void apply(final ElectionInfo info) {

	}
}
