<script lang="ts">
	import { clickOutside } from "../action/ClickOutside";
	import type { ButtonAction } from "./ButtonTypes";

	export type DropdownButtonProps = {
		actions: Array<ButtonAction>;
	};

	let props: DropdownButtonProps = $props();
	let open: boolean = $state(false);

	if (props.actions.length == 0) {
		throw new Error("DropdownButton requires at least one action");
	}

	let defaultAction = props.actions[0];
	let secondaryActions = props.actions.slice(1);

	function toggleDropdown(): void {
		open = !open;
	}

	function closeDropdown(): void {
		open = false;
	}
</script>

<div class="dropdown" use:clickOutside={closeDropdown}>
	<div class="button-group">
		<button class="left" onclick={defaultAction.onClick}>
			{defaultAction.label}
		</button>
		<!-- Or CSS transform -->
		<button class="right" onclick={toggleDropdown}
			>{open ? "▲" : "▼"}</button
		>
	</div>

	{#if open}
		<div class="menu">
			{#each secondaryActions as action}
				<div
					class="menu-item"
					onclick={() => {
						action.onClick();
						closeDropdown();
					}}
				>
					{action.label}
				</div>
			{/each}
		</div>
	{/if}
</div>

<style>
	.dropdown {
		position: relative;
		display: inline-flex;
	}

	.button-group {
		position: relative;
		display: inline-flex;
		overflow: hidden;
	}

	button.left {
		border-top-right-radius: 0;
		border-bottom-right-radius: 0;
		border-right: 1px solid var(--text-color);
	}

	button.right {
		border-top-left-radius: 0;
		border-bottom-left-radius: 0;
	}

	.menu {
		position: absolute;
		top: 100%;
		background: white;
		border: 1px solid #ccc;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
		z-index: 1000;
	}

	.menu-item {
		padding: 0.5rem;
		cursor: pointer;
	}

	.menu-item:hover {
		background-color: #f1f1f1;
	}
</style>
