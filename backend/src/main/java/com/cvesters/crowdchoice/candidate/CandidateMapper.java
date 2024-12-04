package com.cvesters.crowdchoice.candidate;

import java.util.List;
import java.util.Objects;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.candidate.dao.CandidateDao;
import com.cvesters.crowdchoice.candidate.dto.CandidateCreateDto;
import com.cvesters.crowdchoice.candidate.dto.CandidateDto;

public final class CandidateMapper {

	private CandidateMapper() {
	}

	public static List<Candidate> fromDao(final List<CandidateDao> dao) {
		Objects.requireNonNull(dao);

		return dao.stream().map(CandidateMapper::fromDao).toList();
	}

	public static Candidate fromDao(final CandidateDao dao) {
		Objects.requireNonNull(dao);

		return new Candidate(dao.getId(), dao.getName(), dao.getDescription());
	}

	public static void updateDao(final Candidate candidate,
			final CandidateDao dao) {
		Objects.requireNonNull(candidate);
		Objects.requireNonNull(dao);

		dao.setName(candidate.getName());
		dao.setDescription(candidate.getDescription());
	}

	public static Candidate fromDto(final CandidateCreateDto dto) {
		Objects.requireNonNull(dto);

		return new Candidate(dto.name(), dto.description());
	}

	public static List<CandidateDto> toDto(final List<Candidate> candidates) {
		Objects.requireNonNull(candidates);

		return candidates.stream().map(CandidateMapper::toDto).toList();
	}

	public static CandidateDto toDto(final Candidate candidate) {
		Objects.requireNonNull(candidate);

		return new CandidateDto(candidate.getId(), candidate.getName(),
				candidate.getDescription());
	}
}
