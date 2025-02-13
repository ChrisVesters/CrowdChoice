package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.ZonedDateTime;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import com.cvesters.crowdchoice.PostgresContainerConfig;
import com.cvesters.crowdchoice.vote.dao.VoteDao;

@Sql({ "/db/elections.sql", "/db/votes.sql" })
@DataJpaTest
@Import(PostgresContainerConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class VoteRepositoryTest {

	@Autowired
	private VoteRepository voteRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Nested
	class Save {

		private static final long CANDIDATE_ID = 5L;
		private static final ZonedDateTime CASTED_ON = ZonedDateTime.now();

		@Test
		void success() {
			final var vote = new VoteDao(CASTED_ON, CANDIDATE_ID);

			final VoteDao saved = voteRepository.save(vote);

			assertThat(saved.getId()).isNotNull();
			assertThat(saved.getCandidateId()).isEqualTo(CANDIDATE_ID);
			assertThat(saved.getCastedOn()).isEqualTo(CASTED_ON);
		}

		@Test
		void candidateDoesNotExist() {
			final long candidateId = Long.MAX_VALUE;
			final var vote = new VoteDao(CASTED_ON, candidateId);

			assertThatThrownBy(() -> voteRepository.save(vote))
					.isInstanceOf(DataIntegrityViolationException.class);
		}

		@Test
		void castedOnNull() {
			final var vote = new VoteDao(null, CANDIDATE_ID);

			assertThatThrownBy(() -> voteRepository.save(vote))
					.isInstanceOf(DataIntegrityViolationException.class);
		}
	}
}
