<script lang="ts">
	import { invalidateAll } from "$app/navigation";
	import { t } from "$lib/translations/index";

	import CandidateTable from "$lib/candidate/CandidateTable.svelte";

	const { data } = $props();
</script>

<h1>{@html data.info.topic}</h1>
<h2>{$t("common.candidates")}</h2>

<CandidateTable
	electionId={data.info.id}
	candidates={data.candidates}
	onChange={invalidateAll}
	onAdd={invalidateAll}
/>
