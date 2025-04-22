package com.cvesters.crowdchoice.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;
import com.cvesters.crowdchoice.vote.dao.VoteCountView;

@WebMvcTest(VoteController.class)
class VoteControllerTest {

	private static final long ELECTION_ID = 1L;
	private static final String BASE_URL = "/api/elections/" + ELECTION_ID
			+ "/votes";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private VoteService voteService;

	@Nested
	class GetCounts {

		@Test
		void success() throws Exception {
			final long candidate1Id = 2L;
			final long candiate1Votes = 10L;

			final long candidate2Id = 3L;
			final long candiate2Votes = 7L;

			final var candidate1Votes = mock(VoteCountView.class);
			when(candidate1Votes.getCandidateId()).thenReturn(candidate1Id);
			when(candidate1Votes.getVoteCount()).thenReturn(candiate1Votes);

			final var candidate2Votes = mock(VoteCountView.class);
			when(candidate2Votes.getCandidateId()).thenReturn(candidate2Id);
			when(candidate2Votes.getVoteCount()).thenReturn(candiate2Votes);

			when(voteService.getCounts(ELECTION_ID))
					.thenReturn(List.of(candidate1Votes, candidate2Votes));

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().json("""
							[
								{
									"candidateId": %d,
									"voteCount": %d
								},
								{
									"candidateId": %d,
									"voteCount": %d
								}
							]
									""".formatted(candidate1Id, candiate1Votes,
							candidate2Id, candiate2Votes)));
		}

		@Test
		void error() throws Exception {
			when(voteService.getCounts(ELECTION_ID))
					.thenThrow(RuntimeException.class);

			final RequestBuilder request = get(BASE_URL);

			mockMvc.perform(request).andExpect(status().is5xxServerError());
		}
	}

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

		@Test
		void operationNotAllowed() throws Exception{
			final TestVote election = TestVote.TRUMP;
			final String requestBody = requestJson(election);

			when(voteService.create(anyLong(), any()))
					.thenThrow(OperationNotAllowedException.class);

			final RequestBuilder request = post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(requestBody);

			mockMvc.perform(request).andExpect(status().isMethodNotAllowed());
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
