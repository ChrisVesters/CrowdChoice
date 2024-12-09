import type ElementLocator from "$test/locators/ElementLocator";

export class ElementAssertion<T extends HTMLElement> {
	private locator: ElementLocator<T>;

	constructor(locator: ElementLocator<T>) {
		this.locator = locator;
	}

	public isInTheDocument(): void {
		expect(this.locator.get()).toBeDefined();
	}

	public hasLength(size: number): void {
		expect(this.locator.getAll()).toHaveLength(size);
	}

	public hasTextContent(text: string): void {
		expect(this.locator.get().textContent).toBe(text);
	}

	public isDisabled(): void {
		const element = this.locator.get();
		if (!(element instanceof HTMLButtonElement)) {
			throw new Error(
				`isDisabled is not supported on element '${element.tagName}'`
			);
		}
		expect(element.disabled).toBeTruthy();
	}

	public isEnabled(): void {
		const element = this.locator.get();
		if (!(element instanceof HTMLButtonElement)) {
			throw new Error(
				`isEnabled is not supported on element '${element.tagName}'`
			);
		}
		expect(element.disabled).toBeFalsy();
	}

	public isChecked(): void {
		const element = this.locator.get();
		if (!(element instanceof HTMLInputElement)) {
			throw new Error(
				`isChecked is not supported on element '${element.tagName}'`
			);
		}
		expect(element.checked).toBeTruthy();
	}

	public isNotChecked(): void {
		const element = this.locator.get();
		if (!(element instanceof HTMLInputElement)) {
			throw new Error(
				`isChecked is not supported on element '${element.tagName}'`
			);
		}
		expect(element.checked).toBeFalsy();
	}
}

export function assertThat<T extends HTMLElement>(
	locator: ElementLocator<T>
): ElementAssertion<T> {
	return new ElementAssertion(locator);
}
