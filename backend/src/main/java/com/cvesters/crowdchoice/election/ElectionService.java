package com.cvesters.crowdchoice.election;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;

@Service
public class ElectionService {

	private ElectionRepository electionRepository;

	public ElectionService(final ElectionRepository electionRepository) {
		this.electionRepository = electionRepository;
	}

	public List<ElectionInfo> findAll() {
		final List<ElectionDao> daos = electionRepository.findAll();

		return ElectionMapper.fromDao(daos);
	}

	public ElectionInfo create(final ElectionInfo election) {
		Objects.requireNonNull(election);
		
		final ElectionDao dao = ElectionMapper.createDao(election);
		final ElectionDao created = electionRepository.save(dao);
		return ElectionMapper.fromDao(created);
	}
}
