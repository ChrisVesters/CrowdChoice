<script lang="ts">
	import { invalidateAll } from "$app/navigation";
	import { t } from '$lib/translations/index';

	import { CandidateClient } from "$lib/candidate/CandidateClient";

	const { data } = $props();

	let candidateNameField: HTMLInputElement;

	function handleAddCandidate(): void {
		CandidateClient.create(data.info.id, candidateNameField.value)
			.then(() => (candidateNameField.value = ""))
			.then(invalidateAll);
	}

	function handleRemoveCandidate(candidateId: number): void {
		CandidateClient.delete(data.info.id, candidateId)
			.then(invalidateAll);
	}
</script>

<h1>{@html data.info.topic}</h1>
<h2>{$t("common.candidates")}</h2>
<ul>
	{#each data.candidates as candidate}
		<li>
			<button onclick={() => handleRemoveCandidate(candidate.id)}>
				{$t("common.remove")}
			</button>
			{candidate.name}
		</li>
	{/each}
</ul>

<h3>{$t("common.newObject", { object: $t("common.candidate") })}</h3>
<input bind:this={candidateNameField} />
<button onclick={handleAddCandidate}>{$t("common.add")}</button>
