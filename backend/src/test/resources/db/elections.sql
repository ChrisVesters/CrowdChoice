INSERT INTO elections(id, topic)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Topics'),
	(2, 'Federal Elections 2024');

SELECT setval('elections_id_seq', (SELECT MAX(id) from "elections"));

