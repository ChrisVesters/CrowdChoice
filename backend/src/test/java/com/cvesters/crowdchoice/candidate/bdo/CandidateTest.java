package com.cvesters.crowdchoice.candidate.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CandidateTest {

	private static final long ID = 1L;
	private static final String NAME = "Spring";

	@Nested
	class ConstructorWithId {

		@Test
		void success() {
			final var candidate = new Candidate(ID, NAME);

			assertThat(candidate.getId()).isEqualTo(ID);
			assertThat(candidate.getName()).isEqualTo(NAME);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> new Candidate(ID, null))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> new Candidate(ID, name))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class ConstructorWithoutId {
		@Test
		void success() {
			final var candidate = new Candidate(NAME);

			assertThat(candidate.getId()).isNull();
			assertThat(candidate.getName()).isEqualTo(NAME);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> new Candidate(null))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> new Candidate(name))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
