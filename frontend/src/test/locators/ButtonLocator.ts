import { ByRoleLocator } from "./ElementLocator";

export class ButtonLocator extends ByRoleLocator<HTMLButtonElement> {
	private constructor(label: string) {
		super("button", label);
	}

	public static withLabel(label: string): ButtonLocator {
		return new ButtonLocator(label);
	}
}
