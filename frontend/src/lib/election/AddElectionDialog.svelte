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

	function onload(elemnt: HTMLDialogElement): void {
		elemnt.showModal();
	}

	function addElection(): void {
		const topic = topicField.value.trim();
		const description = descriptionField.value.trim();

		if (topic.length == 0) {
			// TODO: set error class
			// topicField.classList.add("invalid");
			return;
		}

		props.onAdd({
			topic,
			description
		});
	}
</script>

<dialog use:onload>
	<h3>{$t("common.addObject", { object: $t("common.election") })}</h3>

	<form>
		<label for="topic">{$t("common.topic")}</label>
		<input
			bind:this={topicField}
			type="text"
			id="topic"
			name="topic"
			required
		/>
		<label for="description">{$t("common.description")}</label>
		<textarea
			bind:this={descriptionField}
			id="description"
			name="description"
			rows="3"
		></textarea>
	</form>

	<div style:float="right">
		<button onclick={props.onClose}>{$t("common.cancel")}</button>
		<button onclick={addElection}>{$t("common.add")}</button>
	</div>
</dialog>
