package com.cvesters.crowdchoice.election.action.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "action")
@JsonSubTypes({
		@JsonSubTypes.Type(value = StartElectionDto.class, name = "start"),
		@JsonSubTypes.Type(value = EndElectionDto.class, name = "end"),
		@JsonSubTypes.Type(value = ScheduleElectionDto.class, name = "schedule") })
public sealed interface ElectionActionDto
		permits StartElectionDto, EndElectionDto, ScheduleElectionDto {

}
