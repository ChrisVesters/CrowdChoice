<script lang="ts">
	import { invalidateAll } from "$app/navigation";
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
<h2>Candidates</h2>
<ul>
	{#each data.candidates as candidate}
		<li>
			<button onclick={() => handleRemoveCandidate(candidate.id)}>
				Remove
			</button>
			{candidate.name}
		</li>
	{/each}
</ul>

<h3>New Candidate</h3>
<input bind:this={candidateNameField} />
<button onclick={handleAddCandidate}>Add</button>
