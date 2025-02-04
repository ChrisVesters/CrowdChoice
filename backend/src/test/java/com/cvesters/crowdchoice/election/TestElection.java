package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.mockito.quality.Strictness;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;

public record TestElection(long id, String topic, String description) {

	public static final TestElection TOPICS = new TestElection(1, "Topics", "");
	public static final TestElection FEDERAL_ELECTIONS_2024 = new TestElection(
			2, "Federal Elections 2024", "Choose a new president.");

	public ElectionDao dao() {
		final ElectionDao dao = mock(
				withSettings().strictness(Strictness.LENIENT));

		when(dao.getId()).thenReturn(id);
		when(dao.getTopic()).thenReturn(topic);
		when(dao.getDescription()).thenReturn(description);

		return dao;
	}

	public ElectionInfo info() {
		return new ElectionInfo(id, topic, description);
	}

	public String infoJson() {
		return """
				{
					"id": %d,
					"topic": "%s",
					"description": "%s"
				}
				""".formatted(id, topic, description);
	}

	public void assertEquals(final ElectionInfo actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getTopic()).isEqualTo(topic);
		assertThat(actual.getDescription()).isEqualTo(description);
	}

	public void assertEquals(final ElectionDao actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getTopic()).isEqualTo(topic);
		assertThat(actual.getDescription()).isEqualTo(description);
	}

	public void assertEquals(final ElectionInfoDto actual) {
		assertThat(actual.id()).isEqualTo(id);
		assertThat(actual.topic()).isEqualTo(topic);
		assertThat(actual.description()).isEqualTo(description);
	}
}
