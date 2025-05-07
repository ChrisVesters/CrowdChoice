package com.cvesters.crowdchoice.election.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.election.action.bdo.ElectionAction;
import com.cvesters.crowdchoice.election.action.bdo.EndElection;
import com.cvesters.crowdchoice.election.action.bdo.ScheduleElection;
import com.cvesters.crowdchoice.election.action.bdo.StartElection;
import com.cvesters.crowdchoice.election.action.dto.EndElectionDto;
import com.cvesters.crowdchoice.election.action.dto.ScheduleElectionDto;
import com.cvesters.crowdchoice.election.action.dto.StartElectionDto;

class ElectionActionMapperTest {

	@Nested
	class FromDto {

		private static final long ELECTION_ID = 99L;

		@Test
		void dtoNull() {
			assertThatThrownBy(
					() -> ElectionActionMapper.fromDto(ELECTION_ID, null))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		void startElectionDto() {
			final var dto = new StartElectionDto();

			final ElectionAction bdo = ElectionActionMapper.fromDto(ELECTION_ID,
					dto);

			assertThat(bdo).isInstanceOf(StartElection.class);
			assertThat(bdo.getElectionId()).isEqualTo(ELECTION_ID);
		}

		@Test
		void endElectionDto() {
			final var dto = new EndElectionDto();

			final ElectionAction bdo = ElectionActionMapper.fromDto(ELECTION_ID,
					dto);

			assertThat(bdo).isInstanceOf(EndElection.class);
			assertThat(bdo.getElectionId()).isEqualTo(ELECTION_ID);
		}

		@Test
		void scheduleElectionDto() {
			final OffsetDateTime start = OffsetDateTime.now().plusDays(1);
			final OffsetDateTime end = start.plusDays(1);
			final var dto = new ScheduleElectionDto(start, end);

			final ElectionAction bdo = ElectionActionMapper.fromDto(ELECTION_ID,
					dto);

			assertThat(bdo).isInstanceOf(ScheduleElection.class);
			assertThat(bdo.getElectionId()).isEqualTo(ELECTION_ID);
		}
	}
}
