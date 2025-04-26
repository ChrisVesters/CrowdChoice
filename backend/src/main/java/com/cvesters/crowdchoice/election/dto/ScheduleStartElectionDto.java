package com.cvesters.crowdchoice.election.dto;

import java.time.OffsetDateTime;

public record ScheduleStartElectionDto(OffsetDateTime startOn)
		implements ElectionActionDto {

}
