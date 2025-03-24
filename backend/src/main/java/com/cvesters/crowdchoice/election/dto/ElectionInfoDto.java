package com.cvesters.crowdchoice.election.dto;

import java.time.OffsetDateTime;

public record ElectionInfoDto(long id, String topic, String description,
		OffsetDateTime startedOn, OffsetDateTime endedOn) {

}
