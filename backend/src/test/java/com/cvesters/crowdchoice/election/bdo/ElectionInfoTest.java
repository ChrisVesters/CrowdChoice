package com.cvesters.crowdchoice.election.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.OffsetDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cvesters.crowdchoice.election.TestElection;

class ElectionInfoTest {

	private static final TestElection ELECTION = TestElection.FEDERAL_ELECTIONS_2024;
	private static final long ID = ELECTION.id();
	private static final String TOPIC = ELECTION.topic();
	private static final String DESCRIPTION = ELECTION.description();
	private static final OffsetDateTime STARTED_ON = ELECTION.startedOn();
	private static final OffsetDateTime ENDED_ON = ELECTION.endedOn();

	private static final OffsetDateTime TODAY = OffsetDateTime.now();
	private static final OffsetDateTime YESTERDAY = TODAY.minusDays(1);
	private static final OffsetDateTime TOMORROW = TODAY.plusDays(1);
	private static final OffsetDateTime AFTER_TOMORROW = TOMORROW.plusDays(1);

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

	@Nested
	class isEditable {

		@Test
		void draft() {
			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					null, null);

			assertThat(election.isEditable()).isTrue();
		}

		@Test
		void scheduled() {
			final OffsetDateTime startsOn = OffsetDateTime.now()
					.plus(Period.ofDays(7));
			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					startsOn, null);

			assertThat(election.isEditable()).isTrue();
		}

		@Test
		void ongoing() {
			final OffsetDateTime startedOn = OffsetDateTime.now()
					.minus(Period.ofDays(7));
			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					startedOn, null);

			assertThat(election.isEditable()).isFalse();
		}

		@Test
		void ended() {
			final OffsetDateTime startedOn = OffsetDateTime.now()
					.minus(Period.ofDays(7));
			final OffsetDateTime endedOn = OffsetDateTime.now()
					.minus(Period.ofDays(6));

			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					startedOn, endedOn);

			assertThat(election.isEditable()).isFalse();
		}
	}

	@Nested
	class isActive {

		@Test
		void draft() {
			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					null, null);

			assertThat(election.isActive()).isFalse();
		}

		@Test
		void scheduled() {
			final OffsetDateTime startsOn = OffsetDateTime.now()
					.plus(Period.ofDays(7));
			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					startsOn, null);

			assertThat(election.isActive()).isFalse();
		}

		@Test
		void ongoing() {
			final OffsetDateTime startedOn = OffsetDateTime.now()
					.minus(Period.ofDays(7));
			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					startedOn, null);

			assertThat(election.isActive()).isTrue();
		}

		@Test
		void ended() {
			final OffsetDateTime startedOn = OffsetDateTime.now()
					.minus(Period.ofDays(7));
			final OffsetDateTime endedOn = OffsetDateTime.now()
					.minus(Period.ofDays(6));

			final ElectionInfo election = new ElectionInfo(TOPIC, DESCRIPTION,
					startedOn, endedOn);

			assertThat(election.isActive()).isFalse();
		}
	}

	// TODO
	@Nested
	class Schedule {

		@Test
		void scheduleStartAndEndDraft() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, null,
					null);

			election.schedule(TOMORROW, AFTER_TOMORROW);

			assertThat(election.getStartedOn()).isEqualTo(TOMORROW);
			assertThat(election.getEndedOn()).isEqualTo(AFTER_TOMORROW);
		}

		@Test
		void scheduleStartDraft() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, null,
					null);

			election.schedule(TOMORROW, null);

			assertThat(election.getStartedOn()).isEqualTo(TOMORROW);
			assertThat(election.getEndedOn()).isNull();
		}

		@Test
		void scheduleStartScheduled() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, TOMORROW,
					null);

			election.schedule(AFTER_TOMORROW, null);

			assertThat(election.getStartedOn()).isEqualTo(AFTER_TOMORROW);
			assertThat(election.getEndedOn()).isNull();
		}

		@Test
		void scheduleStartInProgress() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					null);

			assertThatThrownBy(() -> election.schedule(TOMORROW, null))
					.isInstanceOf(IllegalStateException.class);
		}

		@Test
		void scheduleEndScheduled() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, TOMORROW,
					null);

			election.schedule(TOMORROW, AFTER_TOMORROW);

			assertThat(election.getStartedOn()).isEqualTo(TOMORROW);
			assertThat(election.getEndedOn()).isEqualTo(AFTER_TOMORROW);
		}

		@Test
		void scheduleEndInProgress() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					TOMORROW);

			election.schedule(YESTERDAY, AFTER_TOMORROW);

			assertThat(election.getStartedOn()).isEqualTo(YESTERDAY);
			assertThat(election.getEndedOn()).isEqualTo(AFTER_TOMORROW);
		}

		@Test
		void scheduleEndEnded() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					TODAY);

			assertThatThrownBy(() -> election.schedule(YESTERDAY, TOMORROW))
					.isInstanceOf(IllegalStateException.class);
		}

		@Test
		void unschedule() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, TOMORROW,
					AFTER_TOMORROW);

			election.schedule(null, null);

			assertThat(election.getStartedOn()).isNull();
			assertThat(election.getEndedOn()).isNull();
		}

		@Test
		void unscheduleStartScheduled() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, TOMORROW,
					null);

			election.schedule(null, null);

			assertThat(election.getStartedOn()).isNull();
			assertThat(election.getEndedOn()).isNull();
		}

		@Test
		void unscheduledStartInProgress() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					null);

			assertThatThrownBy(() -> election.schedule(null, null))
					.isInstanceOf(IllegalStateException.class);
		}

		@Test
		void unscheduleEndScheduled() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, TOMORROW,
					AFTER_TOMORROW);

			election.schedule(TOMORROW, null);

			assertThat(election.getStartedOn()).isEqualTo(TOMORROW);
			assertThat(election.getEndedOn()).isNull();
		}

		@Test
		void unscheduleEndInProgress() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY, TOMORROW);

			election.schedule(YESTERDAY, null);

			assertThat(election.getStartedOn()).isEqualTo(YESTERDAY);
			assertThat(election.getEndedOn()).isNull();
		}

		@Test
		void unscheduleEndEnded() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					TODAY);

			assertThatThrownBy(() -> election.schedule(YESTERDAY, null))
					.isInstanceOf(IllegalStateException.class);
		}

		@Test
		void endBeforeStart() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, null,
					null);

			assertThatThrownBy(
					() -> election.schedule(AFTER_TOMORROW, TOMORROW))
							.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void endWithoutStart() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, null,
					null);

			assertThatThrownBy(() -> election.schedule(null, TOMORROW))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void startInPast() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, null,
					null);

			assertThatThrownBy(() -> election.schedule(YESTERDAY, TOMORROW))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void endInPast() {
			final var BEFORE_YESTERDAY = YESTERDAY.minusDays(1);
			final var election = new ElectionInfo(TOPIC, DESCRIPTION,
					BEFORE_YESTERDAY, null);

			assertThatThrownBy(
					() -> election.schedule(BEFORE_YESTERDAY, YESTERDAY))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class Start {

		@Test
		void success() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, null,
					null);

			election.start();

			assertThat(election.getStartedOn()).isCloseTo(OffsetDateTime.now(),
					within(100, ChronoUnit.MILLIS));
		}

		@Test
		void scheduled() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, TOMORROW,
					null);

			election.start();

			assertThat(election.getStartedOn()).isCloseTo(OffsetDateTime.now(),
					within(100, ChronoUnit.MILLIS));
		}

		@Test
		void alreadyStarted() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					null);

			assertThatThrownBy(() -> election.start())
					.isInstanceOf(IllegalStateException.class);
		}
	}

	@Nested
	class End {

		@Test
		void success() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					null);

			election.end();

			assertThat(election.getEndedOn()).isCloseTo(OffsetDateTime.now(),
					within(100, ChronoUnit.MILLIS));
		}

		@Test
		void scheduled() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					TOMORROW);

			election.end();

			assertThat(election.getEndedOn()).isCloseTo(OffsetDateTime.now(),
					within(100, ChronoUnit.MILLIS));
		}

		@Test
		void alreadyEnded() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, YESTERDAY,
					TODAY);

			assertThatThrownBy(() -> election.end())
					.isInstanceOf(IllegalStateException.class);
		}

		@Test
		void notStarted() {
			final var election = new ElectionInfo(TOPIC, DESCRIPTION, TOMORROW,
					null);

			assertThatThrownBy(() -> election.end())
					.isInstanceOf(IllegalStateException.class);
		}
	}
}
