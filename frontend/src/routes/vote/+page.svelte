<script lang="ts">
	import VoteForm from "$lib/vote/VoteForm.svelte";

	const { data } = $props();
</script>

<h1>{@html data.election.topic}</h1>
<div>
	{@html data.election.startedOn
		? new Date(data.election.startedOn).toLocaleString()
		: ""} - {@html data.election.endedOn
		? new Date(data.election.endedOn).toLocaleString()
		: ""}
</div>
<div>{@html data.election.description}</div>


<VoteForm electionId={data.election.id} candidates={data.candidates} />
