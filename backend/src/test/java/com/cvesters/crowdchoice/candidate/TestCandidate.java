package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.mockito.quality.Strictness;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.candidate.dao.CandidateDao;
import com.cvesters.crowdchoice.candidate.dto.CandidateDto;
import com.cvesters.crowdchoice.election.TestElection;

public record TestCandidate(long id, TestElection election, String name,
		String description) {

	public static final TestCandidate MICRONAUT = new TestCandidate(1,
			TestElection.TOPICS, "Micronaut", "Getting rid of reflection");
	public static final TestCandidate DOCKER = new TestCandidate(2,
			TestElection.TOPICS, "Docker", "Containers");
	public static final TestCandidate LOMBOK = new TestCandidate(3,
			TestElection.TOPICS, "Lombok", "No more boilerplate");

	public static final TestCandidate TRUMP = new TestCandidate(4,
			TestElection.FEDERAL_ELECTIONS_2024, "Trump", "Republican");
	public static final TestCandidate BIDEN = new TestCandidate(5,
			TestElection.FEDERAL_ELECTIONS_2024, "Biden", "Democrat");

	public static final Stream<Arguments> candidates() {
		return Stream.of(Arguments.of(MICRONAUT), Arguments.of(DOCKER),
				Arguments.of(LOMBOK));
	}

	public Candidate bdo() {
		return new Candidate(id, name, description);
	}

	public CandidateDao dao() {
		final var dao = mock(CandidateDao.class,
				withSettings().strictness(Strictness.LENIENT));

		when(dao.getId()).thenReturn(id);
		when(dao.getElectionId()).thenReturn(election.id());
		when(dao.getName()).thenReturn(name);
		when(dao.getDescription()).thenReturn(description);

		return dao;
	}

	public void assertEquals(final CandidateDao actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getElectionId()).isEqualTo(election.id());
		assertThat(actual.getName()).isEqualTo(name);
		assertThat(actual.getDescription()).isEqualTo(description);
	}

	public void assertEquals(final Candidate actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getName()).isEqualTo(name);
		assertThat(actual.getDescription()).isEqualTo(description);
	}

	public void assertEquals(final CandidateDto actual) {
		assertThat(actual.id()).isEqualTo(id);
		assertThat(actual.name()).isEqualTo(name);
		assertThat(actual.description()).isEqualTo(description);
	}
}
