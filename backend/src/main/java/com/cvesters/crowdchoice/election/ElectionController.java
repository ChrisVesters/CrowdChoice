package com.cvesters.crowdchoice.election;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dto.ElectionCreateDto;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;

@RestController
@RequestMapping("/api/elections")
public class ElectionController {

	private final ElectionService electionService;

	public ElectionController(final ElectionService electionService) {
		this.electionService = electionService;
	}

	@GetMapping
	public List<ElectionInfoDto> getAll() {
		final List<ElectionInfo> elections = electionService.findAll();
		return ElectionMapper.toDto(elections);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ElectionInfoDto create(
			@RequestBody final ElectionCreateDto electionDto) {
		final ElectionInfo requested = ElectionMapper.fromDto(electionDto);
		final ElectionInfo created = electionService.create(requested);
		return ElectionMapper.toDto(created);
	}
}
