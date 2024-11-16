<script lang="ts">
	import { goto } from "$app/navigation";
	
	import { ElectionClient } from "$lib/election/ElectionClient";

	const { data } = $props();

	let topicField: HTMLInputElement;

	function handleAddElection(): void {
		ElectionClient.create(topicField.value).then(election => {
			goto(`/elections/${election.id}`);
		});
	}
</script>

<h1>Overview</h1>
<h2>Elections</h2>
<ul>
	{#each data.elections as election}
		<li><a href="/elections/{election.id}"> {election.topic}</a></li>
	{/each}
</ul>

<h3>New Election</h3>
<input bind:this={topicField} />
<button onclick={handleAddElection}>Add</button>
