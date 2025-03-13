package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteCountView;
import com.cvesters.crowdchoice.vote.dto.VoteCountDto;
import com.cvesters.crowdchoice.vote.dto.VoteCreateDto;
import com.cvesters.crowdchoice.vote.dto.VoteDto;

class VoteMapperTest {

	private final TestVote VOTE = TestVote.TRUMP;

	@Nested
	class FromDao {

		@Test
		void success() {
			final Vote result = VoteMapper.fromDao(VOTE.dao());

			VOTE.assertEquals(result);
		}

		@Test
		void daoNull() {
			assertThatThrownBy(() -> VoteMapper.fromDao(null))
					.isInstanceOf(NullPointerException.class);
		}

	}

	@Nested
	class FromDto {

		@Test
		void success() {
			final var dto = new VoteCreateDto(VOTE.candidate().id());

			final Vote result = VoteMapper.fromDto(dto);

			assertThat(result.getId()).isNull();
			assertThat(result.getCastedOn()).isCloseTo(ZonedDateTime.now(),
					within(500, ChronoUnit.MILLIS));
			assertThat(result.getCandidateId())
					.isEqualTo(VOTE.candidate().id());
		}

		@Test
		void dtoNull() {
			assertThatThrownBy(() -> VoteMapper.fromDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class ToDto {

		@Test
		void success() {
			final VoteDto result = VoteMapper.toDto(VOTE.bdo());

			VOTE.assertEquals(result);
		}

		@Test
		void bdoNull() {
			assertThatThrownBy(() -> VoteMapper.toDto(null))
					.isInstanceOf(NullPointerException.class);
		}
	}

	@Nested
	class CountView {

		@Test
		void single() {
			final long candidateId = 1L;
			final long voteCount = 19L;

			final var view = mock(VoteCountView.class);
			when(view.getCandidateId()).thenReturn(candidateId);
			when(view.getVoteCount()).thenReturn(voteCount);

			final VoteCountDto dto = VoteMapper.countView(view);

			assertThat(dto.candidateId()).isEqualTo(candidateId);
			assertThat(dto.voteCount()).isEqualTo(voteCount);
		}

		@Test
		void multiple() {
			final long candidate1Id = 1L;
			final long vote1Count = 19L;

			final var view1 = mock(VoteCountView.class);
			when(view1.getCandidateId()).thenReturn(candidate1Id);
			when(view1.getVoteCount()).thenReturn(vote1Count);

			final long candidate2Id = 2L;
			final long vote2Count = 7L;

			final var view2 = mock(VoteCountView.class);
			when(view2.getCandidateId()).thenReturn(candidate2Id);
			when(view2.getVoteCount()).thenReturn(vote2Count);

			final List<VoteCountDto> dtos = VoteMapper
					.countView(List.of(view1, view2));

			assertThat(dtos).hasSize(2).satisfies(dto -> {
				assertThat(dto.candidateId()).isEqualTo(candidate1Id);
				assertThat(dto.voteCount()).isEqualTo(vote1Count);
			}, atIndex(0)).satisfies(dto -> {
				assertThat(dto.candidateId()).isEqualTo(candidate2Id);
				assertThat(dto.voteCount()).isEqualTo(vote2Count);
			}, atIndex(1));
		}

		@Test
		void viewNull() {
			final VoteCountView view = null;

			assertThatThrownBy(() -> VoteMapper.countView(view))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		void viewsNull() {
			final List<VoteCountView> views = null;

			assertThatThrownBy(() -> VoteMapper.countView(views))
					.isInstanceOf(NullPointerException.class);
		}
	}
}
