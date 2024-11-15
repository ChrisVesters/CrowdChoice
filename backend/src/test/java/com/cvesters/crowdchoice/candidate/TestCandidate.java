package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.mockito.quality.Strictness;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.candidate.dao.CandidateDao;
import com.cvesters.crowdchoice.candidate.dto.CandidateDto;
import com.cvesters.crowdchoice.election.TestElection;

public record TestCandidate(long id, TestElection election, String name) {

	public static final TestCandidate MICRONAUT = new TestCandidate(1,
			TestElection.TOPICS, "Micronaut");
	public static final TestCandidate DOCKER = new TestCandidate(2,
			TestElection.TOPICS, "Docker");
	public static final TestCandidate LOMBOK = new TestCandidate(3,
			TestElection.TOPICS, "Lombok");

	public static final TestCandidate TRUMP = new TestCandidate(4,
			TestElection.FEDERAL_ELECTIONS_2024, "Trump");
	public static final TestCandidate BIDEN = new TestCandidate(5,
			TestElection.FEDERAL_ELECTIONS_2024, "Biden");

	public Candidate bdo() {
		return new Candidate(id, name);
	}

	public CandidateDao dao() {
		final var dao = mock(CandidateDao.class,
				withSettings().strictness(Strictness.LENIENT));

		when(dao.getId()).thenReturn(id);
		when(dao.getElectionId()).thenReturn(election.id());
		when(dao.getName()).thenReturn(name);

		return dao;
	}

	public String infoJson() {
		return """
				{
					"id": %d,
					"name": "%s"
				}
				""".formatted(id, name);
	}

	public void assertEquals(final CandidateDao actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getElectionId()).isEqualTo(election.id());
		assertThat(actual.getName()).isEqualTo(name);
	}

	public void assertEquals(final Candidate actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getName()).isEqualTo(name);
	}

	public void assertEquals(final CandidateDto actual) {
		assertThat(actual.id()).isEqualTo(id);
		assertThat(actual.name()).isEqualTo(name);
	}
}
