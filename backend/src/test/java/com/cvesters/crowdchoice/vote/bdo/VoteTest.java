package com.cvesters.crowdchoice.vote.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VoteTest {

	private static final long ID = 1L;
	private static final OffsetDateTime CASTED_ON = OffsetDateTime.now();
	private static final long CANDIDATE_ID = 5L;

	@Nested
	class ConstructorWithId {

		@Test
		void success() {
			final var vote = new Vote(ID, CASTED_ON, CANDIDATE_ID);

			assertThat(vote.getId()).isEqualTo(ID);
			assertThat(vote.getCastedOn()).isEqualTo(CASTED_ON);
			assertThat(vote.getCandidateId()).isEqualTo(CANDIDATE_ID);
		}
	}

	@Nested
	class ConstructorWithoutId {

		@Test
		void success() {
			final var vote = new Vote(CANDIDATE_ID);

			assertThat(vote.getId()).isNull();
			assertThat(vote.getCastedOn()).isCloseTo(OffsetDateTime.now(),
					within(500, ChronoUnit.MILLIS));
			assertThat(vote.getCandidateId()).isEqualTo(CANDIDATE_ID);
		}
	}

}
