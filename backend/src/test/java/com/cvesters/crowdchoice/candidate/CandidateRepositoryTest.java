package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.crowdchoice.PostgresContainerConfig;
import com.cvesters.crowdchoice.candidate.dao.CandidateDao;

@Sql({ "/db/elections.sql" })
@DataJpaTest
@Import(PostgresContainerConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CandidateRepositoryTest {

	@Autowired
	private CandidateRepository candidateRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void findAll() {
		final List<CandidateDao> candidates = candidateRepository
				.findByElectionId(1);

		assertThat(candidates).hasSize(3)
				.anySatisfy(TestCandidate.MICRONAUT::assertEquals)
				.anySatisfy(TestCandidate.DOCKER::assertEquals)
				.anySatisfy(TestCandidate.LOMBOK::assertEquals);
	}

	@Test
	void existsByElectionIdAndName() {
		final var candidate = TestCandidate.TRUMP;
		assertThat(candidateRepository.existsByElectionIdAndName(
				candidate.election().id(), candidate.name())).isTrue();
	}

	@ParameterizedTest
	@CsvSource({ "1,Trump", "2,Harris", "3,Micronaut" })
	void existsByElectionIdAndNameNotFound(final long electionId,
			final String name) {
		assertThat(
				candidateRepository.existsByElectionIdAndName(electionId, name))
						.isFalse();
	}

	@Test
	void findById() {
		final var candidate = TestCandidate.TRUMP;
		final Optional<CandidateDao> found = candidateRepository
				.findById(candidate.id());

		assertThat(found).hasValueSatisfying(candidate::assertEquals);
	}

	@Test
	void findByIdNotFound() {
		final Optional<CandidateDao> candidate = candidateRepository
				.findById(Long.MAX_VALUE);

		assertThat(candidate).isEmpty();
	}

	@Test
	void save() {
		final long electionId = 1L;
		final String name = "Maven";

		final var candidate = new CandidateDao(electionId);
		candidate.setName(name);

		final CandidateDao saved = candidateRepository.save(candidate);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getElectionId()).isEqualTo(electionId);
		assertThat(saved.getName()).isEqualTo(name);

		final var found = entityManager.find(CandidateDao.class, saved.getId());
		assertThat(found).isEqualTo(saved);
	}
}
