package com.cvesters.crowdchoice.election;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.exceptions.NotFoundException;
import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;

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

	public ElectionInfo get(final long electionId) {
		final ElectionDao dao = electionRepository.findById(electionId)
				.orElseThrow(NotFoundException::new);

		return ElectionMapper.fromDao(dao);
	}

	public void verifyExists(final long electionId) {
		if (!electionRepository.existsById(electionId)) {
			throw new NotFoundException();
		}
	}

	public ElectionInfo create(final ElectionInfo election) {
		Objects.requireNonNull(election);

		final var dao = new ElectionDao();
		ElectionMapper.updateDao(election, dao);
		final ElectionDao created = electionRepository.save(dao);
		return ElectionMapper.fromDao(created);
	}

	public ElectionInfo update(final ElectionInfo election) {
		Objects.requireNonNull(election);

		final ElectionDao dao = electionRepository.findById(election.getId())
				.orElseThrow(NotFoundException::new);

		final ElectionInfo currentInfo = ElectionMapper.fromDao(dao);
		if (!currentInfo.isEditable()) {
			throw new OperationNotAllowedException();
		}

		ElectionMapper.updateDao(election, dao);
		final ElectionDao updated = electionRepository.save(dao);
		return ElectionMapper.fromDao(updated);
	}

	public void delete(final long electionId) {
		final ElectionDao dao = electionRepository.findById(electionId)
				.orElseThrow(NotFoundException::new);

		electionRepository.delete(dao);
	}
}
