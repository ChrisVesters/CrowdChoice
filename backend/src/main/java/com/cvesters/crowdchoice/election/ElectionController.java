package com.cvesters.crowdchoice.election;

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

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.election.dto.ElectionCreateDto;
import com.cvesters.crowdchoice.election.dto.ElectionInfoDto;
import com.cvesters.crowdchoice.election.dto.ElectionUpdateDto;

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

	@GetMapping("/{electionId}")
	public ElectionInfoDto get(@PathVariable final long electionId) {
		final ElectionInfo info = electionService.get(electionId);
		return ElectionMapper.toDto(info);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ElectionInfoDto create(
			@RequestBody final ElectionCreateDto electionDto) {
		final ElectionInfo requested = ElectionMapper.fromDto(electionDto);
		final ElectionInfo created = electionService.create(requested);
		return ElectionMapper.toDto(created);
	}

	@PutMapping("/{electionId}")
	public ElectionInfoDto update(@PathVariable final long electionId,
			@RequestBody final ElectionUpdateDto electionDto) {
		final ElectionInfo requested = ElectionMapper.fromDto(electionId,
				electionDto);
		final ElectionInfo created = electionService.update(requested);
		return ElectionMapper.toDto(created);
	}

	@DeleteMapping("/{electionId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable final long electionId) {
		electionService.delete(electionId);
	}
}
