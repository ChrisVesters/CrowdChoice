package com.cvesters.crowdchoice.election.action.bdo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;

class EndElectionTest {

	private static final long ELECTION_ID = 926L;

	@Nested
	class Constructor {

		@Test
		void success() {
			final EndElection action = new EndElection(ELECTION_ID);

			assertThat(action).isNotNull();
			assertThat(action.getElectionId()).isEqualTo(ELECTION_ID);
		}
	}

	@Nested
	class Apply {

		private final ElectionInfo info = mock();

		private final EndElection action = new EndElection(ELECTION_ID);

		@Test
		void success() {
			when(info.getId()).thenReturn(ELECTION_ID);

			action.apply(info);

			verify(info).end();
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
			doThrow(IllegalArgumentException.class).when(info).end();

			assertThatThrownBy(() -> action.apply(info))
					.isInstanceOf(OperationNotAllowedException.class);
		}
	}
}
