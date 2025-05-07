package com.cvesters.crowdchoice.election.action.bdo;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;

public class StartElection extends ElectionAction {
	
	public StartElection(final long electionId) {
		super(electionId);
	}

	@Override
	protected void doApply(final ElectionInfo info) {
		info.start();
	}
	
}
