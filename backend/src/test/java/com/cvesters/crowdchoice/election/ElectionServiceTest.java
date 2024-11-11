package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;

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
			final TestElection election = TestElection.TOPICS;
			final List<ElectionDao> daos = List.of(election.dao());

			when(electionRepository.findAll()).thenReturn(daos);

			final List<ElectionInfo> found = electionService.findAll();

			assertThat(found).hasSize(1).allSatisfy(election::assertEquals);
		}

		@Test
		void multiple() {
			final List<TestElection> elections = List.of(TestElection.TOPICS,
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
	class Create {

		@Test
		void create() {
			final TestElection election = TestElection.TOPICS;
			final ElectionInfo request = new ElectionInfo(election.topic());
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
		void nullElection() {
			assertThatThrownBy(() -> electionService.create(null))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
