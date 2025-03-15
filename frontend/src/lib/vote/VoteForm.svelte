<script lang="ts">
	import type { Candidate } from "$lib/candidate/CandidateTypes";

	import { t } from "$lib/translations/index";
	import { VoteClient } from "./VoteClient";
	import type { CreateVoteRequest } from "./VoteTypes";

	export type VoteFormProps = {
		electionId: number;
		candidates: Array<Candidate>;
	};

	const props: VoteFormProps = $props();

	let selected: number | undefined = $state();
	let submitted: boolean = $state(false);

	function submitVote(): void {
		if (selected == undefined) {
			return;
		}

		const request: CreateVoteRequest = {
			candidateId: selected
		};
		VoteClient.create(props.electionId, request).then(
			() => (submitted = true)
		);
	}
</script>

{#each props.candidates as candidate}
	<div>
		<label for={candidate.id.toString()}>{@html candidate.name}</label>
		<input
			bind:group={selected}
			type="radio"
			name="candidate"
			value={candidate.id}
			disabled={submitted}
		/>
	</div>
{/each}
<button disabled={submitted} onclick={submitVote}>{$t("common.vote")}</button>
