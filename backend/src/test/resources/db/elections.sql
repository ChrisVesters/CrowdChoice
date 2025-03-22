INSERT INTO elections(id, topic, description, started_on, ended_on)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Topics', '', null, null),
	(2, 'Federal Elections 2024', 'Choose a new president.', '2023-10-01 08:00:00 +00:00', '2023-10-01 18:00:00 +00:00');

SELECT setval('elections_id_seq', (SELECT MAX(id) from "elections"));

INSERT INTO candidates(id, election_id, name, description)
OVERRIDING SYSTEM VALUE
VALUES
	(1, 1, 'Micronaut', 'Getting rid of reflection'),
	(2, 1, 'Docker', 'Containers'),
	(3, 1, 'Lombok', 'No more boilerplate'),
	(4, 2, 'Trump', 'Republican'),
	(5, 2, 'Biden', 'Democrat');

SELECT setval('candidates_id_seq', (SELECT MAX(id) from "candidates"));