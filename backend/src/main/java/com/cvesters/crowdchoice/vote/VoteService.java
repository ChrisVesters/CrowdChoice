package com.cvesters.crowdchoice.vote;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.crowdchoice.candidate.CandidateService;
import com.cvesters.crowdchoice.election.ElectionService;
import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;
import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteCountView;
import com.cvesters.crowdchoice.vote.dao.VoteDao;

@Service
public class VoteService {

	private final VoteRepository voteRepository;

	private final ElectionService electionService;
	private final CandidateService candidateService;

	public VoteService(final VoteRepository voteRepository,
			final ElectionService electionService,
			final CandidateService candidateService) {
		this.voteRepository = voteRepository;
		this.electionService = electionService;
		this.candidateService = candidateService;
	}

	public List<VoteCountView> getCounts(final long electionId) {
		electionService.verifyExists(electionId);

		return voteRepository.getCounts(electionId);
	}

	public Vote create(final long electionId, final Vote request) {
		Objects.requireNonNull(request);
		
		final ElectionInfo electionInfo = electionService.get(electionId);
		if (!electionInfo.isActive()) {
			throw new OperationNotAllowedException();
		}
		
		candidateService.verifyExists(electionId, request.getCandidateId());

		final VoteDao dao = new VoteDao(request.getCastedOn(),
				request.getCandidateId());
		final VoteDao created = voteRepository.save(dao);

		return VoteMapper.fromDao(created);
	}
}
