package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.exceptions.NotFoundException;

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

		@Test
		void single() throws Exception {
			final List<TestElection> elections = List.of(TestElection.TOPICS);

			final List<ElectionInfo> infos = elections.stream()
					.map(TestElection::info)
					.toList();
			when(electionService.findAll()).thenReturn(infos);

			final String expectedBody = elections.stream()
					.map(TestElection::infoJson)
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
					.map(TestElection::infoJson)
					.collect(Collectors.joining(",", "[", "]"));

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(expectedBody));
		}
	}

	@Nested
	class Get {

		private static final TestElection ELECTION = TestElection.TOPICS;
		private static final String ENDPOINT = BASE_URL + "/" + ELECTION.id();

		@Test
		void success() throws Exception {
			when(electionService.get(ELECTION.id()))
					.thenReturn(ELECTION.info());

			final RequestBuilder request = get(ENDPOINT);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(ELECTION.infoJson()));
		}

		@Test
		void notFound() throws Exception {
			when(electionService.get(ELECTION.id()))
					.thenThrow(new NotFoundException());

			final RequestBuilder request = get(ENDPOINT);

			mockMvc.perform(request).andExpect(status().isNotFound());
		}
	}

	@Nested
	class Create {

		@Test
		void success() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = requestJson(election.topic(),
					election.description());

			when(electionService.create(argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getTopic()).isEqualTo(election.topic());
				assertThat(request.getDescription())
						.isEqualTo(election.description());
				return true;
			}))).thenReturn(election.info());

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andExpect(content().json(election.infoJson()));
		}

		@Test
		void withoutBody() throws Exception {
			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void invalid() throws Exception {
			final String requestBody = requestJson("", "");

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void error() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = requestJson(election.topic(),
					election.description());

			when(electionService.create(any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}

		private String requestJson(final String topic,
				final String description) {
			return """
					{
						"topic": "%s",
						"description": "%s"
					}
					""".formatted(topic, description);
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
}
