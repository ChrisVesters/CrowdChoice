package com.cvesters.crowdchoice.candidate.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class Candidate {

	private Long id;
	private String name;
	private String description;

	private Candidate(final Long id, final String name, final String description) {
		Objects.requireNonNull(name);
		Validate.notBlank(name);
		Objects.requireNonNull(description);

		this.id = id;
		this.name = name.trim();
		this.description = description.trim();
	}

	public Candidate(final long id, final String name, final String description) {
		this(Long.valueOf(id), name, description);
	}

	public Candidate(final String name, final String description) {
		this(null, name, description);
	}
}
