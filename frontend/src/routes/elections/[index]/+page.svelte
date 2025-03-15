<script lang="ts">
	import { invalidateAll } from "$app/navigation";
	import { t } from "$lib/translations/index";

	import CandidateTable from "$lib/candidate/CandidateTable.svelte";
	import VoteCountTable from "$lib/vote/VoteCountTable.svelte";

	const { data } = $props();

	let tab: string = $state("candidates");

	function openTab(tabName: string): void {
		tab = tabName;
	}
</script>

<h1>{@html data.info.topic}</h1>

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
