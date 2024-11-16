import { ElectionClient } from '$lib/election/ElectionClient';
import type { Election } from '$lib/election/ElectionTypes';

export async function load(): Promise<{ elections: Array<Election> }> {
	return {
		elections: await ElectionClient.getAll()
	};
}
