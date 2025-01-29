import {
	getAllByTestId,
	getByTestId,
	queryAllByTestId,
	queryByTestId,
	screen
} from "@testing-library/svelte";

import ElementLocator from "./ElementLocator";

export class MockLocator extends ElementLocator<HTMLElement> {
	private testId: string;

	private constructor(testId: string) {
		super();
		this.testId = testId;
	}

	public static withTestId(testId: string): MockLocator {
		return new MockLocator(testId);
	}

	protected doGet(): HTMLElement {
		if (this.parent !== undefined) {
			return getByTestId(this.parent.get(), this.testId);
		} else {
			return screen.getByTestId(this.testId);
		}
	}

	protected doGetAll(): HTMLElement[] {
		if (this.parent !== undefined) {
			return getAllByTestId(this.parent.get(), this.testId);
		} else {
			return screen.getAllByTestId(this.testId);
		}
	}

	protected doQuery(): HTMLElement | null {
		if (this.parent !== undefined) {
			return queryByTestId(this.parent.get(), this.testId);
		} else {
			return screen.queryByTestId(this.testId);
		}
	}

	protected doQueryAll(): Array<HTMLElement> {
		if (this.parent !== undefined) {
			return queryAllByTestId(this.parent.get(), this.testId);
		} else {
			return screen.queryAllByTestId(this.testId);
		}
	}
}
