import { sveltekit } from "@sveltejs/kit/vite";
import { defineConfig } from "vitest/config";

export default defineConfig(({ mode }) => ({
	plugins: [sveltekit()],
	server: {
		open: true
	},
	test: {
		environment: "jsdom",
		globals: true
	},
	resolve: {
		conditions: mode === "test" ? ["browser"] : []
	}
}));
