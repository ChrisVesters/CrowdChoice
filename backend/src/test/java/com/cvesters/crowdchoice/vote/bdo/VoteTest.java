package com.cvesters.crowdchoice.vote.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VoteTest {

	private static final long ID = 1L;
	private static final ZonedDateTime CASTED_ON = ZonedDateTime.now();
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
			assertThat(vote.getCastedOn()).isCloseTo(ZonedDateTime.now(),
					within(500, ChronoUnit.MILLIS));
			assertThat(vote.getCandidateId()).isEqualTo(CANDIDATE_ID);
		}
	}

}
