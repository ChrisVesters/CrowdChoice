<script lang="ts">
	import { goto, invalidateAll } from "$app/navigation";
	import { t } from '$lib/translations/index';

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

<h1>{$t("common.overview")}</h1>
<h2>{$t("common.elections")}</h2>
<ul>
	{#each data.elections as election}
		<li>
			<button onclick={() => handleRemoveElection(election.id)}>
				{$t("common.remove")}
			</button>
			<a href="/elections/{election.id}">{election.topic}</a>
		</li>
	{/each}
</ul>

<h3>{$t("common.newObject", { object: $t("common.election") })}</h3>
<input bind:this={topicField} />
<button onclick={handleAddElection}>{$t("common.add")}</button>
