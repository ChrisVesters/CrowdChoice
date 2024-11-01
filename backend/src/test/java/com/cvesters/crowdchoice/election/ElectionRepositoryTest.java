package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import com.cvesters.crowdchoice.election.dao.CandidateDao;
import com.cvesters.crowdchoice.election.dao.ElectionDao;

@MicronautTest
@Sql({ "sql/elections.sql" })
class ElectionRepositoryTest {

	@Inject
	private ElectionRepository electionRepository;

	@Test
	void findAll() {
		final List<ElectionDao> elections = electionRepository.findAll();

		assertThat(elections).hasSize(2);
	}

	@Test
	void findById() {
		final ElectionDao election = electionRepository.findById(1L).get();
		assertThat(election).isNotNull();
	}

	@Test
	void save() {
		final ElectionDao election = new ElectionDao();
		election.setTopic("Election");

		final CandidateDao candidate = new CandidateDao();
		candidate.setName("Candidate");

		final ElectionDao saved = electionRepository.save(election);

		assertThat(saved.getId()).isNotNull();

		final Optional<ElectionDao> found = electionRepository
				.findById(saved.getId());

		assertThat(found).hasValue(saved);

	}
}
