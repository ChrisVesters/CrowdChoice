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
		{#each props.candidates as candidate}
			<tr>
				<td>
					{candidate.name}
				</td>
				<td>
					{props.voteCounts.find(
						voteCount => voteCount.candidateId == candidate.id
					)?.voteCount ?? 0}
				</td>
			</tr>
		{/each}
	</tbody>
</table>
