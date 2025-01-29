<script lang="ts">
	import { t } from "$lib/translations/index";
	
	export type AddCandidateDialogProps = {
		onClose: () => void;
		onAdd: (name: string) => void;
	};

	const props: AddCandidateDialogProps = $props();

	let nameField: HTMLInputElement;

	function onload(elemnt: HTMLDialogElement): void {
		elemnt.showModal();
	}

	function addCandidate(): void {
		const name = nameField.value.trim();
		if (name.length == 0) {
			// TODO: set error class
			// nameField.classList.add("invalid");
			return;
		}

		props.onAdd(name);
	}
</script>

<dialog use:onload>
	<h3>{$t("common.addObject", { object: $t("common.cnadidate") })}</h3>

	<form>
		<label for="name">{$t("common.name")}</label>
		<input
			bind:this={nameField}
			type="text"
			id="name"
			name="name"
			required
		/>
	</form>

	<div style:float="right">
		<button onclick={props.onClose}>{$t("common.cancel")}</button>
		<button onclick={addCandidate}>{$t("common.add")}</button>
	</div>
</dialog>
