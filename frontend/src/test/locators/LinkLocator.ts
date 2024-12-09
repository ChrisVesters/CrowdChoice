import { ByRoleLocator } from "./ElementLocator";

export class LinkLocator extends ByRoleLocator<HTMLAnchorElement> {
	public constructor(label?: string) {
		super("link", label);
	}

	public static withLabel(label: string): LinkLocator {
		return new LinkLocator(label);
	}

	public static any(): LinkLocator {
		return new LinkLocator();
	}
}