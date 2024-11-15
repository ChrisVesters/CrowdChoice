package com.cvesters.crowdchoice.candidate.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

@Getter
public class Candidate {

	private Long id;
	private String name;

	private Candidate(final Long id, final String name) {
		Objects.requireNonNull(name);
		Validate.notBlank(name);

		this.id = id;
		this.name = name;
	}

	public Candidate(final long id, final String name) {
		this(Long.valueOf(id), name);
	}

	public Candidate(final String name) {
		this(null, name);
	}
}
