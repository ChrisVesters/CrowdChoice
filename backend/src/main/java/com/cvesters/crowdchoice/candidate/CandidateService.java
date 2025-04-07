package com.cvesters.crowdchoice.candidate;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.candidate.dao.CandidateDao;
import com.cvesters.crowdchoice.election.ElectionService;
import com.cvesters.crowdchoice.exceptions.NotFoundException;

@Service
public class CandidateService {

	private final CandidateRepository candidateRepository;

	private final ElectionService electionService;

	public CandidateService(final CandidateRepository candidateRepository,
			final ElectionService electionService) {
		this.candidateRepository = candidateRepository;
		this.electionService = electionService;
	}

	public List<Candidate> findAll(final long electionId) {
		electionService.verifyExists(electionId);

		final List<CandidateDao> daos = candidateRepository
				.findByElectionId(electionId);

		return CandidateMapper.fromDao(daos);
	}

	public Candidate create(final long electionId, final Candidate candidate) {
		Objects.requireNonNull(candidate);
		electionService.verifyExists(electionId);

		final CandidateDao dao = new CandidateDao(electionId);
		CandidateMapper.updateDao(candidate, dao);
		final CandidateDao saved = candidateRepository.save(dao);

		return CandidateMapper.fromDao(saved);
	}

	public Candidate update(final long electionId, final Candidate candidate) {
		Objects.requireNonNull(candidate);
		electionService.verifyExists(electionId);

		final CandidateDao dao = candidateRepository
				.findByElectionIdAndId(electionId, candidate.getId())
				.orElseThrow(NotFoundException::new);

		CandidateMapper.updateDao(candidate, dao);
		final CandidateDao saved = candidateRepository.save(dao);

		return CandidateMapper.fromDao(saved);
	}

	public void delete(final long electionId, final long candidateId) {
		electionService.verifyExists(electionId);

		final CandidateDao candidate = candidateRepository
				.findByElectionIdAndId(electionId, candidateId)
				.orElseThrow(NotFoundException::new);

		candidateRepository.delete(candidate);
	}

	public void verifyExists(final long electionId, final long candidateId) {
		if (!candidateRepository.existsByElectionIdAndId(electionId, candidateId)) {
			throw new NotFoundException();
		}
	}
}
