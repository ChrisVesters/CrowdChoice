package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
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

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.exceptions.ConflictException;
import com.cvesters.crowdchoice.exceptions.NotFoundException;

@WebMvcTest(CandidateController.class)
class CandidateControllerTest {

	private static final long ELECTION_ID = 1L;
	private static final String BASE_URL = "/api/elections/" + ELECTION_ID
			+ "/candidates";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CandidateService candidateService;

	@Nested
	class GetAll {

		@Test
		void empty() throws Exception {
			when(candidateService.findAll(ELECTION_ID))
					.thenReturn(Collections.emptyList());

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json("[]"));
		}

		@Test
		void single() throws Exception {
			final List<TestCandidate> candidates = List
					.of(TestCandidate.MICRONAUT);

			final List<Candidate> bdos = candidates.stream()
					.map(TestCandidate::bdo)
					.toList();
			when(candidateService.findAll(ELECTION_ID)).thenReturn(bdos);

			final RequestBuilder request = get(BASE_URL);

			final String expectedBody = candidates.stream()
					.map(TestCandidate::infoJson)
					.collect(Collectors.joining(",", "[", "]"));

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(expectedBody));
		}

		@Test
		void multiple() throws Exception {
			final List<TestCandidate> candidates = List.of(
					TestCandidate.MICRONAUT, TestCandidate.DOCKER,
					TestCandidate.LOMBOK);

			final List<Candidate> bdos = candidates.stream()
					.map(TestCandidate::bdo)
					.toList();
			when(candidateService.findAll(ELECTION_ID)).thenReturn(bdos);

			final RequestBuilder request = get(BASE_URL);

			final String expectedBody = candidates.stream()
					.map(TestCandidate::infoJson)
					.collect(Collectors.joining(",", "[", "]"));

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(expectedBody));
		}

		@Test
		void electionNotFound() throws Exception {
			when(candidateService.findAll(ELECTION_ID))
					.thenThrow(new NotFoundException());

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request).andExpect(status().isNotFound());
		}
	}

	@Nested
	class Create {

		@Test
		void success() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = requestJson(candidate.name());

			final Candidate bdo = candidate.bdo();

			when(candidateService.create(eq(ELECTION_ID), argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getName()).isEqualTo(candidate.name());
				return true;
			}))).thenReturn(bdo);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(candidate.infoJson()));
		}

		@Test
		void duplicate() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = requestJson(candidate.name());

			when(candidateService.create(eq(ELECTION_ID), argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getName()).isEqualTo(candidate.name());
				return true;
			}))).thenThrow(new ConflictException());

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isConflict());
		}

		@Test
		void electionNotFound() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = requestJson(candidate.name());

			when(candidateService.create(eq(ELECTION_ID), argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getName()).isEqualTo(candidate.name());
				return true;
			}))).thenThrow(new NotFoundException());

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isNotFound());
		}

		private String requestJson(final String name) {
			return """
					{
						"name": "%s"
					}
					""".formatted(name);
		}
	}
}