import { ByRoleLocator } from "./ElementLocator";

export class TextLocator extends ByRoleLocator<HTMLElement> {
	private constructor(role: string) {
		super(role);
	}

	public static heading(): TextLocator {
		return new TextLocator("heading");
	}
}
