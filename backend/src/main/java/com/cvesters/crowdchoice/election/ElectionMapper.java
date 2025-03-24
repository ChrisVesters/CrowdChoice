package com.cvesters.crowdchoice.election;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dao.ElectionDao;
import com.cvesters.crowdchoice.election.dto.ElectionCreateDto;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;

public final class ElectionMapper {

	private ElectionMapper() {
	}

	public static List<ElectionInfo> fromDao(final List<ElectionDao> dao) {
		Objects.requireNonNull(dao);

		return dao.stream().map(ElectionMapper::fromDao).toList();
	}

	public static ElectionInfo fromDao(final ElectionDao dao) {
		Objects.requireNonNull(dao);

		return new ElectionInfo(dao.getId(), dao.getTopic(),
				dao.getDescription(), dao.getStartedOn(), dao.getEndedOn());
	}

	public static void updateDao(final ElectionInfo election,
			final ElectionDao dao) {
		Objects.requireNonNull(election);
		Objects.requireNonNull(dao);

		dao.setTopic(election.getTopic());
		dao.setDescription(election.getDescription());
		dao.setStartedOn(election.getStartedOn());
		dao.setEndedOn(election.getEndedOn());
	}

	public static ElectionInfo fromDto(final ElectionCreateDto dto) {
		Objects.requireNonNull(dto);

		return new ElectionInfo(dto.topic(), dto.description(), dto.startedOn(),
				dto.endedOn());
	}

	public static List<ElectionInfoDto> toDto(
			final List<ElectionInfo> elections) {
		Objects.requireNonNull(elections);

		return elections.stream().map(ElectionMapper::toDto).toList();
	}

	public static ElectionInfoDto toDto(final ElectionInfo election) {
		Objects.requireNonNull(election);

		final long id = election.getId();
		final String topic = election.getTopic();
		final String description = election.getDescription();
		final OffsetDateTime startedOn = election.getStartedOn();
		final OffsetDateTime endedOn = election.getEndedOn();

		return new ElectionInfoDto(id, topic, description, startedOn, endedOn);
	}
}
