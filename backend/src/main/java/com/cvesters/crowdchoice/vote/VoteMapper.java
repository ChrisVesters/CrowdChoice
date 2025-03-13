package com.cvesters.crowdchoice.vote;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteCountView;
import com.cvesters.crowdchoice.vote.dao.VoteDao;
import com.cvesters.crowdchoice.vote.dto.VoteCountDto;
import com.cvesters.crowdchoice.vote.dto.VoteCreateDto;
import com.cvesters.crowdchoice.vote.dto.VoteDto;

public final class VoteMapper {

	private VoteMapper() {
	}

	public static Vote fromDao(final VoteDao dao) {
		Objects.requireNonNull(dao);

		return new Vote(dao.getId(), dao.getCastedOn(), dao.getCandidateId());
	}

	public static Vote fromDto(final VoteCreateDto dto) {
		Objects.requireNonNull(dto);

		return new Vote(dto.candidateId());
	}

	public static VoteDto toDto(final Vote vote) {
		Objects.requireNonNull(vote);

		final String castedOn = DateTimeFormatter.ISO_DATE_TIME
				.format(vote.getCastedOn());
		return new VoteDto(vote.getId(), castedOn, vote.getCandidateId());
	}

	public static List<VoteCountDto> countView(
			final List<VoteCountView> views) {
		Objects.requireNonNull(views);

		return views.stream().map(VoteMapper::countView).toList();
	}

	public static VoteCountDto countView(final VoteCountView view) {
		Objects.requireNonNull(view);

		return new VoteCountDto(view.getCandidateId(), view.getVoteCount());
	}
}
