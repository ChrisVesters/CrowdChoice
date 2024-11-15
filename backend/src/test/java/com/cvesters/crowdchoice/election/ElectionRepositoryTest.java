package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.crowdchoice.PostgresContainerConfig;
import com.cvesters.crowdchoice.election.dao.ElectionDao;

@Sql({ "/db/elections.sql" })
@DataJpaTest
@Import(PostgresContainerConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ElectionRepositoryTest {

	@Autowired
	private ElectionRepository electionRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void findAll() {
		final List<ElectionDao> elections = electionRepository.findAll();

		assertThat(elections).hasSize(2)
				.anySatisfy(TestElection.TOPICS::assertEquals)
				.anySatisfy(TestElection.FEDERAL_ELECTIONS_2024::assertEquals);

	}

	@Test
	void findById() {
		final var election = TestElection.TOPICS;
		final Optional<ElectionDao> found = electionRepository
				.findById(election.id());

		assertThat(found).hasValueSatisfying(election::assertEquals);

	}

	@Test
	void findByIdNonExisting() {
		final Optional<ElectionDao> election = electionRepository
				.findById(Long.MAX_VALUE);

		assertThat(election).isEmpty();
	}

	@Test
	void existsById() {
		final var election = TestElection.TOPICS;
		assertThat(electionRepository.existsById(election.id())).isTrue();
	}

	@Test
	void existsByIdNonExisting() {
		assertThat(electionRepository.existsById(Long.MAX_VALUE)).isFalse();
	}

	@Test
	void save() {
		final String topic = "Election";

		final var election = new ElectionDao();
		election.setTopic(topic);

		final ElectionDao saved = electionRepository.save(election);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getTopic()).isEqualTo(topic);

		final var found = entityManager.find(ElectionDao.class, saved.getId());
		assertThat(found).isEqualTo(saved);
	}
}
