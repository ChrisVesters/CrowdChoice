package com.cvesters.crowdchoice.vote.dao;

import java.time.ZonedDateTime;

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
@Entity(name = "votes")
public class VoteDao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.PRIVATE)
	private Long id;

	@Column(name = "casted_on", nullable = false, updatable = false)
	@Setter(value = AccessLevel.PRIVATE)
	private ZonedDateTime castedOn;

	@Column(name = "candidate_id", nullable = false, updatable = false)
	@Setter(value = AccessLevel.PRIVATE)
	private long candidateId;

	public VoteDao(final ZonedDateTime castedOn, final long candidateId) {
		this.castedOn = castedOn;
		this.candidateId = candidateId;
	}
}
