<script lang="ts">
	import type { ChangeEventHandler } from "svelte/elements";

	import { t } from "$lib/translations/index";

	import AddElectionDialog from "./AddElectionDialog.svelte";
	import { ElectionClient } from "./ElectionClient";
	import type { CreateElectionRequest, Election } from "./ElectionTypes";

	export type ElectionTableProps = {
		elections: Election[];
		onChange: () => void;
		onAdd: (id: number) => void;
	};

	const props: ElectionTableProps = $props();

	let selected: number[] = $state([]);
	let isAddElectionDialogVisible: boolean = $state(false);

	const toggleAll: ChangeEventHandler<HTMLInputElement> = e => {
		if (props.elections.length == 0) {
			e.currentTarget.checked = false;
			return;
		}

		selected = e.currentTarget.checked
			? props.elections.map(election => election.id)
			: [];
	};

	function handleRemoveElections(): void {
		Promise.allSettled(selected.map(id => ElectionClient.delete(id)))
			.then(props.onChange)
			.then(() => (selected = []));
	}

	function showAddElectionDialog(): void {
		isAddElectionDialogVisible = true;
	}

	function hideAddElectionDialog(): void {
		isAddElectionDialogVisible = false;
	}

	function addElection(request: CreateElectionRequest): void {
		hideAddElectionDialog();
		ElectionClient.create(request).then(election =>
			props.onAdd(election.id)
		);
	}
</script>

<button onclick={handleRemoveElections} disabled={selected.length == 0}>
	{$t("common.remove")}
</button>
<button onclick={showAddElectionDialog}>
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
					checked={props.elections.length > 0 &&
						selected.length == props.elections.length}
				/>
			</th>
			<th>{$t("common.topic")}</th>
			<th>{$t("common.description")}</th>
		</tr>
	</thead>

	<tbody>
		{#each props.elections as election}
			<tr>
				<td>
					<input
						bind:group={selected}
						style:vertical-align="text-bottom"
						class="fullWidth"
						type="checkbox"
						value={election.id}
					/>
				</td>
				<td>
					<a href="/elections/{election.id}">{election.topic}</a>
				</td>
				<td>{election.description}</td>
			</tr>
		{/each}
	</tbody>
</table>

{#if isAddElectionDialogVisible === true}
	<AddElectionDialog onClose={hideAddElectionDialog} onAdd={addElection} />
{/if}
