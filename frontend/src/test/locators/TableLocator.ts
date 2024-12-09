import { ByRoleLocator } from "./ElementLocator";

export class TableLocator extends ByRoleLocator<HTMLTableElement> {
	private constructor() {
		super("table");
	}

	public static any(): TableLocator {
		return new TableLocator();
	}

	public rows(): TableRowLocator {
		return this.find(TableRowLocator.any()) as TableRowLocator;
	}

	public row(index: number): TableRowLocator {
		return this.rows().nth(index) as TableRowLocator;
	}
}

export class TableRowLocator extends ByRoleLocator<HTMLTableRowElement> {
	private constructor() {
		super("row");
	}

	public static any(): TableRowLocator {
		return new TableRowLocator();
	}

	public cells(): TableCellLocator {
		return this.find(TableCellLocator.any()) as TableCellLocator;
	}

	public cell(index: number): TableCellLocator {
		return this.cells().nth(index) as TableCellLocator;
	}

	public headers(): TableHeaderLocator {
		return this.find(TableHeaderLocator.any()) as TableHeaderLocator;
	}

	public header(index: number): TableHeaderLocator {
		return this.headers().nth(index) as TableHeaderLocator;
	}
}

export class TableCellLocator extends ByRoleLocator<HTMLTableCellElement> {
	private constructor() {
		super("cell");
	}

	public static any(): TableCellLocator {
		return new TableCellLocator();
	}
}

export class TableHeaderLocator extends ByRoleLocator<HTMLTableCellElement> {
	private constructor() {
		super("columnheader");
	}

	public static any(): TableHeaderLocator {
		return new TableHeaderLocator();
	}
}
