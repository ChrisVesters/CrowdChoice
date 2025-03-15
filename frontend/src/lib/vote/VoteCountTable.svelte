<script lang="ts">
	import { t } from "$lib/translations/index";

	import type { Candidate } from "$lib/candidate/CandidateTypes";

	import type { VoteCount } from "./VoteTypes";

	export type VoteCountTableprops = {
		electionId: number;
		candidates: Array<Candidate>;
		voteCounts: Array<VoteCount>;
	};

	const props: VoteCountTableprops = $props();
</script>

<table>
	<thead>
		<tr>
			<th>{$t("common.name")}</th>
			<th>{$t("common.votes")}</th>
		</tr>
	</thead>

	<tbody>
		{#each props.voteCounts as voteCount}
			<tr>
				<td>
					{props.candidates.find(
						candidate => candidate.id == voteCount.candidateId
					)?.name}
				</td>
				<td> {voteCount.voteCount} </td>
			</tr>
		{/each}
	</tbody>
</table>
