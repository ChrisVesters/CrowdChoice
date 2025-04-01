<script lang="ts">
	import { t } from "$lib/translations/index";

	import type { Election, UpdateElectionRequest } from "./ElectionTypes";

	import { fromFormInputFormat, toFormInputFormat } from "$util/DateUtils";

	export type UpdateElectionDialogProps = {
		election: Election;
		onClose: () => void;
		onUpdate: (electionId: number, request: UpdateElectionRequest) => void;
	};

	const props: UpdateElectionDialogProps = $props();

	let topicField: HTMLInputElement;
	let descriptionField: HTMLTextAreaElement;
	let startsOnField: HTMLInputElement;
	let endsOnField: HTMLInputElement;

	function onload(element: HTMLDialogElement): void {
		topicField.value = props.election.topic;
		descriptionField.value = props.election.description;
		startsOnField.value = toFormInputFormat(props.election.startedOn);
		endsOnField.value = toFormInputFormat(props.election.endedOn);

		element.showModal();
	}

	function updateElection(): void {
		const topic = topicField.value.trim();
		const description = descriptionField.value.trim();
		const startsOn = startsOnField.value;
		const endsOn = endsOnField.value;

		if (topic.length == 0) {
			// TODO: set error class
			// topicField.classList.add("invalid");
			return;
		}

		props.onUpdate(props.election.id, {
			topic,
			description,
			startedOn: fromFormInputFormat(startsOn),
			endedOn: fromFormInputFormat(endsOn)
		});
	}
</script>

<dialog use:onload>
	<h3>{$t("common.updateObject", { object: $t("common.election") })}</h3>

	<form>
		<label for="topic">{$t("common.topic")}</label>
		<input
			bind:this={topicField}
			class="fullWidth"
			type="text"
			id="topic"
			name="topic"
			required
		/>
		<label for="description">{$t("common.description")}</label>
		<textarea
			bind:this={descriptionField}
			class="fullWidth"
			id="description"
			name="description"
			rows="3"
		></textarea>
		<label for="startsOn">{$t("common.startsOn")}</label>
		<input
			bind:this={startsOnField}
			class="fullWidth"
			type="datetime-local"
			id="startsOn"
			name="startsOn"
		/>

		<label for="endsOn">{$t("common.endsOn")}</label>
		<input
			bind:this={endsOnField}
			class="fullWidth"
			type="datetime-local"
			id="endsOn"
			name="endsOn"
		/>
	</form>

	<div style:float="right">
		<button onclick={props.onClose}>{$t("common.cancel")}</button>
		<button onclick={updateElection}>{$t("common.update")}</button>
	</div>
</dialog>
