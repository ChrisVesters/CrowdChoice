export function toFormInputFormat(dateValue: string | null): string {
	if (!dateValue) {
		return "";
	}
	
	const date = new Date(dateValue);

	const year = date.getFullYear().toString();
	const month = (date.getMonth() + 1).toString().padStart(2, '0');
	const day = date.getDate().toString().padStart(2, '0');

	const hours = date.getHours().toString().padStart(2, '0');
	const minutes = date.getMinutes().toString().padStart(2, '0');


	return `${year}-${month}-${day}T${hours}:${minutes}`;
}

export function fromFormInputFormat(dateValue: string): string | null {
	if (dateValue === "") {
		return null;
	}
	
	return new Date(dateValue).toISOString();
}