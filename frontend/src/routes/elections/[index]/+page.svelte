<script lang="ts">
	import { invalidateAll } from "$app/navigation";
	import { t } from '$lib/translations/index';

	import { CandidateClient } from "$lib/candidate/CandidateClient";
	import CandidateTable from "$lib/candidate/CandidateTable.svelte";

	const { data } = $props();

	let candidateNameField: HTMLInputElement;

	function handleAddCandidate(): void {
		CandidateClient.create(data.info.id, candidateNameField.value)
			.then(() => (candidateNameField.value = ""))
			.then(invalidateAll);
	}
</script>

<h1>{@html data.info.topic}</h1>
<h2>{$t("common.candidates")}</h2>

<CandidateTable electionId={data.info.id} candidates={data.candidates} onChange={invalidateAll} />

<h3>{$t("common.newObject", { object: $t("common.candidate") })}</h3>
<input bind:this={candidateNameField} />
<button onclick={handleAddCandidate}>{$t("common.add")}</button>
