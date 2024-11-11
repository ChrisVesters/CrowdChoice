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

	@Nested
	class ConstructorWithId {

		@Test
		void success() {
			final var info = new ElectionInfo(ID, TOPIC);

			assertThat(info.getId()).isEqualTo(ID);
			assertThat(info.getTopic()).isEqualTo(TOPIC);
		}

		@Test
		void topicNull() {
			assertThatThrownBy(() -> new ElectionInfo(ID, null))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void topicInvalid(final String topic) {
			assertThatThrownBy(() -> new ElectionInfo(ID, topic))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class ConstructorWithoutId {

		@Test
		void success() {
			final var info = new ElectionInfo(TOPIC);

			assertThat(info.getId()).isNull();
			assertThat(info.getTopic()).isEqualTo(TOPIC);
		}

		@Test
		void topicNull() {
			assertThatThrownBy(() -> new ElectionInfo(null))
					.isInstanceOf(NullPointerException.class);
		}

		@ParameterizedTest
		@ValueSource(strings = { "", " " })
		void topicInvalid(final String topic) {
			assertThatThrownBy(() -> new ElectionInfo(topic))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
