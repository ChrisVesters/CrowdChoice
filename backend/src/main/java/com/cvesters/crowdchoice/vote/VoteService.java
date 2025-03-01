package com.cvesters.crowdchoice.vote;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.crowdchoice.candidate.CandidateService;
import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteDao;

@Service
public class VoteService {

	private final VoteRepository voteRepository;

	private final CandidateService candidateService;

	public VoteService(final VoteRepository voteRepository,
			final CandidateService candidateService) {
		this.voteRepository = voteRepository;
		this.candidateService = candidateService;
	}

	public Vote create(final long electionId, final Vote request) {
		Objects.requireNonNull(request);
		candidateService.verifyExists(electionId, request.getCandidateId());

		final VoteDao dao = new VoteDao(request.getCastedOn(), request.getCandidateId());
		final VoteDao created = voteRepository.save(dao);

		return VoteMapper.fromDao(created);
	}
}
