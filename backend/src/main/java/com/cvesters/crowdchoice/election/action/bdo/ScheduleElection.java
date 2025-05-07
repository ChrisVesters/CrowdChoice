package com.cvesters.crowdchoice.election.action.bdo;

import java.time.OffsetDateTime;

import org.apache.commons.lang3.Validate;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;

public class ScheduleElection extends ElectionAction {

	private final OffsetDateTime startOn;
	private final OffsetDateTime endOn;

	public ScheduleElection(final long electionId, final OffsetDateTime startOn,
			final OffsetDateTime endOn) {
		super(electionId);

		Validate.isTrue(
				endOn == null || (startOn != null && endOn.isAfter(startOn)));

		this.startOn = startOn;
		this.endOn = endOn;
	}

	@Override
	protected void doApply(final ElectionInfo info) {
		info.schedule(startOn, endOn);
	}

}
