package com.cvesters.crowdchoice.election.action.bdo;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;

public class EndElection extends ElectionAction {

	public EndElection(final long electionId) {
		super(electionId);
	}

	@Override
	protected void doApply(final ElectionInfo info) {	
		info.end();
	}
	
}
