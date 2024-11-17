package com.cvesters.crowdchoice.candidate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.cvesters.crowdchoice.candidate.dao.CandidateDao;

public interface CandidateRepository extends Repository<CandidateDao, Long> {

	List<CandidateDao> findByElectionId(final long electionId);

	boolean existsByElectionIdAndName(final long electionId,
			final String name);

	Optional<CandidateDao> findByElectionIdAndId(final long electionId,
			final long id);

	CandidateDao save(final CandidateDao candidate);

	void delete(final CandidateDao candidate);
}
