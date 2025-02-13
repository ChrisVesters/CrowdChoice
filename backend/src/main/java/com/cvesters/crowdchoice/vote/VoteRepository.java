package com.cvesters.crowdchoice.vote;

import org.springframework.data.repository.Repository;

import com.cvesters.crowdchoice.vote.dao.VoteDao;

public interface VoteRepository extends Repository<VoteDao, Long> {

	VoteDao save(final VoteDao vote);
}
