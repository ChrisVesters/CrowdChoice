package com.cvesters.crowdchoice.vote;

import java.util.List;

import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.Repository;

import com.cvesters.crowdchoice.vote.dao.VoteCountView;
import com.cvesters.crowdchoice.vote.dao.VoteDao;

public interface VoteRepository extends Repository<VoteDao, Long> {

	@NativeQuery(value = """
			SELECT c.id as candidateId, COUNT(v.id) AS voteCount
			FROM votes v
			JOIN candidates c ON c.id = v.candidate_id
			WHERE c.election_id = :electionId
			GROUP BY c.id
			""")
	List<VoteCountView> getCounts(final long electionId);

	VoteDao save(final VoteDao vote);
}
