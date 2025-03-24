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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void single(final TestElection election) {
			final List<ElectionDao> daos = List.of(election.dao());

			when(electionRepository.findAll()).thenReturn(daos);

			final List<ElectionInfo> found = electionService.findAll();

			assertThat(found).hasSize(1).allSatisfy(election::assertEquals);
		}

		@Test
		void multiple() {
			final var elections = TestElection.ALL;
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

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void success(final TestElection election) {
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

		private static final long ELECTION_ID = 12L;

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

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void create(final TestElection election) {
			final var request = new ElectionInfo(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());
			final ElectionDao expectedDao = election.dao();

			when(electionRepository.save(argThat(dao -> {
				assertThat(dao.getId()).isNull();
				assertThat(dao.getTopic()).isEqualTo(election.topic());
				assertThat(dao.getDescription())
						.isEqualTo(election.description());
				assertThat(dao.getStartedOn()).isEqualTo(election.startedOn());
				assertThat(dao.getEndedOn()).isEqualTo(election.endedOn());
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

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void success(final TestElection election) {
			final ElectionDao dao = election.dao();
			when(electionRepository.findById(election.id()))
					.thenReturn(Optional.of(dao));

			electionService.delete(election.id());

			verify(electionRepository).delete(dao);
		}

		@Test
		void electionNotFound() {
			final long electionId = 123L;
			when(electionRepository.findById(electionId))
					.thenReturn(Optional.empty());

			assertThatThrownBy(() -> electionService.delete(electionId))
					.isInstanceOf(NotFoundException.class);
		}
	}
}
