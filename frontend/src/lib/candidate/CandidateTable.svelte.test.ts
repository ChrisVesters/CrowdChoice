import { render } from "@testing-library/svelte";
import { userEvent } from "@testing-library/user-event";
import { test } from "vitest";

import { assertThat } from "$test/assertions/Assert";
import { ButtonLocator } from "$test/locators/ButtonLocator";
import { CheckboxLocator } from "$test/locators/CheckboxLocator";
import { TableLocator, TableRowLocator } from "$test/locators/TableLocator";
import { setupTranslations } from "$test/Mocks";

import CandidateTable from "$lib/candidate/CandidateTable.svelte";
import { CandidateClient } from "./CandidateClient";
import type { Candidate } from "./CandidateTypes";

await setupTranslations();

vi.mock("$lib/candidate/CandidateClient");

const onChangeMock = vi.fn();

const ui = {
	removeButton: ButtonLocator.withLabel("remove"),
	table: TableLocator.any()
};

const testCandidates: Candidate[] = [
	{
		id: 1,
		name: "Java"
	},
	{
		id: 2,
		name: "C#"
	}
];

beforeEach(() => {
	vi.clearAllMocks();
});

describe("renders", () => {
	test("no candidates", () => {
		render(CandidateTable, {
			target: document.body,
			props: { electionId: 1, candidates: [], onChange: onChangeMock }
		});

		assertThat(ui.removeButton).isDisabled();
		assertThat(ui.table).isInTheDocument();
		assertThat(ui.table.rows()).hasLength(1);

		const headerRow = ui.table.row(0) as TableRowLocator;
		assertThat(headerRow).isInTheDocument();
		assertThat(headerRow.headers()).hasLength(2);
		assertThat(
			headerRow.header(0).find(CheckboxLocator.any())
		).isNotChecked();
		assertThat(headerRow.header(1)).hasTextContent("name");
	});

	test("single candidate", () => {
		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0]],
				onChange: onChangeMock
			}
		});

		assertThat(ui.removeButton).isDisabled();
		assertThat(ui.table).isInTheDocument();
		assertThat(ui.table.rows()).hasLength(2);

		const headerRow = ui.table.row(0) as TableRowLocator;
		assertThat(headerRow).isInTheDocument();
		assertThat(headerRow.headers()).hasLength(2);
		assertThat(
			headerRow.header(0).find(CheckboxLocator.any())
		).isNotChecked();
		assertThat(headerRow.header(1)).hasTextContent("name");

		const dataRow = ui.table.row(1) as TableRowLocator;
		assertThat(dataRow).isInTheDocument();
		assertThat(dataRow.cells()).hasLength(2);
		assertThat(dataRow.cell(0).find(CheckboxLocator.any())).isNotChecked();
		assertThat(dataRow.cell(1)).hasTextContent("Java");
	});

	test("multiple candidates", () => {
		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0], testCandidates[1]],
				onChange: onChangeMock
			}
		});

		assertThat(ui.removeButton).isDisabled();
		assertThat(ui.table).isInTheDocument();
		assertThat(ui.table.rows()).hasLength(3);

		const headerRow = ui.table.row(0) as TableRowLocator;
		assertThat(headerRow).isInTheDocument();
		assertThat(headerRow.headers()).hasLength(2);
		assertThat(headerRow.header(0)).hasTextContent("");
		assertThat(headerRow.header(1)).hasTextContent("name");

		const firstDataRow = ui.table.row(1) as TableRowLocator;
		assertThat(firstDataRow).isInTheDocument();
		assertThat(firstDataRow.cells()).hasLength(2);
		assertThat(
			firstDataRow.cell(0).find(CheckboxLocator.any())
		).isNotChecked();
		assertThat(firstDataRow.cell(1)).hasTextContent("Java");

		const secondDataRow = ui.table.row(2) as TableRowLocator;
		assertThat(secondDataRow).isInTheDocument();
		assertThat(secondDataRow.cells()).hasLength(2);
		assertThat(
			secondDataRow.cell(0).find(CheckboxLocator.any())
		).isNotChecked();
		assertThat(secondDataRow.cell(1)).hasTextContent("C#");
	});
});

describe("toggle all", () => {
	test("select all without candidates", async () => {
		const user = userEvent.setup();

		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [],
				onChange: onChangeMock
			}
		});

		const headerRow = ui.table.row(0) as TableRowLocator;
		const selectAllCheckbox = headerRow.nth(0).find(CheckboxLocator.any());
		await user.click(selectAllCheckbox.get());

		assertThat(selectAllCheckbox).isNotChecked();
		assertThat(ui.removeButton).isDisabled();
	});

	test("select all with candidates", async () => {
		const user = userEvent.setup();

		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0], testCandidates[1]],
				onChange: onChangeMock
			}
		});

		const headerRow = ui.table.row(0) as TableRowLocator;
		const selectAllCheckbox = headerRow
			.header(0)
			.find(CheckboxLocator.any());
		await user.click(selectAllCheckbox.get());

		assertThat(selectAllCheckbox).isChecked();
		assertThat(ui.removeButton).isEnabled();

		const firstDataRow = ui.table.row(1) as TableRowLocator;
		const firstCheckbox = firstDataRow.cell(0).find(CheckboxLocator.any());
		assertThat(firstCheckbox).isChecked();

		const secondDataRow = ui.table.row(2) as TableRowLocator;
		const secondCheckbox = secondDataRow
			.cell(0)
			.find(CheckboxLocator.any());
		assertThat(secondCheckbox).isChecked();
	});

	test("unselect all", async () => {
		const user = userEvent.setup();

		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0], testCandidates[1]],
				onChange: onChangeMock
			}
		});

		const headerRow = ui.table.row(0) as TableRowLocator;
		const selectAllCheckbox = headerRow
			.header(0)
			.find(CheckboxLocator.any());
		await user.click(selectAllCheckbox.get());
		await user.click(selectAllCheckbox.get());

		assertThat(selectAllCheckbox).isNotChecked();
		assertThat(ui.removeButton).isDisabled();

		const firstDataRow = ui.table.row(1) as TableRowLocator;
		const firstCheckbox = firstDataRow.cell(0).find(CheckboxLocator.any());
		assertThat(firstCheckbox).isNotChecked();

		const secondDataRow = ui.table.row(2) as TableRowLocator;
		const secondCheckbox = secondDataRow
			.cell(0)
			.find(CheckboxLocator.any());
		assertThat(secondCheckbox).isNotChecked();
	});
});

describe("toggle one", () => {
	test("select", async () => {
		const user = userEvent.setup();

		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0], testCandidates[1]],
				onChange: onChangeMock
			}
		});

		const firstDataRow = ui.table.row(1) as TableRowLocator;
		const firstCheckbox = firstDataRow.cell(0).find(CheckboxLocator.any());
		await user.click(firstCheckbox.get());
		assertThat(firstCheckbox).isChecked();

		const secondDataRow = ui.table.row(2) as TableRowLocator;
		const secondCheckbox = secondDataRow
			.cell(0)
			.find(CheckboxLocator.any());

		assertThat(secondCheckbox).isNotChecked();

		const headerRow = ui.table.row(0) as TableRowLocator;
		const selectAll = headerRow.header(0).find(CheckboxLocator.any());
		assertThat(selectAll).isNotChecked();
		assertThat(ui.removeButton).isEnabled();
	});

	test("unselect", async () => {
		const user = userEvent.setup();

		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0], testCandidates[1]],
				onChange: onChangeMock
			}
		});

		const firstDataRow = ui.table.row(1) as TableRowLocator;
		const firstCheckbox = firstDataRow.nth(0).find(CheckboxLocator.any());
		await user.click(firstCheckbox.get());
		await user.click(firstCheckbox.get());

		assertThat(firstCheckbox).isNotChecked();
		assertThat(ui.removeButton).isDisabled();

		const headerRow = ui.table.row(0) as TableRowLocator;
		const selectAllCheckbox = headerRow.nth(0).find(CheckboxLocator.any());
		assertThat(selectAllCheckbox).isNotChecked();
	});

	test("select all", async () => {
		const user = userEvent.setup();

		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0], testCandidates[1]],
				onChange: onChangeMock
			}
		});

		const firstDataRow = ui.table.row(1) as TableRowLocator;
		const firstCheckbox = firstDataRow.cell(0).find(CheckboxLocator.any());
		await user.click(firstCheckbox.get());
		assertThat(firstCheckbox).isChecked();

		const secondDataRow = ui.table.row(2) as TableRowLocator;
		const secondCheckbox = secondDataRow
			.cell(0)
			.find(CheckboxLocator.any());
		await user.click(secondCheckbox.get());
		assertThat(secondCheckbox).isChecked();

		const headerRow = ui.table.row(0) as TableRowLocator;
		const selectAllCheckbox = headerRow
			.header(0)
			.find(CheckboxLocator.any());
		assertThat(selectAllCheckbox).isChecked();
	});
});

describe("actions", () => {
	test("remove candidate", async () => {
		vi.mocked(CandidateClient.delete).mockResolvedValue(undefined);

		const user = userEvent.setup();

		render(CandidateTable, {
			props: {
				electionId: 1,
				candidates: [testCandidates[0]],
				onChange: onChangeMock
			}
		});

		const headerRow = ui.table.row(0) as TableRowLocator;
		const selectAllCheckbox = headerRow
			.header(0)
			.find(CheckboxLocator.any());
		await user.click(selectAllCheckbox.get());
		await user.click(ui.removeButton.get());

		expect(CandidateClient.delete).toHaveBeenCalledTimes(1);
		expect(CandidateClient.delete).toHaveBeenNthCalledWith(
			1,
			1,
			testCandidates[0].id
		);
		expect(onChangeMock).toHaveBeenCalled();
	});
});
