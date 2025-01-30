package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.candidate.dao.CandidateDao;
import com.cvesters.crowdchoice.candidate.dto.CandidateCreateDto;
import com.cvesters.crowdchoice.candidate.dto.CandidateDto;

class CandidateMapperTest {

	@Nested
	class FromDao {

		@Test
		void single() {
			final TestCandidate candidate = TestCandidate.MICRONAUT;
			final CandidateDao dao = candidate.dao();

			final Candidate result = CandidateMapper.fromDao(dao);

			candidate.assertEquals(result);
		}

		@Test
		void multiple() {
			final List<TestCandidate> candidates = List.of(
					TestCandidate.MICRONAUT, TestCandidate.DOCKER,
					TestCandidate.LOMBOK);

			final List<CandidateDao> dao = candidates.stream()
					.map(TestCandidate::dao)
					.toList();

			final List<Candidate> result = CandidateMapper.fromDao(dao);

			assertThat(result).hasSize(candidates.size());

			for (int i = 0; i < candidates.size(); i++) {
				candidates.get(i).assertEquals(result.get(i));
			}
		}

		@Test
		void daoNull() {
			final CandidateDao dao = null;
			assertThatThrownBy(() -> CandidateMapper.fromDao(dao))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void daosNull() {
			final List<CandidateDao> daos = null;
			assertThatThrownBy(() -> CandidateMapper.fromDao(daos))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class UpdateDao {

		private static final TestCandidate CANDIDATE = TestCandidate.MICRONAUT;
		private static final long ELECTION_ID = CANDIDATE.election().id();

		@Test
		void newDao() {
			final Candidate bdo = CANDIDATE.bdo();
			final CandidateDao dao = new CandidateDao(ELECTION_ID);

			CandidateMapper.updateDao(bdo, dao);

			assertThat(dao.getId()).isNull();
			assertThat(dao.getElectionId()).isEqualTo(ELECTION_ID);
			assertThat(dao.getName()).isEqualTo(CANDIDATE.name());
		}

		@Test
		void existingDao() {
			final Candidate bdo = CANDIDATE.bdo();
			final CandidateDao dao = CANDIDATE.dao();

			CandidateMapper.updateDao(bdo, dao);

			verify(dao).setName(CANDIDATE.name());
			verify(dao).setDescription(CANDIDATE.description());
			verifyNoMoreInteractions(dao);
		}

		@Test
		void bdoNull() {
			final Candidate bdo = null;
			final CandidateDao dao = new CandidateDao(ELECTION_ID);

			assertThatThrownBy(() -> CandidateMapper.updateDao(bdo, dao))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void daoNull() {
			final Candidate bdo = CANDIDATE.bdo();
			final CandidateDao dao = null;

			assertThatThrownBy(() -> CandidateMapper.updateDao(bdo, dao))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class FromDto {

		@Test
		void success() {
			final TestCandidate candidate = TestCandidate.MICRONAUT;
			final var dto = new CandidateCreateDto(candidate.name(),
					candidate.description());

			final Candidate result = CandidateMapper.fromDto(dto);

			assertThat(result.getId()).isNull();
			assertThat(result.getName()).isEqualTo(candidate.name());
		}

		@Test
		void dtoNull() {
			final CandidateCreateDto dto = null;

			assertThatThrownBy(() -> CandidateMapper.fromDto(dto))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToDto {

		@Test
		void single() {
			final TestCandidate candidate = TestCandidate.MICRONAUT;
			final Candidate bdo = candidate.bdo();

			final CandidateDto dto = CandidateMapper.toDto(bdo);

			assertThat(dto.id()).isEqualTo(candidate.id());
			assertThat(dto.name()).isEqualTo(candidate.name());
		}

		@Test
		void multiple() {
			final List<TestCandidate> candidates = List.of(
					TestCandidate.MICRONAUT, TestCandidate.DOCKER,
					TestCandidate.LOMBOK);

			final List<Candidate> bdos = candidates.stream()
					.map(TestCandidate::bdo)
					.toList();

			final List<CandidateDto> dtos = CandidateMapper.toDto(bdos);

			assertThat(dtos).hasSize(candidates.size());

			for (int i = 0; i < candidates.size(); i++) {
				candidates.get(i).assertEquals(dtos.get(i));
			}
		}

		@Test
		void bdoNull() {
			final Candidate candidate = null;
			assertThatThrownBy(() -> CandidateMapper.toDto(candidate))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void bdosNull() {
			final List<Candidate> candidates = null;
			assertThatThrownBy(() -> CandidateMapper.toDto(candidates))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
