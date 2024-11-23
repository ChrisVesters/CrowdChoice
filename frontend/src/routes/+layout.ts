import type { Load } from "@sveltejs/kit";
import { loadTranslations } from '$lib/translations';
import "../app.css";

export const load: Load = async ({ url }) => {
  const { pathname } = url;

  const initLocale = 'en'; // get from cookie, user session, ...

  await loadTranslations(initLocale, pathname); // keep this just before the `return`

  return {};
}