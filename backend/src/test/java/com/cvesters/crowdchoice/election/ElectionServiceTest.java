package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.exceptions.NotFoundException;

class ElectionServiceTest {

	private final ElectionRepository electionRepository = mock();
	private final ElectionService electionService = new ElectionService(
			electionRepository);

	@Nested
	class FindAll {

		@Test
		void empty() {
			when(electionRepository.findAll())
					.thenReturn(Collections.emptyList());

			final List<ElectionInfo> found = electionService.findAll();

			assertThat(found).isEmpty();
		}

		@Test
		void single() {
			final var election = TestElection.TOPICS;
			final List<ElectionDao> daos = List.of(election.dao());

			when(electionRepository.findAll()).thenReturn(daos);

			final List<ElectionInfo> found = electionService.findAll();

			assertThat(found).hasSize(1).allSatisfy(election::assertEquals);
		}

		@Test
		void multiple() {
			final var elections = List.of(TestElection.TOPICS,
					TestElection.FEDERAL_ELECTIONS_2024);
			final List<ElectionDao> daos = elections.stream()
					.map(TestElection::dao)
					.toList();

			when(electionRepository.findAll()).thenReturn(daos);

			final List<ElectionInfo> found = electionService.findAll();

			assertThat(found).hasSize(elections.size());
			IntStream.range(0, elections.size())
					.forEach(i -> elections.get(i).assertEquals(found.get(i)));
		}
	}

	@Nested
	class Get {

		@Test
		void success() {
			final TestElection election = TestElection.TOPICS;
			final ElectionDao dao = election.dao();

			when(electionRepository.findById(1L)).thenReturn(Optional.of(dao));

			final ElectionInfo found = electionService.get(1L);

			election.assertEquals(found);
		}

		@Test
		void notFound() {
			when(electionRepository.findById(1L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> electionService.get(1L))
					.isInstanceOf(NotFoundException.class);

		}
	}

	@Nested
	class VerifyExists {

		private static final TestElection ELECTION = TestElection.TOPICS;
		private static final long ELECTION_ID = ELECTION.id();

		@Test
		void exists() {
			when(electionRepository.existsById(ELECTION_ID)).thenReturn(true);

			assertThatNoException().isThrownBy(
					() -> electionService.verifyExists(ELECTION_ID));
		}

		@Test
		void doesNotExist() {
			when(electionRepository.existsById(ELECTION_ID)).thenReturn(false);

			assertThatThrownBy(() -> electionService.verifyExists(ELECTION_ID))
					.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	class Create {

		@Test
		void create() {
			final var election = TestElection.TOPICS;
			final var request = new ElectionInfo(election.topic(),
					election.description());
			final ElectionDao expectedDao = election.dao();

			when(electionRepository.save(argThat(dao -> {
				assertThat(dao.getId()).isNull();
				assertThat(dao.getTopic()).isEqualTo(election.topic());
				return true;
			}))).thenReturn(expectedDao);

			final ElectionInfo created = electionService.create(request);

			election.assertEquals(created);
		}

		@Test
		void electionNull() {
			assertThatThrownBy(() -> electionService.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class Delete {

		private static final TestElection ELECTION = TestElection.TOPICS;
		private static final long ELECTION_ID = ELECTION.id();

		@Test
		void success() {

			final ElectionDao dao = ELECTION.dao();
			when(electionRepository.findById(ELECTION_ID))
					.thenReturn(Optional.of(dao));

			electionService.delete(ELECTION_ID);

			verify(electionRepository).delete(dao);
		}

		@Test
		void electionNotFound() {
			when(electionRepository.findById(ELECTION_ID))
					.thenReturn(Optional.empty());

			assertThatThrownBy(() -> electionService.delete(ELECTION_ID))
					.isInstanceOf(NotFoundException.class);
		}
	}
}
