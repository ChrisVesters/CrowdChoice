package com.cvesters.crowdchoice.vote.dto;

import java.util.Objects;

public record VoteCreateDto(Long candidateId) {

	// TODO: different approach for validation.
	// This causes some ugly errors
	public VoteCreateDto {
		Objects.requireNonNull(candidateId);
	}

}
