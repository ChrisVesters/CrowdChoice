<script lang="ts">
	import { goto } from "$app/navigation";
	
	import { ElectionClient } from "$lib/elections/ElectionClient";

	const { data } = $props();

	let topicField: HTMLInputElement;

	function handleAddElection(): void {
		ElectionClient.create(topicField.value).then(election => {
			goto(`/elections/${election.id}`);
		});
	}
</script>

<h1>Elections</h1>

<ul>
	{#each data.elections as election}
		<li><a href="/elections/{election.id}"> {election.topic}</a></li>
	{/each}
</ul>

<h2>New Election</h2>
<input bind:this={topicField} />
<button onclick={handleAddElection}>Add</button>
