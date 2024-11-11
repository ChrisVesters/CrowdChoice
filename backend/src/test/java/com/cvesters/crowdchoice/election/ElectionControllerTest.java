package com.cvesters.crowdchoice.election;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;

@WebMvcTest(ElectionController.class)
class ElectionControllerTest {

	private static final String BASE_URL = "/api/elections";

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
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
	class Create {

		@Test
		void success() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = "{ \"topic\": \"" + election.topic()
					+ "\" }";

			when(electionService.create(argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getTopic()).isEqualTo(election.topic());
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
		void invalid() throws Exception {
			final String requestBody = "{ \"topic\": \"\" }";

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void error() throws Exception {
			final TestElection election = TestElection.TOPICS;
			final String requestBody = "{ \"topic\": \"" + election.topic()
					+ "\" }";

			when(electionService.create(any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}
	}
}
