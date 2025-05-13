<script lang="ts">
	import { t } from "$lib/translations/index";
	import { fromFormInputFormat, toFormInputFormat } from "$util/DateUtils";

	import { ScheduleElectionRequest } from "./ElectionTypes";

	export type ScheduleElectionDialogProps = {
		startsOn: string | null;
		endsOn: string | null;

		onClose: () => void;
		onSchedule: (request: ScheduleElectionRequest) => void;
	};

	const props: ScheduleElectionDialogProps = $props();

	let startsOnField: HTMLInputElement;
	let endsOnField: HTMLInputElement;

	function onload(elemnt: HTMLDialogElement): void {
		startsOnField.value = toFormInputFormat(props.startsOn);
		endsOnField.value = toFormInputFormat(props.endsOn);

		elemnt.showModal();
	}

	function isStarted(): boolean {
		return props.startsOn != null && new Date(props.startsOn) <= new Date();
	}

	function addElection(): void {
		const startOn: string | null = isStarted()
			? props.startsOn
			: fromFormInputFormat(startsOnField.value);
		const endOn: string | null = fromFormInputFormat(endsOnField.value);

		props.onSchedule(new ScheduleElectionRequest(startOn, endOn));
	}
</script>

<dialog use:onload>
	<h3>{$t("common.scheduleObject", { object: $t("common.election") })}</h3>

	<form>
		<label for="startsOn">{$t("common.startsOn")}</label>
		<input
			bind:this={startsOnField}
			disabled={isStarted()}
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
		<button onclick={addElection}>{$t("common.schedule")}</button>
	</div>
</dialog>
