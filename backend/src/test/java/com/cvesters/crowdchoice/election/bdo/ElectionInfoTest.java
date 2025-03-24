package com.cvesters.crowdchoice.election.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.crowdchoice.election.TestElection;

public class ElectionInfoTest {

	private static final TestElection ELECTION = TestElection.FEDERAL_ELECTIONS_2024;
	private static final long ID = ELECTION.id();
	private static final String TOPIC = ELECTION.topic();
	private static final String DESCRIPTION = ELECTION.description();
	private static final OffsetDateTime STARTED_ON = ELECTION.startedOn();
	private static final OffsetDateTime ENDED_ON = ELECTION.endedOn();

	@Nested
	class ConstructorWithId {

		@Test
		void success() {
			final var info = new ElectionInfo(ID, TOPIC, DESCRIPTION,
					STARTED_ON, ENDED_ON);

			assertThat(info.getId()).isEqualTo(ID);
			assertThat(info.getTopic()).isEqualTo(TOPIC);
			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
			assertThat(info.getStartedOn()).isEqualTo(STARTED_ON);
			assertThat(info.getEndedOn()).isEqualTo(ENDED_ON);
		}

		@Test
		void topicTrimmed() {
			final var info = new ElectionInfo(ID, "  " + TOPIC + "  ",
					DESCRIPTION, STARTED_ON, ENDED_ON);

			assertThat(info.getTopic()).isEqualTo(TOPIC);
		}

		@Test
		void descriptionTrimmed() {
			final var info = new ElectionInfo(ID, TOPIC,
					"  " + DESCRIPTION + "  ", STARTED_ON, ENDED_ON);

			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
		}

		@Test
		void topicNull() {
			assertThatThrownBy(() -> new ElectionInfo(ID, null, DESCRIPTION,
					STARTED_ON, ENDED_ON))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void topicInvalid(final String topic) {
			assertThatThrownBy(() -> new ElectionInfo(ID, topic, DESCRIPTION,
					STARTED_ON, ENDED_ON))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> new ElectionInfo(ID, TOPIC, null,
					STARTED_ON, ENDED_ON))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void endedOnBeforeStartedOn() {
			assertThatThrownBy(() -> new ElectionInfo(ID, TOPIC, DESCRIPTION,
					ENDED_ON, STARTED_ON))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void endedOnSameAsStartedOn() {
			assertThatThrownBy(() -> new ElectionInfo(ID, TOPIC, DESCRIPTION,
					STARTED_ON, STARTED_ON))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void startedOnNullEndedOnNotNull() {
			assertThatThrownBy(() -> new ElectionInfo(ID, TOPIC, DESCRIPTION,
					null, ENDED_ON))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class ConstructorWithoutId {

		@Test
		void success() {
			final var info = new ElectionInfo(TOPIC, DESCRIPTION, STARTED_ON,
					ENDED_ON);

			assertThat(info.getId()).isNull();
			assertThat(info.getTopic()).isEqualTo(TOPIC);
			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
			assertThat(info.getStartedOn()).isEqualTo(STARTED_ON);
			assertThat(info.getEndedOn()).isEqualTo(ENDED_ON);
		}

		@Test
		void topicTrimmed() {
			final var info = new ElectionInfo("  " + TOPIC + "  ", DESCRIPTION,
					STARTED_ON, ENDED_ON);

			assertThat(info.getTopic()).isEqualTo(TOPIC);
		}

		@Test
		void descriptionTrimmed() {
			final var info = new ElectionInfo(TOPIC, "  " + DESCRIPTION + "  ",
					STARTED_ON, ENDED_ON);

			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
		}

		@Test
		void topicNull() {
			assertThatThrownBy(() -> new ElectionInfo(null, DESCRIPTION,
					STARTED_ON, ENDED_ON))
							.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void topicInvalid(final String topic) {
			assertThatThrownBy(() -> new ElectionInfo(topic, DESCRIPTION,
					STARTED_ON, ENDED_ON))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
