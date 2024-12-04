package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

	@Nested
	class ExistsByElectionIdAndName {

		@Test
		void found() {
			final var candidate = TestCandidate.TRUMP;
			assertThat(candidateRepository.existsByElectionIdAndName(
					candidate.election().id(), candidate.name())).isTrue();
		}

		@ParameterizedTest
		@CsvSource({ "1,Trump", "2,Harris", "3,Micronaut" })
		void notFound(final long electionId, final String name) {
			assertThat(candidateRepository.existsByElectionIdAndName(electionId,
					name)).isFalse();
		}
	}

	@Nested
	class FindByElectionIdAndId {

		@Test
		void found() {
			final var candidate = TestCandidate.TRUMP;
			final long electionId = candidate.election().id();
			final long candidateId = candidate.id();

			final Optional<CandidateDao> found = candidateRepository
					.findByElectionIdAndId(electionId, candidateId);

			assertThat(found).hasValueSatisfying(candidate::assertEquals);
		}

		@Test
		void notFound() {
			final var candidate = TestCandidate.TRUMP;
			final long electionId = candidate.election().id();

			final Optional<CandidateDao> found = candidateRepository
					.findByElectionIdAndId(electionId, Long.MAX_VALUE);

			assertThat(found).isEmpty();
		}
	}

	@Nested
	class Save {

		private static final long ELECTION_ID = 1L;
		private static final String NAME = "Maven";
		private static final String DESCRIPTION = "Java build tool";

		@Test
		void success() {
			final var candidate = new CandidateDao(ELECTION_ID);
			candidate.setName(NAME);
			candidate.setDescription(DESCRIPTION);

			final CandidateDao saved = candidateRepository.save(candidate);

			assertThat(saved.getId()).isNotNull();
			assertThat(saved.getElectionId()).isEqualTo(ELECTION_ID);
			assertThat(saved.getName()).isEqualTo(NAME);
			assertThat(saved.getDescription()).isEqualTo(DESCRIPTION);

			final var found = entityManager.find(CandidateDao.class,
					saved.getId());
			assertThat(found).isEqualTo(saved);
		}

		@Test
		void nameNull() {
			final var candidate = new CandidateDao(ELECTION_ID);
			candidate.setDescription(DESCRIPTION);

			assertThatThrownBy(() -> candidateRepository.save(candidate))
					.isInstanceOf(DataIntegrityViolationException.class);
		}

		@Test
		void descriptionNull() {
			final var candidate = new CandidateDao(ELECTION_ID);
			candidate.setName(NAME);

			assertThatThrownBy(() -> candidateRepository.save(candidate))
					.isInstanceOf(DataIntegrityViolationException.class);
		}
	}

	@Nested
	class Delete {

		@Test
		void found() {
			final long candidateId = 1L;
			final CandidateDao dao = entityManager.find(CandidateDao.class,
					candidateId);

			candidateRepository.delete(dao);

			assertThat(entityManager.contains(dao)).isFalse();
			assertThat(entityManager.find(CandidateDao.class, candidateId))
					.isNull();
		}
	}
}
