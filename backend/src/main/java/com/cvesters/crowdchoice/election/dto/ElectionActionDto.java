package com.cvesters.crowdchoice.election.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "action")
@JsonSubTypes({
	@JsonSubTypes.Type(value = StartElectionDto.class, name = "start"),
	@JsonSubTypes.Type(value = ScheduleStartElectionDto.class, name = "schedule_start"),
	@JsonSubTypes.Type(value = EndElectionDto.class, name = "end")
})
public sealed interface ElectionActionDto permits StartElectionDto, ScheduleStartElectionDto, EndElectionDto {
	
}
