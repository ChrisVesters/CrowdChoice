<script lang="ts">
	import { t } from "$lib/translations/index";

	import type { Candidate, UpdateCandidateRequest } from "./CandidateTypes";

	export type AddCandidateDialogProps = {
		candidate: Candidate;
		onClose: () => void;
		onUpdate: (
			candidateId: number,
			request: UpdateCandidateRequest
		) => void;
	};

	const props: AddCandidateDialogProps = $props();

	let nameField: HTMLInputElement;
	let descriptionField: HTMLTextAreaElement;

	function onload(element: HTMLDialogElement): void {
		nameField.value = props.candidate.name;
		descriptionField.value = props.candidate.description;

		element.showModal();
	}

	function addCandidate(): void {
		const name = nameField.value.trim();
		const description = descriptionField.value.trim();

		if (name.length == 0) {
			// TODO: set error class
			// nameField.classList.add("invalid");
			return;
		}

		props.onUpdate(props.candidate.id, {
			name,
			description
		});
	}
</script>

<dialog use:onload>
	<h3>{$t("common.addObject", { object: $t("common.candidate") })}</h3>

	<form>
		<label for="name">{$t("common.name")}</label>
		<input
			bind:this={nameField}
			class="fullWidth"
			type="text"
			id="name"
			name="name"
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
	</form>

	<div style:float="right">
		<button onclick={props.onClose}>{$t("common.cancel")}</button>
		<button onclick={addCandidate}>{$t("common.add")}</button>
	</div>
</dialog>
