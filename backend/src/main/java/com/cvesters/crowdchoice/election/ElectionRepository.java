package com.cvesters.crowdchoice.election;

import io.micronaut.data.repository.CrudRepository;

import com.cvesters.crowdchoice.config.Repository;
import com.cvesters.crowdchoice.election.dao.ElectionDao;

@Repository
// TODO: extend from GenericRepository
public interface ElectionRepository extends CrudRepository<ElectionDao, Long> {

}
