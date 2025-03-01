package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@WebMvcTest(VoteController.class)
class VoteControllerTest {

	private static final long ELECTION_ID = 1L;
	private static final String BASE_URL = "/api/elections/" + ELECTION_ID
			+ "/votes";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VoteService voteService;

	@Nested
	class CreateVote {

		@Test
		void success() throws Exception {
			final TestVote vote = TestVote.TRUMP;
			final String requestBody = requestJson(vote);

			when(voteService.create(eq(ELECTION_ID), argThat(request -> {
				assertThat(request.getId()).isNull();
				assertThat(request.getCandidateId())
						.isEqualTo(vote.candidate().id());
				return true;
			}))).thenReturn(vote.bdo());

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andExpect(content().json(infoJson(vote)));
		}

		@Test
		void withoutBody() throws Exception {
			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void invalid() throws Exception {
			final String requestBody = "{}";

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isBadRequest());
		}

		@Test
		void error() throws Exception {
			final TestVote election = TestVote.TRUMP;
			final String requestBody = requestJson(election);

			when(voteService.create(anyLong(), any()))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}

		private String requestJson(final TestVote vote) {
			return """
					{
						"candidateId": %d
					}
					""".formatted(vote.candidate().id());
		}

		private String infoJson(final TestVote vote) {
			final String formattedCastedOn = DateTimeFormatter.ISO_DATE_TIME
					.format(vote.castedOn());
			return """
					{
						"id": %d,
						"castedOn": "%s",
						"candidateId": %d
					}
					""".formatted(vote.id(), formattedCastedOn,
					vote.candidate().id());
		}
	}

}
