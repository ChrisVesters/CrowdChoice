package com.cvesters.crowdchoice.election.dto;

import java.time.OffsetDateTime;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

public record ElectionUpdateDto(String topic, String description,
		OffsetDateTime startedOn, OffsetDateTime endedOn) {

	// TODO: different approach for validation.
	// This causes some ugly errors
	public ElectionUpdateDto {
		Objects.requireNonNull(topic);
		Validate.notBlank(topic);
		Objects.requireNonNull(description);
	}
}
