package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.election.dto.ElectionCreateDto;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;

class ElectionMapperTest {

	@Nested
	class FromDao {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void single(final TestElection election) {
			final ElectionDao dao = election.dao();

			final ElectionInfo info = ElectionMapper.fromDao(dao);

			election.assertEquals(info);
		}

		@Test
		void multiple() {
			final List<TestElection> elections = TestElection.ALL;

			final List<ElectionDao> dao = elections.stream()
					.map(TestElection::dao)
					.toList();

			final List<ElectionInfo> infos = ElectionMapper.fromDao(dao);

			assertThat(infos).hasSize(elections.size());

			for (int i = 0; i < elections.size(); i++) {
				elections.get(i).assertEquals(infos.get(i));
			}
		}

		@Test
		void daoNull() {
			final ElectionDao dao = null;

			assertThatThrownBy(() -> ElectionMapper.fromDao(dao))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void daosNull() {
			final List<ElectionDao> daos = null;

			assertThatThrownBy(() -> ElectionMapper.fromDao(daos))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateDao {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void newDao(final TestElection election) {
			final ElectionInfo bdo = election.info();
			final ElectionDao dao = new ElectionDao();

			ElectionMapper.updateDao(bdo, dao);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getTopic()).isEqualTo(election.topic());
			assertThat(dao.getDescription()).isEqualTo(election.description());
			assertThat(dao.getStartedOn()).isEqualTo(election.startedOn());
			assertThat(dao.getEndedOn()).isEqualTo(election.endedOn());
		}

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void existingDao(final TestElection election) {
			final ElectionInfo bdo = election.info();
			final ElectionDao dao = election.dao();

			ElectionMapper.updateDao(bdo, dao);

			verify(dao).setTopic(election.topic());
			verify(dao).setDescription(election.description());
			verify(dao).setStartedOn(election.startedOn());
			verify(dao).setEndedOn(election.endedOn());
			verifyNoMoreInteractions(dao);
		}

		@Test
		void bdoNull() {
			final ElectionDao dao = TestElection.TOPICS.dao();

			assertThatThrownBy(() -> ElectionMapper.updateDao(null, dao))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void daoNull() {
			final ElectionInfo bdo = TestElection.TOPICS.info();

			assertThatThrownBy(() -> ElectionMapper.updateDao(bdo, null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class FromDto {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void success(final TestElection election) {
			final var dto = new ElectionCreateDto(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());

			final ElectionInfo info = ElectionMapper.fromDto(dto);

			assertThat(info.getId()).isNull();
			assertThat(info.getTopic()).isEqualTo(election.topic());
			assertThat(info.getDescription()).isEqualTo(election.description());
			assertThat(info.getStartedOn()).isEqualTo(election.startedOn());
			assertThat(info.getEndedOn()).isEqualTo(election.endedOn());
		}

		@Test
		void dtoNull() {
			assertThatThrownBy(() -> ElectionMapper.fromDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToDto {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void single(final TestElection election) {
			final ElectionInfo info = election.info();

			final ElectionInfoDto dto = ElectionMapper.toDto(info);

			election.assertEquals(dto);
		}

		@Test
		void multiple() {
			final List<TestElection> elections = TestElection.ALL;

			final List<ElectionInfo> infos = elections.stream()
					.map(TestElection::info)
					.toList();

			final List<ElectionInfoDto> dtos = ElectionMapper.toDto(infos);

			assertThat(dtos).hasSize(elections.size());

			for (int i = 0; i < elections.size(); i++) {
				elections.get(i).assertEquals(dtos.get(i));
			}
		}

		@Test
		void bdoNull() {
			final ElectionInfo info = null;

			assertThatThrownBy(() -> ElectionMapper.toDto(info))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void bdosNull() {
			final List<ElectionInfo> infos = null;

			assertThatThrownBy(() -> ElectionMapper.toDto(infos))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
