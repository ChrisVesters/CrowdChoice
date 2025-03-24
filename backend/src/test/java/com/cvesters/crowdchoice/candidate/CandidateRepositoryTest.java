package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
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
	void findByElectionIdAndId() {
		final var candidate = TestCandidate.TRUMP;
		final long electionId = candidate.election().id();
		final long candidateId = candidate.id();

		final Optional<CandidateDao> found = candidateRepository
				.findByElectionIdAndId(electionId, candidateId);

		assertThat(found).hasValueSatisfying(candidate::assertEquals);
	}

	@Test
	void findByElectionIdAndIdNotFound() {
		final var candidate = TestCandidate.TRUMP;
		final long electionId = candidate.election().id();

		final Optional<CandidateDao> found = candidateRepository
				.findByElectionIdAndId(electionId, Long.MAX_VALUE);

		assertThat(found).isEmpty();
	}

	@Test
	void save() {
		final long electionId = 1L;
		final String name = "Maven";
		final String description = "Java build tool";

		final var candidate = new CandidateDao(electionId);
		candidate.setName(name);
		candidate.setDescription(description);

		final CandidateDao saved = candidateRepository.save(candidate);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getElectionId()).isEqualTo(electionId);
		assertThat(saved.getName()).isEqualTo(name);
		assertThat(saved.getDescription()).isEqualTo(description);

		final var found = entityManager.find(CandidateDao.class, saved.getId());
		assertThat(found).isEqualTo(saved);
	}

	@Test
	void saveForNonExistingElection() {
		final long electionId = Long.MAX_VALUE;
		final String name = "Maven";
		final String description = "Java build tool";

		final var candidate = new CandidateDao(electionId);
		candidate.setName(name);
		candidate.setDescription(description);

		assertThatThrownBy(() -> candidateRepository.save(candidate))
				.isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	void delete() {
		final long candidateId = 1L;
		final CandidateDao dao = entityManager.find(CandidateDao.class,
				candidateId);

		candidateRepository.delete(dao);

		assertThat(entityManager.contains(dao)).isFalse();
		assertThat(entityManager.find(CandidateDao.class, candidateId))
				.isNull();
	}

	@Test
	void existsById() {
		final long electionId = 1L;
		final long candidateId = 1L;

		assertThat(candidateRepository.existsByElectionIdAndId(electionId,
				candidateId)).isTrue();
	}

	@Test
	void existsByIdNonExistingElection() {
		final long electionId = Long.MAX_VALUE;
		final long candidateId = 1L;

		assertThat(candidateRepository.existsByElectionIdAndId(electionId,
				candidateId)).isFalse();
	}

	@Test
	void existsByIdNonExistingCandidate() {
		final long electionId = 1L;
		final long candidateId = Long.MAX_VALUE;

		assertThat(candidateRepository.existsByElectionIdAndId(electionId,
				candidateId)).isFalse();
	}
}
