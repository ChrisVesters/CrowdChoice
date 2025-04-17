package com.cvesters.crowdchoice.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.exceptions.ConflictException;
import com.cvesters.crowdchoice.exceptions.NotFoundException;
import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;

@WebMvcTest(CandidateController.class)
class CandidateControllerTest {

	private static final long ELECTION_ID = 1L;
	private static final String BASE_URL = "/api/elections/" + ELECTION_ID
			+ "/candidates";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
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
					.map(CandidateControllerTest::infoJson)
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
					.map(CandidateControllerTest::infoJson)
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
			final String requestBody = requestJson(candidate.name(),
					candidate.description());

			final Candidate bdo = candidate.bdo();

			when(candidateService.create(eq(ELECTION_ID), argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getName()).isEqualTo(candidate.name());
				assertThat(request.getDescription())
						.isEqualTo(candidate.description());
				return true;
			}))).thenReturn(bdo);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andExpect(content().json(infoJson(candidate)));
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
		void duplicate() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = requestJson(candidate.name(),
					candidate.description());

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
			final String requestBody = requestJson(candidate.name(),
					candidate.description());

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

		@Test
		void error() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = requestJson(candidate.name(),
					candidate.description());

			when(candidateService.create(anyLong(), any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}

		@Test
		void operationNotAllowed() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = requestJson(candidate.name(),
					candidate.description());

			when(candidateService.create(anyLong(), any()))
					.thenThrow(OperationNotAllowedException.class);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isMethodNotAllowed());
		}

		private String requestJson(final String name,
				final String description) {
			return """
					{
						"name": "%s",
						"description": "%s"
					}
					""".formatted(name, description);
		}
	}

	@Nested
	class Update {

		@ParameterizedTest
		@MethodSource("com.cvesters.crowdchoice.candidate.TestCandidate#candidates")
		void success(final TestCandidate candidate) throws Exception {
			final String requestBody = updateJson(candidate.name(),
					candidate.description());

			when(candidateService.update(eq(ELECTION_ID), argThat(request -> {
				assertThat(request.getId()).isEqualTo(candidate.id());
				assertThat(request.getName()).isEqualTo(candidate.name());
				assertThat(request.getDescription())
						.isEqualTo(candidate.description());
				return true;
			}))).thenReturn(candidate.bdo());

			final RequestBuilder request = put(BASE_URL + "/" + candidate.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json(infoJson(candidate)));
		}

		@Test
		void withoutBody() throws Exception {
			final long candidateId = 234L;
			final RequestBuilder request = put(BASE_URL + "/" + candidateId)
					.contentType(MediaType.APPLICATION_JSON_VALUE);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void invalid() throws Exception {
			final String requestBody = updateJson("", "");

			final long candidateId = 234L;
			final RequestBuilder request = put(BASE_URL + "/" + candidateId)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void notFound() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;

			final String requestBody = updateJson(candidate.name(),
					candidate.description());

			when(candidateService.update(anyLong(), any()))
					.thenThrow(NotFoundException.class);

			final RequestBuilder request = put(BASE_URL + "/" + candidate.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isNotFound());
		}

		@Test
		void error() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = updateJson(candidate.name(),
					candidate.description());

			when(candidateService.update(anyLong(), any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = put(BASE_URL + "/" + candidate.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}

		@Test
		void operationNotAllowed() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;
			final String requestBody = updateJson(candidate.name(),
					candidate.description());

			when(candidateService.update(anyLong(), any()))
					.thenThrow(OperationNotAllowedException.class);

			final RequestBuilder request = put(BASE_URL + "/" + candidate.id())
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isMethodNotAllowed());
		}

		private String updateJson(final String name, final String description) {
			return """
					{
						"name": "%s",
						"description": "%s"
					}
					""".formatted(name, description);
		}
	}

	@Nested
	class Delete {

		@Test
		void success() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;

			final RequestBuilder request = delete(
					BASE_URL + "/" + candidate.id());

			mockMvc.perform(request).andExpect(status().isNoContent());

			verify(candidateService).delete(ELECTION_ID, candidate.id());
		}

		@Test
		void notFound() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;

			doThrow(new NotFoundException()).when(candidateService)
					.delete(ELECTION_ID, candidate.id());

			final RequestBuilder request = delete(
					BASE_URL + "/" + candidate.id());

			mockMvc.perform(request).andExpect(status().isNotFound());
		}

		@Test
		void operationNotAllowed() throws Exception {
			final TestCandidate candidate = TestCandidate.LOMBOK;

			doThrow(new OperationNotAllowedException()).when(candidateService)
					.delete(ELECTION_ID, candidate.id());

			final RequestBuilder request = delete(
					BASE_URL + "/" + candidate.id());

			mockMvc.perform(request).andExpect(status().isMethodNotAllowed());
		}

	}

	private static String infoJson(final TestCandidate candidate) {
		return """
				{
					"id": %d,
					"name": "%s",
					"description": "%s"
				}
				""".formatted(candidate.id(), candidate.name(),
				candidate.description());
	}
}
