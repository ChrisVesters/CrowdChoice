package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.cvesters.crowdchoice.election.action.ElectionActionMapper;
import com.cvesters.crowdchoice.election.action.bdo.ElectionAction;
import com.cvesters.crowdchoice.election.action.dto.EndElectionDto;
import com.cvesters.crowdchoice.election.action.dto.ScheduleElectionDto;
import com.cvesters.crowdchoice.election.action.dto.StartElectionDto;
import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.exceptions.NotFoundException;
import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;

@WebMvcTest(ElectionController.class)
class ElectionControllerTest {

	private static final String BASE_URL = "/api/elections";

	@Autowired
	protected MockMvc mockMvc;

	@MockitoBean
	private ElectionService electionService;

	@Nested
	class GetAll {

		@Test
		void empty() throws Exception {
			when(electionService.findAll()).thenReturn(Collections.emptyList());

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json("[]"));
		}

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void single(final TestElection election) throws Exception {
			final List<TestElection> elections = List.of(election);

			final List<ElectionInfo> infos = elections.stream()
					.map(TestElection::info)
					.toList();
			when(electionService.findAll()).thenReturn(infos);

			final String expectedBody = elections.stream()
					.map(ElectionControllerTest::infoJson)
					.collect(Collectors.joining(",", "[", "]"));

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(expectedBody));
		}

		@Test
		void multiple() throws Exception {
			final List<TestElection> elections = List.of(TestElection.TOPICS,
					TestElection.FEDERAL_ELECTIONS_2024);

			final List<ElectionInfo> infos = elections.stream()
					.map(TestElection::info)
					.toList();
			when(electionService.findAll()).thenReturn(infos);

			final String expectedBody = elections.stream()
					.map(ElectionControllerTest::infoJson)
					.collect(Collectors.joining(",", "[", "]"));

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(expectedBody));
		}
	}

	@Nested
	class Get {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void success(final TestElection election) throws Exception {
			when(electionService.get(election.id()))
					.thenReturn(election.info());

			final String endpoint = BASE_URL + "/" + election.id();
			final RequestBuilder request = get(endpoint);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(infoJson(election)));
		}

		@Test
		void notFound() throws Exception {
			final long electionId = 125L;
			when(electionService.get(electionId))
					.thenThrow(new NotFoundException());

			final String endpoint = BASE_URL + "/" + electionId;
			final RequestBuilder request = get(endpoint);

			mockMvc.perform(request).andExpect(status().isNotFound());
		}
	}

	@Nested
	class Create {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void success(final TestElection election) throws Exception {
			final String requestBody = requestJson(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());

			when(electionService.create(argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getTopic()).isEqualTo(election.topic());
				assertThat(request.getDescription())
						.isEqualTo(election.description());
				assertThat(request.getStartedOn())
						.isEqualTo(election.startedOn());
				assertThat(request.getEndedOn()).isEqualTo(election.endedOn());
				return true;
			}))).thenReturn(election.info());

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andExpect(content().json(infoJson(election)));
		}

		@Test
		void withoutBody() throws Exception {
			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void invalid() throws Exception {
			final String requestBody = requestJson("", "", null, null);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void error() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = requestJson(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());

			when(electionService.create(any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}

		private String requestJson(final String topic, final String description,
				final OffsetDateTime startedOn, OffsetDateTime endedOn) {
			return """
					{
						"topic": "%s",
						"description": "%s",
						"startedOn": %s,
						"endedOn": %s
					}
					""".formatted(topic, description, map(startedOn),
					map(endedOn));
		}
	}

	@Nested
	class Update {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.election.TestElection#elections")
		void success(final TestElection election) throws Exception {
			final String requestBody = updateJson(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());

			when(electionService.update(argThat(request -> {
				assertThat(request.getId()).isEqualTo(election.id());
				assertThat(request.getTopic()).isEqualTo(election.topic());
				assertThat(request.getDescription())
						.isEqualTo(election.description());
				assertThat(request.getStartedOn())
						.isEqualTo(election.startedOn());
				assertThat(request.getEndedOn()).isEqualTo(election.endedOn());
				return true;
			}))).thenReturn(election.info());

			final RequestBuilder request = put(BASE_URL + "/" + election.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(infoJson(election)));
		}

		@Test
		void withoutBody() throws Exception {
			final long electionId = 234L;
			final RequestBuilder request = put(BASE_URL + "/" + electionId)
					.contentType(MediaType.APPLICATION_JSON_VALUE);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void invalid() throws Exception {
			final String requestBody = updateJson("", "", null, null);

			final long electionId = 234L;
			final RequestBuilder request = put(BASE_URL + "/" + electionId)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void notFound() throws Exception {
			final TestElection election = TestElection.TOPICS;

			final String requestBody = updateJson(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());

			doThrow(new NotFoundException()).when(electionService)
					.update(any());

			final RequestBuilder request = put(BASE_URL + "/" + election.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isNotFound());
		}

		@Test
		void error() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = updateJson(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());

			when(electionService.update(any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = put(BASE_URL + "/" + election.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}

		@Test
		void operationNotAllowed() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = updateJson(election.topic(),
					election.description(), election.startedOn(),
					election.endedOn());

			when(electionService.update(any()))
					.thenThrow(OperationNotAllowedException.class);

			final RequestBuilder request = put(BASE_URL + "/" + election.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isMethodNotAllowed());
		}

		private String updateJson(final String topic, final String description,
				final OffsetDateTime startedOn, OffsetDateTime endedOn) {
			return """
					{
						"topic": "%s",
						"description": "%s",
						"startedOn": %s,
						"endedOn": %s
					}
					""".formatted(topic, description, map(startedOn),
					map(endedOn));
		}
	}

	@Nested
	class Patch {

		@Test
		void start() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = """
					{
						"action": "start"
					}
					""";

			try (final MockedStatic<ElectionActionMapper> actionMapper = Mockito
					.mockStatic(ElectionActionMapper.class);
					final MockedStatic<ElectionMapper> electionMapper = Mockito
							.mockStatic(ElectionMapper.class)) {
				final ElectionAction action = mock();
				final ElectionInfo updated = mock();

				actionMapper.when(() -> ElectionActionMapper
						.fromDto(eq(election.id()), argThat(dto -> {
							assertThat(dto)
									.isInstanceOf(StartElectionDto.class);
							return true;
						}))).thenReturn(action);

				when(electionService.apply(action)).thenReturn(updated);

				electionMapper.when(() -> ElectionMapper.toDto(updated))
						.thenReturn(election.dto());

				final RequestBuilder request = patch(
						BASE_URL + "/" + election.id())
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(requestBody);

				mockMvc.perform(request)
						.andExpect(status().isOk())
						.andExpect(content().json(infoJson(election)));
			}
		}

		@Test
		void end() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = """
					{
						"action": "end"
					}
					""";

			try (final MockedStatic<ElectionActionMapper> actionMapper = Mockito
					.mockStatic(ElectionActionMapper.class);
					final MockedStatic<ElectionMapper> electionMapper = Mockito
							.mockStatic(ElectionMapper.class)) {
				final ElectionAction action = mock();
				final ElectionInfo updated = mock();

				actionMapper.when(() -> ElectionActionMapper
						.fromDto(eq(election.id()), argThat(dto -> {
							assertThat(dto)
									.isInstanceOf(EndElectionDto.class);
							return true;
						}))).thenReturn(action);

				when(electionService.apply(action)).thenReturn(updated);

				electionMapper.when(() -> ElectionMapper.toDto(updated))
						.thenReturn(election.dto());

				final RequestBuilder request = patch(
						BASE_URL + "/" + election.id())
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(requestBody);

				mockMvc.perform(request)
						.andExpect(status().isOk())
						.andExpect(content().json(infoJson(election)));
			}
		}

		@Test
		void schedule() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final OffsetDateTime startOn = OffsetDateTime.now().plusDays(1);
			final OffsetDateTime endOn = startOn.plusDays(1);

			final String requestBody = """
					{
						"action": "schedule",
						"startOn": %s,
						"endOn": %s
					}
					""".formatted(map(startOn), map(endOn));

			try (final MockedStatic<ElectionActionMapper> actionMapper = Mockito
					.mockStatic(ElectionActionMapper.class);
					final MockedStatic<ElectionMapper> electionMapper = Mockito
							.mockStatic(ElectionMapper.class)) {
				final ElectionAction action = mock();
				final ElectionInfo updated = mock();

				actionMapper.when(() -> ElectionActionMapper
						.fromDto(eq(election.id()), argThat(dto -> {
							assertThat(dto)
									.isInstanceOf(ScheduleElectionDto.class);
							final ScheduleElectionDto schedule = (ScheduleElectionDto) dto;
							assertThat(schedule.startOn()).isEqualTo(startOn);
							assertThat(schedule.endOn()).isEqualTo(endOn);
							return true;
						}))).thenReturn(action);

				when(electionService.apply(action)).thenReturn(updated);

				electionMapper.when(() -> ElectionMapper.toDto(updated))
						.thenReturn(election.dto());

				final RequestBuilder request = patch(
						BASE_URL + "/" + election.id())
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(requestBody);

				mockMvc.perform(request)
						.andExpect(status().isOk())
						.andExpect(content().json(infoJson(election)));
			}
		}

		@Test
		void withoutBody() throws Exception {
			final long electionId = 234L;
			final RequestBuilder request = patch(BASE_URL + "/" + electionId)
					.contentType(MediaType.APPLICATION_JSON_VALUE);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void invalid() throws Exception {
			final String requestBody = patchJson("tank");

			final long electionId = 234L;
			final RequestBuilder request = patch(BASE_URL + "/" + electionId)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void notFound() throws Exception {
			final TestElection election = TestElection.TOPICS;

			final String requestBody = patchJson("start");

			doThrow(new NotFoundException()).when(electionService).apply(any());

			final RequestBuilder request = patch(BASE_URL + "/" + election.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isNotFound());
		}

		@Test
		void error() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = patchJson("start");

			when(electionService.apply(any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = patch(BASE_URL + "/" + election.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}

		@Test
		void operationNotAllowed() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = patchJson("start");

			when(electionService.apply(any()))
					.thenThrow(OperationNotAllowedException.class);

			final RequestBuilder request = patch(BASE_URL + "/" + election.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isMethodNotAllowed());
		}

		private String patchJson(final String action) {
			return """
					{
						"action": "%s"
					}
					""".formatted(action);
		}
	}

	@Nested
	class Delete {

		private static final TestElection ELECTION = TestElection.TOPICS;
		private static final long ELECTION_ID = ELECTION.id();

		@Test
		void success() throws Exception {

			final RequestBuilder request = delete(
					BASE_URL + "/" + ELECTION.id());

			mockMvc.perform(request).andExpect(status().isNoContent());

			verify(electionService).delete(ELECTION_ID);
		}

		@Test
		void notFound() throws Exception {
			doThrow(new NotFoundException()).when(electionService)
					.delete(ELECTION_ID);

			final RequestBuilder request = delete(
					BASE_URL + "/" + ELECTION.id());

			mockMvc.perform(request).andExpect(status().isNotFound());
		}
	}

	private static String infoJson(final TestElection election) {
		return """
				{
					"id": %d,
					"topic": "%s",
					"description": "%s",
					"startedOn": %s,
					"endedOn": %s
				}
				""".formatted(election.id(), election.topic(),
				election.description(), map(election.startedOn()),
				map(election.endedOn()));
	}

	private static String map(final OffsetDateTime timestamp) {
		return Optional.ofNullable(timestamp)
				.map(DateTimeFormatter.ISO_DATE_TIME::format)
				.map(value -> "\"" + value + "\"")
				.orElse(null);
	}

}
