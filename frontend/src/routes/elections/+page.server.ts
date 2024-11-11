import { ElectionClient, type Election } from '$lib/elections/ElectionClient';

export async function load(): Promise<{ elections: Array<Election> }> {
	return {
		elections: await ElectionClient.getAll()
	};
}
