package com.cvesters.crowdchoice.candidate.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "candidates")
public class CandidateDao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.PRIVATE)
	private Long id;

	@Column(name = "election_id", nullable = false, updatable = false)
	@Setter(value = AccessLevel.PRIVATE)
	private long electionId;

	private String name;
	private String description;

	public CandidateDao(final long electionId) {
		this.electionId = electionId;
	}
}
