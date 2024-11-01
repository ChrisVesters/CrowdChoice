package com.cvesters.crowdchoice.election.dao;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedEntity("candidates")
public class CandidateDao {

	@Id
	@GeneratedValue(GeneratedValue.Type.IDENTITY)
	private Long id;

	@MappedProperty("election_id")
	private long electionId;

	private String name;

}
