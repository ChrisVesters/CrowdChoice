<script lang="ts">
	import type { ChangeEventHandler } from "svelte/elements";

	import { t } from "$lib/translations/index";

	import { CandidateClient } from "./CandidateClient";
	
	import type { Candidate } from "./CandidateTypes";

	export type CandidateTableprops = {
		electionId: number;
		candidates: Candidate[];
		onChange: () => void;
	};

	const props: CandidateTableprops = $props();

	let selected: number[] = $state([]);

	const toggleAll: ChangeEventHandler<HTMLInputElement> = e => {
		if (props.candidates.length == 0) {
			e.currentTarget.checked = false;
			return;
		}

		selected = e.currentTarget.checked
			? props.candidates.map(candidate => candidate.id)
			: [];
	};

	function handleRemoveElections(): void {
		Promise.allSettled(
			selected.map(id => CandidateClient.delete(props.electionId, id))
		)
			.then(props.onChange)
			.then(() => (selected = []));
	}
</script>

<button onclick={handleRemoveElections} disabled={selected.length == 0}>
	{$t("common.remove")}
</button>

<table>
	<thead>
		<tr>
			<th>
				<input
					style:vertical-align="text-bottom"
					type="checkbox"
					onchange={toggleAll}
					checked={props.candidates.length > 0 &&
						selected.length == props.candidates.length}
				/>
			</th>
			<th>{$t("common.name")}</th>
		</tr>
	</thead>

	<tbody>
		{#each props.candidates as candidate}
			<tr>
				<td>
					<input
						style:vertical-align="text-bottom"
						bind:group={selected}
						type="checkbox"
						value={candidate.id}
					/>
				</td>
				<td> {candidate.name} </td>
			</tr>
		{/each}
	</tbody>
</table>
