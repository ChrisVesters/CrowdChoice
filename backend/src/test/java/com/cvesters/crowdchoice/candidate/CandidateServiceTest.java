package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.candidate.dao.CandidateDao;
import com.cvesters.crowdchoice.election.ElectionService;
import com.cvesters.crowdchoice.election.TestElection;
import com.cvesters.crowdchoice.exceptions.ConflictException;
import com.cvesters.crowdchoice.exceptions.NotFoundException;

class CandidateServiceTest {

	private static final TestCandidate CANDIDATE = TestCandidate.MICRONAUT;
	private static final long CANDIDATE_ID = CANDIDATE.id();

	private static final TestElection ELECTION = CANDIDATE.election();
	private static final long ELECTION_ID = ELECTION.id();

	private final ElectionService electionService = mock();
	private final CandidateRepository candidateRepository = mock();

	private final CandidateService candidateService = new CandidateService(
			candidateRepository, electionService);

	@Nested
	class FindAll {

		@Test
		void empty() {
			when(candidateRepository.findByElectionId(ELECTION_ID))
					.thenReturn(Collections.emptyList());

			final List<Candidate> result = candidateService
					.findAll(ELECTION_ID);

			assertThat(result).isEmpty();
		}

		@Test
		void single() {
			final List<CandidateDao> daos = List.of(CANDIDATE.dao());
			when(candidateRepository.findByElectionId(ELECTION_ID))
					.thenReturn(daos);

			final List<Candidate> result = candidateService
					.findAll(ELECTION_ID);

			assertThat(result).hasSize(1).anySatisfy(CANDIDATE::assertEquals);
		}

		@Test
		void multiple() {
			final var candidates = List.of(CANDIDATE, TestCandidate.LOMBOK);

			final List<CandidateDao> daos = candidates.stream()
					.map(TestCandidate::dao)
					.toList();
			when(candidateRepository.findByElectionId(ELECTION_ID))
					.thenReturn(daos);

			final List<Candidate> result = candidateService
					.findAll(ELECTION_ID);

			assertThat(result).hasSize(daos.size())
					.anySatisfy(CANDIDATE::assertEquals);

			for (int i = 0; i < candidates.size(); i++) {
				candidates.get(i).assertEquals(result.get(i));
			}
		}

		@Test
		void electionNotFound() {
			doThrow(new NotFoundException()).when(electionService)
					.verifyExists(ELECTION_ID);

			assertThatThrownBy(() -> candidateService.findAll(ELECTION_ID))
					.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	class Create {

		private final CandidateDao dao = CANDIDATE.dao();

		@Test
		void success() {
			final var request = new Candidate(CANDIDATE.name(),
					CANDIDATE.description());

			when(candidateRepository.save(argThat(saved -> {
				assertThat(saved.getId()).isNull();
				assertThat(saved.getElectionId()).isEqualTo(ELECTION_ID);
				assertThat(saved.getName()).isEqualTo(CANDIDATE.name());
				return true;
			}))).thenReturn(dao);

			final Candidate created = candidateService.create(ELECTION_ID,
					request);

			CANDIDATE.assertEquals(created);
		}

		@Test
		void candidateAlreadyExists() {
			final var request = new Candidate(CANDIDATE.name(),
					CANDIDATE.description());

			when(candidateRepository.existsByElectionIdAndName(ELECTION_ID,
					CANDIDATE.name())).thenReturn(true);

			assertThatThrownBy(
					() -> candidateService.create(ELECTION_ID, request))
							.isInstanceOf(ConflictException.class);

			verify(candidateRepository).existsByElectionIdAndName(ELECTION_ID,
					CANDIDATE.name());
			verifyNoMoreInteractions(candidateRepository);
		}

		@Test
		void electionNotFound() {
			final var request = new Candidate(CANDIDATE.name(),
					CANDIDATE.description());
			doThrow(new NotFoundException()).when(electionService)
					.verifyExists(ELECTION_ID);

			assertThatThrownBy(
					() -> candidateService.create(ELECTION_ID, request))
							.isInstanceOf(NotFoundException.class);
		}

		@Test
		void candidateNull() {
			final Candidate candidate = null;
			assertThatThrownBy(
					() -> candidateService.create(ELECTION_ID, candidate))
							.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() {
			final CandidateDao dao = CANDIDATE.dao();
			when(candidateRepository.findByElectionIdAndId(ELECTION_ID,
					CANDIDATE_ID)).thenReturn(Optional.of(dao));

			candidateService.delete(ELECTION_ID, CANDIDATE_ID);

			verify(candidateRepository).delete(dao);
		}

		@Test
		void electionNotFound() {
			doThrow(new NotFoundException()).when(electionService)
					.verifyExists(ELECTION_ID);

			assertThatThrownBy(
					() -> candidateService.delete(ELECTION_ID, CANDIDATE_ID))
							.isInstanceOf(NotFoundException.class);

			verify(candidateRepository, never()).delete(any());

		}

		@Test
		void candidateNotFound() {
			when(candidateRepository.findByElectionIdAndId(ELECTION_ID,
					CANDIDATE_ID)).thenReturn(Optional.empty());

			assertThatThrownBy(
					() -> candidateService.delete(ELECTION_ID, CANDIDATE_ID))
							.isInstanceOf(NotFoundException.class);

			verify(candidateRepository, never()).delete(any());
		}
	}
}
