package com.cvesters.crowdchoice.election.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ElectionInfoTest {

	private static final long ID = 344L;
	private static final String TOPIC = "Topic";
	private static final String DESCRIPTION = "Description";

	@Nested
	class ConstructorWithId {

		@Test
		void success() {
			final var info = new ElectionInfo(ID, TOPIC, DESCRIPTION);

			assertThat(info.getId()).isEqualTo(ID);
			assertThat(info.getTopic()).isEqualTo(TOPIC);
			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
		}

		@Test
		void topicTrimmed() {
			final var info = new ElectionInfo(ID, "  " + TOPIC + "  ",
					DESCRIPTION);

			assertThat(info.getTopic()).isEqualTo(TOPIC);
		}

		@Test
		void descriptionTrimmed() {
			final var info = new ElectionInfo(ID, TOPIC,
					"  " + DESCRIPTION + "  ");

			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
		}

		@Test
		void topicNull() {
			assertThatThrownBy(() -> new ElectionInfo(ID, null, DESCRIPTION))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void topicInvalid(final String topic) {
			assertThatThrownBy(() -> new ElectionInfo(ID, topic, DESCRIPTION))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void descriptionNull() {
			assertThatThrownBy(() -> new ElectionInfo(ID, TOPIC, null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ConstructorWithoutId {

		@Test
		void success() {
			final var info = new ElectionInfo(TOPIC, DESCRIPTION);

			assertThat(info.getId()).isNull();
			assertThat(info.getTopic()).isEqualTo(TOPIC);
			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
		}

		@Test
		void topicTrimmed() {
			final var info = new ElectionInfo("  " + TOPIC + "  ", DESCRIPTION);

			assertThat(info.getTopic()).isEqualTo(TOPIC);
		}

		@Test
		void descriptionTrimmed() {
			final var info = new ElectionInfo(TOPIC, "  " + DESCRIPTION + "  ");

			assertThat(info.getDescription()).isEqualTo(DESCRIPTION);
		}

		@Test
		void topicNull() {
			assertThatThrownBy(() -> new ElectionInfo(null, DESCRIPTION))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void topicInvalid(final String topic) {
			assertThatThrownBy(() -> new ElectionInfo(topic, DESCRIPTION))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
