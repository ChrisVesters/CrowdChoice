<script lang="ts">
	import { t } from "$lib/translations/index";

	import type { CreateElectionRequest } from "./ElectionTypes";

	export type AddElectionDialogProps = {
		onClose: () => void;
		onAdd: (request: CreateElectionRequest) => void;
	};

	const props: AddElectionDialogProps = $props();

	let topicField: HTMLInputElement;
	let descriptionField: HTMLTextAreaElement;
	let startsOnField: HTMLInputElement;
	let endsOnField: HTMLInputElement;

	function onload(elemnt: HTMLDialogElement): void {
		elemnt.showModal();
	}

	function addElection(): void {
		const topic = topicField.value.trim();
		const description = descriptionField.value.trim();
		const startsOn = startsOnField.value;
		const endsOn = endsOnField.value;

		console.log(startsOn, endsOn);

		if (topic.length == 0) {
			// TODO: set error class
			// topicField.classList.add("invalid");
			return;
		}

		props.onAdd({
			topic,
			description,
			startedOn: new Date(startsOn).toISOString(),
			endedOn: new Date(endsOn).toISOString()
		});
	}
</script>

<dialog use:onload>
	<h3>{$t("common.addObject", { object: $t("common.election") })}</h3>

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
		<button onclick={addElection}>{$t("common.add")}</button>
	</div>
</dialog>
