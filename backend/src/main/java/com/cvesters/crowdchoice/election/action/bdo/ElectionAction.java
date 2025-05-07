package com.cvesters.crowdchoice.election.action.bdo;

import java.util.Objects;

import org.apache.commons.lang3.Validate;

import lombok.Getter;

import com.cvesters.crowdchoice.election.bdo.ElectionInfo;
import com.cvesters.crowdchoice.exceptions.OperationNotAllowedException;

public abstract class ElectionAction {

	@Getter
	protected final long electionId;

	public ElectionAction(final long electionId) {
		this.electionId = electionId;
	}

	public void apply(final ElectionInfo info) {
		Objects.requireNonNull(info);
		Objects.requireNonNull(info.getId());
		Validate.isTrue(info.getId() == electionId);

		try {
			doApply(info);
		} catch (final IllegalArgumentException | IllegalStateException e) {
			throw new OperationNotAllowedException(e);
		}
	}

	protected abstract void doApply(final ElectionInfo info);
}
