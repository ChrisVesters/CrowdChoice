INSERT INTO votes(id, casted_on, candidate_id)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, '2024-12-01T8:30:00+00:00', '5'),
	(2, '2024-12-01T8:32:15+00:00', '4'),
	(3, '2024-12-01T8:33:01+00:00', '5');

SELECT setval('votes_id_seq', (SELECT MAX(id) from "votes"));