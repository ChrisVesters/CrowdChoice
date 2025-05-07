package com.cvesters.crowdchoice.election.action.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;

class ScheduleElectionTest {

	private static final long ELECTION_ID = 926L;

	private static final OffsetDateTime START_ON = OffsetDateTime.now();
	private static final OffsetDateTime END_ON = START_ON.plusDays(2);

	@Nested
	class Constructor {

		@Test
		void success() {
			final ScheduleElection action = new ScheduleElection(ELECTION_ID,
					START_ON, END_ON);

			assertThat(action).isNotNull();
			assertThat(action.getElectionId()).isEqualTo(ELECTION_ID);
		}

		@Test
		void startAfterEnd() {
			assertThatThrownBy(
					() -> new ScheduleElection(ELECTION_ID, END_ON, START_ON))
							.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class Apply {

		private final ElectionInfo info = mock();

		private final ScheduleElection action = new ScheduleElection(
				ELECTION_ID, START_ON, END_ON);

		@Test
		void success() {
			when(info.getId()).thenReturn(ELECTION_ID);

			action.apply(info);

			verify(info).schedule(START_ON, END_ON);
		}

		@Test
		void infoNull() {
			assertThatThrownBy(() -> action.apply(null))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void infoIdNull() {
			when(info.getId()).thenReturn(null);

			assertThatThrownBy(() -> action.apply(info))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void infoIdMismatch() {
			when(info.getId()).thenReturn(ELECTION_ID + 1);

			assertThatThrownBy(() -> action.apply(info))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		void operationNotAllowed() {
			when(info.getId()).thenReturn(ELECTION_ID);
			doThrow(IllegalArgumentException.class).when(info)
					.schedule(any(), any());

			assertThatThrownBy(() -> action.apply(info))
					.isInstanceOf(OperationNotAllowedException.class);
		}
	}
}
