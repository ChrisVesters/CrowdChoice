package com.cvesters.crowdchoice.election.dto;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

public record ElectionCreateDto(String topic) {

	// TODO: different approach for validation.
	// This causes some ugly errors
	public ElectionCreateDto {
		Objects.requireNonNull(topic);
		Validate.notBlank(topic);
	}
}
