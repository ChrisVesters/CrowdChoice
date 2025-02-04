package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.election.dto.ElectionCreateDto;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;

class ElectionMapperTest {

	@Nested
	class FromDao {

		@Test
		void single() {
			final TestElection election = TestElection.TOPICS;
			final ElectionDao dao = election.dao();

			final ElectionInfo info = ElectionMapper.fromDao(dao);

			election.assertEquals(info);
		}

		@Test
		void multiple() {
			final List<TestElection> elections = List.of(TestElection.TOPICS,
					TestElection.FEDERAL_ELECTIONS_2024);

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

		@Test
		void newDao() {
			final TestElection election = TestElection.TOPICS;
			final ElectionInfo bdo = election.info();
			final ElectionDao dao = new ElectionDao();

			ElectionMapper.updateDao(bdo, dao);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getTopic()).isEqualTo(election.topic());
			assertThat(dao.getDescription()).isEqualTo(election.description());
		}

		@Test
		void existingDao() {
			final TestElection election = TestElection.TOPICS;
			final ElectionInfo bdo = election.info();
			final ElectionDao dao = election.dao();

			ElectionMapper.updateDao(bdo, dao);

			verify(dao).setTopic(election.topic());
			verify(dao).setDescription(election.description());
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

		@Test
		void success() {
			final TestElection election = TestElection.TOPICS;
			final var dto = new ElectionCreateDto(election.topic(),
					election.description());

			final ElectionInfo info = ElectionMapper.fromDto(dto);

			assertThat(info.getId()).isNull();
			assertThat(info.getTopic()).isEqualTo(election.topic());
			assertThat(info.getDescription()).isEqualTo(election.description());
		}

		@Test
		void dtoNull() {
			assertThatThrownBy(() -> ElectionMapper.fromDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToDto {

		@Test
		void single() {
			final TestElection election = TestElection.TOPICS;
			final ElectionInfo info = election.info();

			final ElectionInfoDto dto = ElectionMapper.toDto(info);

			election.assertEquals(dto);
		}

		@Test
		void multiple() {
			final List<TestElection> elections = List.of(TestElection.TOPICS,
					TestElection.FEDERAL_ELECTIONS_2024);

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
