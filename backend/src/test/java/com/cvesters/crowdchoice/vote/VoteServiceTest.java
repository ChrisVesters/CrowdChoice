package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.candidate.CandidateService;
import com.cvesters.crowdchoice.candidate.TestCandidate;
import com.cvesters.crowdchoice.exceptions.NotFoundException;
import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteDao;

class VoteServiceTest {

	private static final TestVote VOTE = TestVote.TRUMP;

	private static final TestCandidate CANDIDATE = VOTE.candidate();
	private static final long CANDIDATE_ID = CANDIDATE.id();
	private static final long ELECTION_ID = CANDIDATE.election().id();

	private final CandidateService candidateService = mock();
	private final VoteRepository voteRepository = mock();

	private final VoteService voteService = new VoteService(voteRepository,
			candidateService);

	@Nested
	class Create {

		private final VoteDao dao = VOTE.dao();

		@Test
		void success() {
			final var request = new Vote(CANDIDATE_ID);

			when(voteRepository.save(argThat(saved -> {
				assertThat(saved.getId()).isNull();
				assertThat(saved.getCastedOn()).isCloseTo(ZonedDateTime.now(),
						within(500, ChronoUnit.MILLIS));
				assertThat(saved.getCandidateId()).isEqualTo(CANDIDATE_ID);
				return true;
			}))).thenReturn(dao);

			final Vote created = voteService.create(ELECTION_ID, request);

			VOTE.assertEquals(created);
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
