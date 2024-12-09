import { getAllByRole, getByRole, screen } from "@testing-library/svelte";

export default abstract class ElementLocator<T extends HTMLElement> {
	protected parent?: ElementLocator<HTMLElement>;
	protected index?: number;

	public within<P extends HTMLElement>(
		locator: ElementLocator<P>
	): ElementLocator<T> {
		this.parent = locator;
		return this;
	}

	public find<C extends HTMLElement>(
		locator: ElementLocator<C>
	): ElementLocator<C> {
		locator.within(this);
		return locator;
	}

	public nth(index: number): ElementLocator<T> {
		this.index = index;
		return this;
	}

	public get(): T {
		if (this.index !== undefined) {
			const elements = this.doGetAll();
			const element = elements.at(this.index);
			if (element === undefined) {
				throw new Error(`No element at index ${this.index}`);
			}
			return element;
		} else {
			return this.doGet();
		}
	}

	public getAll(): Array<T> {
		const elements = this.doGetAll();
		if (this.index !== undefined) {
			const element = elements.at(this.index);
			if (element === undefined) {
				throw new Error(`No element at index ${this.index}`);
			}
			return [element];
		} else {
			return elements;
		}
	}

	protected abstract doGet(): T;

	protected abstract doGetAll(): Array<T>;
}

export class ByRoleLocator<T extends HTMLElement> extends ElementLocator<T> {
	private role: string;
	private label?: string;

	constructor(role: string, label?: string) {
		super();
		this.role = role;
		this.label = label;
	}

	protected doGet(): T {
		if (this.parent !== undefined) {
			return getByRole(this.parent.get(), this.role, {
				name: this.label
			});
		} else {
			return screen.getByRole(this.role, { name: this.label });
		}
	}

	protected doGetAll(): Array<T> {
		if (this.parent !== undefined) {
			return getAllByRole(this.parent.get(), this.role, {
				name: this.label
			});
		} else {
			return screen.getAllByRole(this.role, { name: this.label });
		}
	}
}
