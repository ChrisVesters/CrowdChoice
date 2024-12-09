import { loadTranslations } from "$lib/translations";

export async function setupTranslations(locale: string = "en"): Promise<void> {
	await loadTranslations(locale);
}