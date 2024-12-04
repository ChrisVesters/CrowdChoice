INSERT INTO elections(id, topic)
OVERRIDING SYSTEM VALUE
VALUES 
	(1, 'Topics'),
	(2, 'Federal Elections 2024');

SELECT setval('elections_id_seq', (SELECT MAX(id) from "elections"));

INSERT INTO candidates(id, election_id, name, description)
OVERRIDING SYSTEM VALUE
VALUES
	(1, 1, 'Micronaut', 'Eliminate reflection'),
	(2, 1, 'Docker', 'Containers'),
	(3, 1, 'Lombok', 'No more boilerplate'),
	(4, 2, 'Trump', 'Make America Great Again'),
	(5, 2, 'Biden', 'I forgot what to say');

SELECT setval('candidates_id_seq', (SELECT MAX(id) from "candidates"));