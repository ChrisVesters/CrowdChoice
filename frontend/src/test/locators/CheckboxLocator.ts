import { ByRoleLocator } from "./ElementLocator";

export class CheckboxLocator extends ByRoleLocator<HTMLInputElement> {
	constructor() {
		super("checkbox");
	}

	public static any(): CheckboxLocator {
		return new CheckboxLocator();
	}
}
