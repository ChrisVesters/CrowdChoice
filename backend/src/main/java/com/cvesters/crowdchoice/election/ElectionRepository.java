package com.cvesters.crowdchoice.election;

import org.springframework.data.repository.ListCrudRepository;

import com.cvesters.crowdchoice.election.dao.ElectionDao;

public interface ElectionRepository
		extends ListCrudRepository<ElectionDao, Long> {

}
