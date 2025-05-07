package com.cvesters.crowdchoice.election.action.dto;

import java.time.OffsetDateTime;

public record ScheduleElectionDto(OffsetDateTime startOn, OffsetDateTime endOn)
		implements ElectionActionDto {

}
