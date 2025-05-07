package com.cvesters.crowdchoice.election.action;

import java.time.OffsetDateTime;
import java.util.Objects;

import com.cvesters.crowdchoice.election.action.bdo.ElectionAction;
import com.cvesters.crowdchoice.election.action.bdo.EndElection;
import com.cvesters.crowdchoice.election.action.bdo.ScheduleElection;
import com.cvesters.crowdchoice.election.action.bdo.StartElection;
import com.cvesters.crowdchoice.election.action.dto.ElectionActionDto;
import com.cvesters.crowdchoice.election.action.dto.EndElectionDto;
import com.cvesters.crowdchoice.election.action.dto.ScheduleElectionDto;
import com.cvesters.crowdchoice.election.action.dto.StartElectionDto;

public final class ElectionActionMapper {

	private ElectionActionMapper() {
	}

	public static ElectionAction fromDto(final long electionId,
			final ElectionActionDto dto) {
		Objects.requireNonNull(dto);

		return switch (dto) {
			case StartElectionDto _ -> new StartElection(electionId);
			case ScheduleElectionDto scheduleDto -> {
				final OffsetDateTime startOn = scheduleDto.startOn();
				final OffsetDateTime endOn = scheduleDto.endOn();
				yield new ScheduleElection(electionId, startOn, endOn);
			}
			case EndElectionDto _ -> new EndElection(electionId);
		};
	}

}
