package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.cvesters.crowdchoice.vote.bdo.Vote;
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
	class toDto {

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
}
