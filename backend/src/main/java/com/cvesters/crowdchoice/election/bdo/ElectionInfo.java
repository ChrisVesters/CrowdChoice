package com.cvesters.crowdchoice.election.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class ElectionInfo {

	private Long id;
	private String topic;
	private String description;

	private ElectionInfo(final Long id, final String topic,
			final String description) {
		Objects.requireNonNull(topic);
		Validate.notBlank(topic);
		Objects.requireNonNull(description);

		this.id = id;
		this.topic = topic.trim();
		this.description = description.trim();
	}

	public ElectionInfo(final long id, final String topic,
			final String description) {
		this(Long.valueOf(id), topic, description);
	}

	public ElectionInfo(final String topic, final String description) {
		this(null, topic, description);
	}
}
