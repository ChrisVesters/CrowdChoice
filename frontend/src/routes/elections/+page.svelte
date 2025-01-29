<script lang="ts">
	import { goto, invalidateAll } from "$app/navigation";
	import { t } from "$lib/translations/index";

	import { ElectionClient } from "$lib/election/ElectionClient";
	import ElectionTable from "$lib/election/ElectionTable.svelte";

	const { data } = $props();

	let newTopic: string = $state("");

	function isValidTopicField(): boolean {
		console.log(newTopic);
		return newTopic.length > 0;
	}

	function handleAddElection(): void {
		if (!isValidTopicField()) {
			return;
		}

		ElectionClient.create(newTopic).then(election => {
			goto(`/elections/${election.id}`);
		});
	}
</script>

<h1>{$t("common.overview")}</h1>
<h2>{$t("common.elections")}</h2>

<ElectionTable
	elections={data.elections}
	onChange={invalidateAll}
	onAdd={electionId => goto(`/elections/${electionId}`)}
/>