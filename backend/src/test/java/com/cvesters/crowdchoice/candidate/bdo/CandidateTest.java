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
	private static final String DESCRIPTION = "Dependency injection framework";

	@Nested
	class ConstructorWithId {

		@Test
		void success() {
			final var candidate = new Candidate(ID, NAME, DESCRIPTION);

			assertThat(candidate.getId()).isEqualTo(ID);
			assertThat(candidate.getName()).isEqualTo(NAME);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> new Candidate(ID, null, DESCRIPTION))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> new Candidate(ID, name, DESCRIPTION))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> new Candidate(ID, NAME, null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ConstructorWithoutId {
		@Test
		void success() {
			final var candidate = new Candidate(NAME, DESCRIPTION);

			assertThat(candidate.getId()).isNull();
			assertThat(candidate.getName()).isEqualTo(NAME);
		}

		@Test
		void nameNull() {
			assertThatThrownBy(() -> new Candidate(null, DESCRIPTION))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void nameInvalid(final String name) {
			assertThatThrownBy(() -> new Candidate(name, DESCRIPTION))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> new Candidate(NAME, null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
