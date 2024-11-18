<script lang="ts">
	import { goto, invalidateAll } from "$app/navigation";

	import { ElectionClient } from "$lib/election/ElectionClient";

	const { data } = $props();

	let topicField: HTMLInputElement;

	function handleAddElection(): void {
		ElectionClient.create(topicField.value).then(election => {
			goto(`/elections/${election.id}`);
		});
	}

	function handleRemoveElection(id: number): void {
		ElectionClient.delete(id).then(invalidateAll);
	}
</script>

<h1>Overview</h1>
<h2>Elections</h2>
<ul>
	{#each data.elections as election}
		<li>
			<button onclick={() => handleRemoveElection(election.id)}>
				Remove
			</button>
			<a href="/elections/{election.id}"> {election.topic}</a>
		</li>
	{/each}
</ul>

<h3>New Election</h3>
<input bind:this={topicField} />
<button onclick={handleAddElection}>Add</button>
