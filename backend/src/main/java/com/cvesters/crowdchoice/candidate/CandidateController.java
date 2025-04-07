package com.cvesters.crowdchoice.candidate;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cvesters.crowdchoice.candidate.bdo.Candidate;
import com.cvesters.crowdchoice.candidate.dto.CandidateCreateDto;
import com.cvesters.crowdchoice.candidate.dto.CandidateDto;
import com.cvesters.crowdchoice.candidate.dto.CandidateUpdateDto;

@RestController
@RequestMapping("/api/elections/{electionId}/candidates")
public class CandidateController {

	private final CandidateService candidateService;

	public CandidateController(final CandidateService candidateService) {
		this.candidateService = candidateService;
	}

	@GetMapping
	public List<CandidateDto> getAll(@PathVariable final long electionId) {
		final List<Candidate> candidates = candidateService.findAll(electionId);
		return CandidateMapper.toDto(candidates);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CandidateDto create(@PathVariable final long electionId,
			@RequestBody CandidateCreateDto candidateDto) {
		final Candidate requested = CandidateMapper.fromDto(candidateDto);
		final Candidate created = candidateService.create(electionId,
				requested);
		return CandidateMapper.toDto(created);
	}

	@PutMapping("/{candidateId}")
	public CandidateDto update(@PathVariable final long electionId,
			@PathVariable final long candidateId,
			@RequestBody final CandidateUpdateDto candidateDto) {
		final Candidate requested = CandidateMapper.fromDto(candidateId,
				candidateDto);
		final Candidate updated = candidateService.update(electionId,
				requested);
		return CandidateMapper.toDto(updated);
	}

	@DeleteMapping("/{candidateId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable final long electionId,
			@PathVariable final long candidateId) {
		candidateService.delete(electionId, candidateId);
	}
}
