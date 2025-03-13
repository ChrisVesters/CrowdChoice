package com.cvesters.crowdchoice.vote;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cvesters.crowdchoice.vote.bdo.Vote;
import com.cvesters.crowdchoice.vote.dao.VoteCountView;
import com.cvesters.crowdchoice.vote.dto.VoteCountDto;
import com.cvesters.crowdchoice.vote.dto.VoteCreateDto;
import com.cvesters.crowdchoice.vote.dto.VoteDto;

@RestController
@RequestMapping("/api/elections/{electionId}/votes")
public class VoteController {

	private final VoteService voteService;

	public VoteController(final VoteService voteService) {
		this.voteService = voteService;
	}

	@GetMapping
	public List<VoteCountDto> getCounts(@PathVariable final long electionId) {
		final List<VoteCountView> counts = voteService.getCounts(electionId);
		return VoteMapper.countView(counts);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public VoteDto create(@PathVariable final long electionId,
			@RequestBody final VoteCreateDto voteDto) {
		final Vote requested = VoteMapper.fromDto(voteDto);
		final Vote created = voteService.create(electionId, requested);
		return VoteMapper.toDto(created);
	}
}
