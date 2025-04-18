<script lang="ts">
	import type { ChangeEventHandler } from "svelte/elements";

	import { t } from "$lib/translations/index";

	import AddElectionDialog from "./AddElectionDialog.svelte";
	import { ElectionClient } from "./ElectionClient";
	import UpdateElectionDialog from "./UpdateElectionDialog.svelte";

	import type {
		CreateElectionRequest,
		Election,
		UpdateElectionRequest
	} from "./ElectionTypes";

	export type ElectionTableProps = {
		elections: Array<Election>;
		onChange: () => void;
		onAdd: (id: number) => void;
	};

	const props: ElectionTableProps = $props();

	let selected: Array<number> = $state([]);
	let isAddElectionDialogVisible: boolean = $state(false);
	let isUpdateElectionDialogVisible: boolean = $state(false);

	let selectedElections = $derived(
		props.elections.filter(election => selected.includes(election.id))
	);

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

	function showUpdateElectionDialog(): void {
		isUpdateElectionDialogVisible = true;
	}

	function hideUpdateElectionDialog(): void {
		isUpdateElectionDialogVisible = false;
	}

	function updateElection(
		electionId: number,
		request: UpdateElectionRequest
	): void {
		hideUpdateElectionDialog();
		ElectionClient.update(electionId, request).then(props.onChange);
	}

	function isEditable(election: Election): boolean {
		return (
			election.startedOn == null ||
			new Date(election.startedOn) > new Date()
		);
	}

	function getStatus(election: Election): string {
		if (election.startedOn == null) {
			return $t("common.draft");
		} else if (new Date(election.startedOn) > new Date()) {
			return $t("common.scheduled");
		} else if (
			election.endedOn == null ||
			new Date(election.endedOn) > new Date()
		) {
			return $t("common.inProgress");
		} else {
			return $t("common.finished");
		}
	}
</script>

<button onclick={showAddElectionDialog}>
	{$t("common.add")}
</button>
<button onclick={handleRemoveElections} disabled={selected.length == 0}>
	{$t("common.remove")}
</button>
<button
	onclick={showUpdateElectionDialog}
	disabled={selectedElections.length != 1 ||
		!isEditable(selectedElections[0])}
>
	{$t("common.edit")}
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
			<th>{$t("common.status")}</th>
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
				<td>{getStatus(election)}</td>
			</tr>
		{/each}
	</tbody>
</table>

{#if isAddElectionDialogVisible === true}
	<AddElectionDialog onClose={hideAddElectionDialog} onAdd={addElection} />
{/if}

{#if isUpdateElectionDialogVisible === true}
	<UpdateElectionDialog
		onClose={hideUpdateElectionDialog}
		onUpdate={updateElection}
		election={selectedElections.length > 0
			? selectedElections[0]
			: props.elections[0]}
	/>
{/if}
