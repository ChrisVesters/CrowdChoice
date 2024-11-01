package com.cvesters.crowdchoice.election.dao;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: records don't make sense because of id.
@Getter
@Setter
@NoArgsConstructor
@MappedEntity("elections")
public class ElectionDao {

	@Id
	@GeneratedValue(GeneratedValue.Type.IDENTITY)
	private Long id;

	private String topic;

}
