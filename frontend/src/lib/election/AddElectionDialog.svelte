<script lang="ts">
	import { t } from "$lib/translations/index";
	
	export type AddElectionDialogProps = {
		onClose: () => void;
		onAdd: (topic: string) => void;
	};

	const props: AddElectionDialogProps = $props();

	let topicField: HTMLInputElement;

	function onload(elemnt: HTMLDialogElement): void {
		elemnt.showModal();
	}

	function addElection(): void {
		const topic = topicField.value.trim();
		if (topic.length == 0) {
			// TODO: set error class
			// topicField.classList.add("invalid");
			return;
		}

		props.onAdd(topic);
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
	</form>

	<div style:float="right">
		<button onclick={props.onClose}>{$t("common.cancel")}</button>
		<button onclick={addElection}>{$t("common.add")}</button>
	</div>
</dialog>
