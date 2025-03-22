package com.cvesters.crowdchoice.election.bdo;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class ElectionInfo {

	private final Long id;
	private String topic;
	private String description;
	private ZonedDateTime startedOn;
	private ZonedDateTime endedOn;

	private ElectionInfo(final Long id, final String topic,
			final String description, final ZonedDateTime startedOn,
			final ZonedDateTime endedOn) {
		Objects.requireNonNull(topic);
		Validate.notBlank(topic);
		Objects.requireNonNull(description);
		// endedOn.isAfter(startedOn)

		this.id = id;
		this.topic = topic.trim();
		this.description = description.trim();
		this.startedOn = startedOn;
		this.endedOn = endedOn;
	}

	public ElectionInfo(final long id, final String topic,
			final String description, final ZonedDateTime startedOn,
			final ZonedDateTime endedOn) {
		this(Long.valueOf(id), topic, description, startedOn, endedOn);
	}

	public ElectionInfo(final String topic, final String description,
			final ZonedDateTime startedOn, final ZonedDateTime endedOn) {
		this(null, topic, description, startedOn, endedOn);
	}
}
