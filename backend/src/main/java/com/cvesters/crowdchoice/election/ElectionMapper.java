package com.cvesters.crowdchoice.election;

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

		return new ElectionInfo(dao.getId(), dao.getTopic());
	}

	public static ElectionDao createDao(final ElectionInfo election) {
		Objects.requireNonNull(election);

		return updateDao(election, new ElectionDao());
	}

	public static ElectionDao updateDao(final ElectionInfo election,
			final ElectionDao dao) {
		Objects.requireNonNull(election);
		Objects.requireNonNull(dao);

		dao.setTopic(election.getTopic());
		return dao;
	}

	public static ElectionInfo fromDto(final ElectionCreateDto dto) {
		Objects.requireNonNull(dto);

		return new ElectionInfo(dto.topic());
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

		return new ElectionInfoDto(id, topic);
	}
}
