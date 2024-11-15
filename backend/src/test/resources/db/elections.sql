INSERT INTO elections(id, topic)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Topics'),
	(2, 'Federal Elections 2024');

SELECT setval('elections_id_seq', (SELECT MAX(id) from "elections"));

INSERT INTO candidates(id, election_id, name)
OVERRIDING SYSTEM VALUE
VALUES
	(1, 1, 'Micronaut'),
	(2, 1, 'Docker'),
	(3, 1, 'Lombok'),
	(4, 2, 'Trump'),
	(5, 2, 'Biden');

SELECT setval('candidates_id_seq', (SELECT MAX(id) from "candidates"));