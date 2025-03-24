package com.cvesters.crowdchoice.vote.bdo;

import java.time.OffsetDateTime;
import java.util.Objects;

import lombok.Getter;

@Getter
public class Vote {

	private Long id;
	private OffsetDateTime castedOn;
	private long candidateId;

	private Vote(final Long id, final OffsetDateTime castedOn,
			final long candidateId) {
		Objects.requireNonNull(castedOn);
		this.id = id;
		this.castedOn = castedOn;
		this.candidateId = candidateId;
	}

	public Vote(final long id, final OffsetDateTime castedOn,
			final long candidateId) {
		this(Long.valueOf(id), castedOn, candidateId);
	}

	public Vote(final long candidateId) {
		this(null, OffsetDateTime.now(), candidateId);
	}
}
