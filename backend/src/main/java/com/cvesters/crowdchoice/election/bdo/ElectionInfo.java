package com.cvesters.crowdchoice.election.bdo;

import java.time.OffsetDateTime;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class ElectionInfo {

	private final Long id;
	private String topic;
	private String description;
	private OffsetDateTime startedOn;
	private OffsetDateTime endedOn;

	private ElectionInfo(final Long id, final String topic,
			final String description, final OffsetDateTime startedOn,
			final OffsetDateTime endedOn) {
		Objects.requireNonNull(topic);
		Validate.notBlank(topic);
		Objects.requireNonNull(description);
		Validate.isTrue(endedOn == null
				|| (startedOn != null && endedOn.isAfter(startedOn)));

		this.id = id;
		this.topic = topic.trim();
		this.description = description.trim();
		this.startedOn = startedOn;
		this.endedOn = endedOn;
	}

	public ElectionInfo(final long id, final String topic,
			final String description, final OffsetDateTime startedOn,
			final OffsetDateTime endedOn) {
		this(Long.valueOf(id), topic, description, startedOn, endedOn);
	}

	public ElectionInfo(final String topic, final String description,
			final OffsetDateTime startedOn, final OffsetDateTime endedOn) {
		this(null, topic, description, startedOn, endedOn);
	}

	public boolean isEditable() {
		return !hasStarted();
	}

	public boolean isActive() {
		return hasStarted() && !hasEnded();
	}

	private boolean hasStarted() {
		return startedOn != null && startedOn.isBefore(OffsetDateTime.now());
	}

	private boolean hasEnded() {
		return endedOn != null && endedOn.isBefore(OffsetDateTime.now());
	}
}
