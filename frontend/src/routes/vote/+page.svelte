<script lang="ts">
	import VoteForm from "$lib/vote/VoteForm.svelte";

	const { data } = $props();
</script>

<h1>{@html data.election.topic}</h1>
<div>{@html data.election.description}</div>

<VoteForm electionId={data.election.id} candidates={data.candidates} />
