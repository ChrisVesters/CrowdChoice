<script lang="ts">
	import { goto, invalidateAll } from "$app/navigation";
	import { t } from "$lib/translations/index";

	import ElectionTable from "$lib/election/ElectionTable.svelte";

	const { data } = $props();
</script>

<h1>{$t("common.overview")}</h1>
<h2>{$t("common.elections")}</h2>

<ElectionTable
	elections={data.elections}
	onChange={invalidateAll}
	onAdd={electionId => goto(`/elections/${electionId}`)}
/>
