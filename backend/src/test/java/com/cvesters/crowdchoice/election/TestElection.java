package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.mockito.quality.Strictness;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;

public record TestElection(long id, String topic) {

	public static final TestElection TOPICS = new TestElection(1, "Topics");
	public static final TestElection FEDERAL_ELECTIONS_2024 = new TestElection(
			2, "Federal Elections 2024");

	public ElectionDao dao() {
		final ElectionDao dao = mock(
				withSettings().strictness(Strictness.LENIENT));

		when(dao.getId()).thenReturn(id);
		when(dao.getTopic()).thenReturn(topic);

		return dao;
	}

	public ElectionInfo info() {
		return new ElectionInfo(id, topic);
	}

	public String infoJson() {
		return """
				{
					"id": %d,
					"topic": "%s"
				}
				""".formatted(id, topic);
	}

	public void assertEquals(final ElectionInfo actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getTopic()).isEqualTo(topic);
	}

	public void assertEquals(final ElectionDao actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getTopic()).isEqualTo(topic);
	}

	public void assertEquals(final ElectionInfoDto actual) {
		assertThat(actual.id()).isEqualTo(id);
		assertThat(actual.topic()).isEqualTo(topic);
	}
}
