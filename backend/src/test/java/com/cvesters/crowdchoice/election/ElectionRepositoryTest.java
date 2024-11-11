package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;
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

		final ElectionDao saved = electionRepository.save(election);

		assertThat(saved.getId()).isNotNull();

		final Optional<ElectionDao> found = electionRepository
				.findById(saved.getId());

		assertThat(found).hasValueSatisfying(value -> {
			assertThat(value.getId()).isEqualTo(saved.getId());
			assertThat(value.getTopic()).isEqualTo(election.getTopic());
		});
	}
}
