<script lang="ts">
	import { invalidateAll } from "$app/navigation";
	import { t } from "$lib/translations/index";

	import CandidateTable from "$lib/candidate/CandidateTable.svelte";
	import VoteCountTable from "$lib/vote/VoteCountTable.svelte";

	import DropdownButton from "$lib/common/button/DropdownButton.svelte";
	import { ElectionClient } from "$lib/election/ElectionClient";
	import {
		EndElectionequest,
		ScheduleElectionRequest,
		StartElectionRequest,
		type Election
	} from "$lib/election/ElectionTypes";
	import ScheduleElectionDialog from "$lib/election/ScheduleElectionDialog.svelte";

	const { data } = $props();

	let tab: string = $state("candidates");
	let isScheduleDialogVisible: boolean = $state(false);

	function openTab(tabName: string): void {
		tab = tabName;
	}

	function isEditable(election: Election): boolean {
		return (
			election.startedOn == null ||
			new Date(election.startedOn) > new Date()
		);
	}

	function startElection(): void {
		ElectionClient.patch(data.info.id, new StartElectionRequest()).then(
			invalidateAll
		);
	}

	function endElection(): void {
		ElectionClient.patch(data.info.id, new EndElectionequest()).then(
			invalidateAll
		);
	}

	function showScheduleDialog(): void {
		isScheduleDialogVisible = true;
	}

	function scheduleElection(action: ScheduleElectionRequest): void {
		ElectionClient.patch(data.info.id, action)
			.then(invalidateAll)
			.then(() => (isScheduleDialogVisible = false));
	}
</script>

<h1>{@html data.info.topic}</h1>
<div>
	{@html data.info.startedOn
		? new Date(data.info.startedOn).toLocaleString()
		: ""} - {@html data.info.endedOn
		? new Date(data.info.endedOn).toLocaleString()
		: ""}
</div>
<h2>{@html data.info.description}</h2>

{#if data.info.startedOn ? new Date(data.info.startedOn) > new Date() : true}
	<DropdownButton
		actions={[
			{
				label: $t("common.start"),
				onClick: startElection
			},
			{
				label: $t("common.schedule"),
				onClick: showScheduleDialog
			}
		]}
	/>
{:else if data.info.endedOn ? new Date(data.info.endedOn) > new Date() : true}
	<DropdownButton
		actions={[
			{
				label: $t("common.end"),
				onClick: endElection
			},
			{
				label: $t("common.schedule"),
				onClick: showScheduleDialog
			}
		]}
	/>
{/if}

<div class="tab">
	<button
		class={tab == "candidates" ? "active" : ""}
		onclick={() => openTab("candidates")}
	>
		{$t("common.candidates")}
	</button>
	<button
		class={tab == "votes" ? "active" : ""}
		onclick={() => openTab("votes")}
	>
		{$t("common.votes")}
	</button>
</div>

<div class="tabcontent">
	{#if tab == "candidates"}
		<h2>{$t("common.candidates")}</h2>
		<CandidateTable
			electionId={data.info.id}
			candidates={data.candidates}
			editable={isEditable(data.info)}
			onChange={invalidateAll}
			onAdd={invalidateAll}
		/>
	{:else if tab === "votes"}
		<h2>{$t("common.votes")}</h2>
		<VoteCountTable
			electionId={data.info.id}
			candidates={data.candidates}
			voteCounts={data.voteCounts}
		/>
	{/if}
</div>

{#if isScheduleDialogVisible}
	<ScheduleElectionDialog
		startsOn={data.info.startedOn}
		endsOn={data.info.endedOn}
		onClose={() => (isScheduleDialogVisible = false)}
		onSchedule={scheduleElection}
	/>
{/if}
