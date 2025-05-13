import type { ActionReturn } from "svelte/action";

export function clickOutside(
	node: HTMLElement,
	callback: () => void
): ActionReturn {
	const handleClick = (event: MouseEvent): void => {
		if (!node.contains(event.target as Node)) {
			callback();
		}
	};

	document.addEventListener("click", handleClick, true);

	return {
		destroy(): void {
			document.removeEventListener("click", handleClick, true);
		}
	};
}
