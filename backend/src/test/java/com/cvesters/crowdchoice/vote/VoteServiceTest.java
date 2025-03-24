package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.candidate.CandidateService;
import com.cvesters.crowdchoice.candidate.TestCandidate;
import com.cvesters.crowdchoice.election.ElectionService;
import com.cvesters.crowdchoice.exceptions.NotFoundException;
import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteCountView;
import com.cvesters.crowdchoice.vote.dao.VoteDao;

class VoteServiceTest {

	private static final TestVote VOTE = TestVote.TRUMP;

	private static final TestCandidate CANDIDATE = VOTE.candidate();
	private static final long CANDIDATE_ID = CANDIDATE.id();
	private static final long ELECTION_ID = CANDIDATE.election().id();

	private final ElectionService electionService = mock();
	private final CandidateService candidateService = mock();
	private final VoteRepository voteRepository = mock();

	private final VoteService voteService = new VoteService(voteRepository,
			electionService, candidateService);

	@Nested
	class GetCounts {

		@Test
		void success() {
			final var candidate1 = mock(VoteCountView.class);
			final var candidate2 = mock(VoteCountView.class);

			final List<VoteCountView> counts = List.of(candidate1, candidate2);

			when(voteRepository.getCounts(ELECTION_ID)).thenReturn(counts);
			final List<VoteCountView> result = voteService
					.getCounts(ELECTION_ID);

			assertThat(result).isEqualTo(counts);
		}

		@Test
		void noVotes() {
			when(voteRepository.getCounts(ELECTION_ID))
					.thenReturn(Collections.emptyList());

			final List<VoteCountView> result = voteService
					.getCounts(ELECTION_ID);

			assertThat(result).isEmpty();
		}

		@Test
		void electionNotFound() {
			doThrow(NotFoundException.class).when(electionService)
					.verifyExists(ELECTION_ID);

			assertThatThrownBy(() -> voteService.getCounts(ELECTION_ID))
					.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	class Create {

		private final VoteDao dao = VOTE.dao();

		@Test
		void success() {
			final var request = new Vote(CANDIDATE_ID);

			when(voteRepository.save(argThat(saved -> {
				assertThat(saved.getId()).isNull();
				assertThat(saved.getCastedOn()).isCloseTo(OffsetDateTime.now(),
						within(500, ChronoUnit.MILLIS));
				assertThat(saved.getCandidateId()).isEqualTo(CANDIDATE_ID);
				return true;
			}))).thenReturn(dao);

			final Vote created = voteService.create(ELECTION_ID, request);

			VOTE.assertEquals(created);
		}

		@Test
		void electionNotFound() {
			final var request = new Vote(CANDIDATE_ID);
			doThrow(new NotFoundException()).when(electionService)
					.verifyExists(ELECTION_ID);

			assertThatThrownBy(() -> voteService.create(ELECTION_ID, request))
					.isInstanceOf(NotFoundException.class);
		}

		@Test
		void candidateNotFound() {
			final var request = new Vote(CANDIDATE_ID);
			doThrow(new NotFoundException()).when(candidateService)
					.verifyExists(ELECTION_ID, CANDIDATE_ID);

			assertThatThrownBy(() -> voteService.create(ELECTION_ID, request))
					.isInstanceOf(NotFoundException.class);
		}

		@Test
		void voteNull() {
			assertThatThrownBy(() -> voteService.create(ELECTION_ID, null))
					.isInstanceOf(NullPointerException.class);
		}

	}
}
