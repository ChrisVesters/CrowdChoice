package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.mockito.quality.Strictness;

import com.cvesters.crowdchoice.candidate.TestCandidate;
import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteDao;
import com.cvesters.crowdchoice.vote.dto.VoteDto;

public record TestVote(long id, ZonedDateTime castedOn,
		TestCandidate candidate) {

	public static final TestVote TRUMP = new TestVote(1L,
			ZonedDateTime.of(2024, 12, 1, 8, 30, 0, 0, ZoneOffset.UTC),
			TestCandidate.TRUMP);

	public Vote bdo() {
		return new Vote(id, castedOn, candidate.id());
	}

	public VoteDto dto() {
		final String castedOnString = DateTimeFormatter.ISO_DATE_TIME.format(castedOn);
		return new VoteDto(id, castedOnString, candidate.id());
	}

	public VoteDao dao() {
		final var dao = mock(VoteDao.class,
				withSettings().strictness(Strictness.LENIENT));

		when(dao.getId()).thenReturn(id);
		when(dao.getCastedOn()).thenReturn(castedOn);
		when(dao.getCandidateId()).thenReturn(candidate.id());

		return dao;
	}

	public void assertEquals(final Vote vote) {
		assertThat(vote.getId()).isEqualTo(id);
		assertThat(vote.getCastedOn()).isEqualTo(castedOn);
		assertThat(vote.getCandidateId()).isEqualTo(candidate.id());
	}

	public void assertEquals(final VoteDto dto) {
		assertThat(dto.id()).isEqualTo(id);
		assertThat(dto.castedOn()).isEqualTo(DateTimeFormatter.ISO_DATE_TIME.format(castedOn));
		assertThat(dto.candidateId()).isEqualTo(candidate.id());
	}
}
