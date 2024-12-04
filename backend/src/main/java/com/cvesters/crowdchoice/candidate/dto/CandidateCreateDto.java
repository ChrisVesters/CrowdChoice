package com.cvesters.crowdchoice.candidate.dto;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

public record CandidateCreateDto(String name, String description) {

	// TODO: different approach for validation.
	// This causes some ugly errors
	public CandidateCreateDto {
		Objects.requireNonNull(name);
		Validate.notBlank(name);
		Objects.requireNonNull(description);
	}
}
