package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.mockito.quality.Strictness;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;

public record TestElection(long id, String topic, String description,
		OffsetDateTime startedOn, OffsetDateTime endedOn) {

	public static final TestElection TOPICS = new TestElection(1, "Topics", "",
			null, null);
	public static final TestElection FEDERAL_ELECTIONS_2024 = new TestElection(
			2, "Federal Elections 2024", "Choose a new president.",
			OffsetDateTime.of(2023, 10, 1, 8, 0, 0, 0, ZoneOffset.UTC),
			OffsetDateTime.of(2023, 10, 1, 18, 0, 0, 0, ZoneOffset.UTC));
	public static final TestElection COLOURS = new TestElection(2, "Colours",
			"", OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
			null);

	public static final List<TestElection> ALL = List.of(TOPICS,
			FEDERAL_ELECTIONS_2024);

	public static final Stream<Arguments> elections() {
		return ALL.stream().map(Arguments::of);
	}

	public ElectionDao dao() {
		final ElectionDao dao = mock(
				withSettings().strictness(Strictness.LENIENT));

		when(dao.getId()).thenReturn(id);
		when(dao.getTopic()).thenReturn(topic);
		when(dao.getDescription()).thenReturn(description);
		when(dao.getStartedOn()).thenReturn(startedOn);
		when(dao.getEndedOn()).thenReturn(endedOn);

		return dao;
	}

	public ElectionInfo info() {
		return new ElectionInfo(id, topic, description, startedOn, endedOn);
	}

	public void assertEquals(final ElectionInfo actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getTopic()).isEqualTo(topic);
		assertThat(actual.getDescription()).isEqualTo(description);
		assertThat(actual.getStartedOn()).isEqualTo(startedOn);
		assertThat(actual.getEndedOn()).isEqualTo(endedOn);
	}

	public void assertEquals(final ElectionDao actual) {
		assertThat(actual.getId()).isEqualTo(id);
		assertThat(actual.getTopic()).isEqualTo(topic);
		assertThat(actual.getDescription()).isEqualTo(description);
		assertThat(actual.getStartedOn()).isEqualTo(startedOn);
		assertThat(actual.getEndedOn()).isEqualTo(endedOn);
	}

	public void assertEquals(final ElectionInfoDto actual) {
		assertThat(actual.id()).isEqualTo(id);
		assertThat(actual.topic()).isEqualTo(topic);
		assertThat(actual.description()).isEqualTo(description);
		assertThat(actual.startedOn()).isEqualTo(startedOn);
		assertThat(actual.endedOn()).isEqualTo(endedOn);
	}
}
