<script lang="ts">
	import type { ChangeEventHandler } from "svelte/elements";

	import { t } from "$lib/translations/index";

	import { CandidateClient } from "./CandidateClient";

	import AddCandidateDialog from "./AddCandidateDialog.svelte";
	import type { Candidate, CreateCandidateRequest } from "./CandidateTypes";

	export type CandidateTableprops = {
		electionId: number;
		candidates: Candidate[];
		onChange: () => void;
		onAdd: (id: number) => void;
	};

	const props: CandidateTableprops = $props();

	let selected: number[] = $state([]);
	let isAddCandidateDialogVisible: boolean = $state(false);

	const toggleAll: ChangeEventHandler<HTMLInputElement> = e => {
		if (props.candidates.length == 0) {
			e.currentTarget.checked = false;
			return;
		}

		selected = e.currentTarget.checked
			? props.candidates.map(candidate => candidate.id)
			: [];
	};

	function handleRemoveCandidates(): void {
		Promise.allSettled(
			selected.map(id => CandidateClient.delete(props.electionId, id))
		)
			.then(props.onChange)
			.then(() => (selected = []));
	}

	function showAddCandidateDialog(): void {
		isAddCandidateDialogVisible = true;
	}

	function hideAddCandidateDialog(): void {
		isAddCandidateDialogVisible = false;
	}

	function addCandidate(request: CreateCandidateRequest): void {
		hideAddCandidateDialog();
		CandidateClient.create(props.electionId, request).then(candidate =>
			props.onAdd(candidate.id)
		);
	}
</script>

<button onclick={handleRemoveCandidates} disabled={selected.length == 0}>
	{$t("common.remove")}
</button>
<button onclick={showAddCandidateDialog}>
	{$t("common.add")}
</button>

<table>
	<thead>
		<tr>
			<th>
				<input
					style:vertical-align="text-bottom"
					class="fullWidth"
					type="checkbox"
					onchange={toggleAll}
					checked={props.candidates.length > 0 &&
						selected.length == props.candidates.length}
				/>
			</th>
			<th>{$t("common.name")}</th>
			<th>{$t("common.description")}</th>
		</tr>
	</thead>

	<tbody>
		{#each props.candidates as candidate}
			<tr>
				<td>
					<input
						bind:group={selected}
						style:vertical-align="text-bottom"
						class="fullWidth"
						type="checkbox"
						value={candidate.id}
					/>
				</td>
				<td> {candidate.name} </td>
				<td> {candidate.description} </td>
			</tr>
		{/each}
	</tbody>
</table>

{#if isAddCandidateDialogVisible === true}
	<AddCandidateDialog onClose={hideAddCandidateDialog} onAdd={addCandidate} />
{/if}
