package com.cvesters.crowdchoice.election.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class ElectionInfo {

	private Long id;
	private String topic;

	public ElectionInfo(final long id, final String topic) {
		Objects.requireNonNull(topic);
		Validate.notBlank(topic);

		this.id = id;
		this.topic = topic;
	}

	public ElectionInfo(final String topic) {
		Objects.requireNonNull(topic);
		Validate.notBlank(topic);

		this.topic = topic;
	}
}
