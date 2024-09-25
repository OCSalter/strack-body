CREATE DATABASE strack;
\c strack;

CREATE TABLE games (
    id uuid NOT NULL,
    PRIMARY KEY (id),
    name character varying NOT NULL
);

CREATE TABLE modes (
    id uuid NOT NULL,
    PRIMARY KEY (id),
    name character varying NOT NULL,
    num_teams integer NOT NULL,
    num_players integer NOT NULL
);

CREATE TABLE games_modes (
    game_id uuid NOT NULL,
    mode_id uuid NOT NULL
);

ALTER TABLE games_modes
ADD CONSTRAINT games_modes_id_games_id_modes PRIMARY KEY (game_id, mode_id);
ALTER TABLE games_modes
ADD FOREIGN KEY (game_id) REFERENCES games (id);
ALTER TABLE games_modes
ADD FOREIGN KEY (mode_id) REFERENCES modes (id);

CREATE TABLE users (
    id uuid NOT NULL,
    PRIMARY KEY (id),
    user_name character varying NOT NULL
);

CREATE TABLE groups (
    id uuid NOT NULL,
    PRIMARY KEY (id),
    group_name character varying NOT NULL
);

CREATE TABLE users_groups (
    user_id uuid NOT NULL,
    group_id uuid NOT NULL
);

CREATE TABLE matches (
    id uuid primary key default gen_random_uuid (),
    group_id uuid NOT NULL references groups(id),
    mode_id uuid NOT NULL references modes(id),
    match_time timestamp NOT NULL default current_timestamp
);

CREATE TABLE teams (
    id uuid NOT NULL,
    match_id uuid NOT NULL references matches(id),
    PRIMARY KEY (id)
);

CREATE TABLE players (
    id uuid NOT NULL,
    user_id uuid NOT NULL references users(id),
    team_id uuid NOT NULL references teams(id),
    match_id uuid NOT NULL references matches(id),
    PRIMARY KEY (id)
);

CREATE TABLE event_types (
    id uuid NOT NULL,
    event_name character varying NOT NULL
)

CREATE targeted_events (
    id uuid NOT NULL,
    type_id uuid NOT NULL references event_types(id),
    reference_id uuid NOT NULL,
    target_id uuid NOT NULL
)

CREATE general_events (
    id uuid NOT NULL,
    type_id uuid NOT NULL references event_types(id),
    reference_id uuid NOT NULL,
    event_value SMALLINT NOT NULL
)


        ---     /ENTRIES/     ---
INSERT INTO games (id, name)
VALUES ('fad82936-8ee4-4e7d-a9b4-19ba950ad526', 'Call of Duty: Modern Warfare 2');
INSERT INTO games (id, name)
VALUES ('0c43780d-ef1e-4fc0-9195-a067aa291848', 'Magic: The Gathering');
COMMIT;
--MTG
INSERT INTO modes (id, name, num_teams, num_players)
VALUES ('5ab8d027-e746-4884-b6ac-dcd5ed7662d4', 'EDH', 4, 4);
INSERT INTO modes (id, name, num_teams, num_players)
VALUES ('79b5eb1a-0efa-4c81-bab1-fc2b3793459c', 'Modern', 2, 2);
-- MW2
INSERT INTO modes (id, name, num_teams, num_players)
VALUES ('581740d3-fed3-40bc-9d09-1b61157f27c5', 'Capture The Flag', 2, 12);
INSERT INTO modes (id, name, num_teams, num_players)
VALUES ('27b90427-c1d3-4c36-a13d-a37a91bf016d', 'Search and Destroy', 2, 12);
COMMIT;


INSERT INTO users (id, user_name)
VALUES ('cb6e8fa5-050e-4f07-a90a-8f60237c425e','Big Guy');
INSERT INTO users (id, user_name)
VALUES ('4da7c162-2626-423d-8c64-189d9c43e64e','Lord_Jerry104');
INSERT INTO users (id, user_name)
VALUES ('8f9f4c7c-3a49-4dae-9a6a-e199c7b2a05d','Gabby Nightbringer');
INSERT INTO users (id, user_name)
VALUES ('aecaa879-2f4f-48d3-a5a7-79d9c9454b5d','Warlock Sam');
INSERT INTO users (id, user_name)
VALUES ('8e7de19a-25a1-4832-9c9c-b396abc2c60b','Princess 117');
INSERT INTO users (id, user_name)
VALUES ('06a96bd8-74ac-41c2-acba-1c084d15268d','Mazter Cheif');
INSERT INTO users (id, user_name)
VALUES ('5bd6d855-396f-429d-bf5c-0f149abb436b','grunt man');
INSERT INTO users (id, user_name)
VALUES ('9953b579-53d3-4bf1-8097-c54ba62509ab','goblin_main');
COMMIT;

INSERT INTO groups(id, group_name)
VALUES('5f7d61cb-dc11-4509-8b0f-454d0eec0674','Mega Gamer Group');
INSERT INTO groups(id, group_name)
VALUES('7e993503-cf82-4ae2-b9ca-1cba07cada4a','flower field');
INSERT INTO groups(id, group_name)
VALUES('29230b32-d9c6-477b-b1f3-7806e874498a','Gabby Zone');
COMMIT;

INSERT INTO users_groups(user_id, group_id) -- Gabby Nightbringer to Gabby Zone
VALUES('8f9f4c7c-3a49-4dae-9a6a-e199c7b2a05d','29230b32-d9c6-477b-b1f3-7806e874498a');
INSERT INTO users_groups(user_id, group_id) -- Big Guy to flower field
VALUES('cb6e8fa5-050e-4f07-a90a-8f60237c425e','7e993503-cf82-4ae2-b9ca-1cba07cada4a');
INSERT INTO users_groups(user_id, group_id) -- Princess 117 to flower field
VALUES('8e7de19a-25a1-4832-9c9c-b396abc2c60b','7e993503-cf82-4ae2-b9ca-1cba07cada4a');
INSERT INTO users_groups(user_id, group_id) -- Warlock Sam to flower field
VALUES('aecaa879-2f4f-48d3-a5a7-79d9c9454b5d','7e993503-cf82-4ae2-b9ca-1cba07cada4a');
INSERT INTO users_groups(user_id, group_id) -- goblin_main to flower field
VALUES('9953b579-53d3-4bf1-8097-c54ba62509ab','7e993503-cf82-4ae2-b9ca-1cba07cada4a');
INSERT INTO users_groups(user_id, group_id) -- Big Guy to Mega Gamer Group
VALUES('cb6e8fa5-050e-4f07-a90a-8f60237c425e','5f7d61cb-dc11-4509-8b0f-454d0eec0674');
INSERT INTO users_groups(user_id, group_id) -- Lord_Jerry104 to Mega Gamer Group
VALUES('4da7c162-2626-423d-8c64-189d9c43e64e','5f7d61cb-dc11-4509-8b0f-454d0eec0674');
INSERT INTO users_groups(user_id, group_id) -- Gabby Nightbringer to Mega Gamer Group
VALUES('8f9f4c7c-3a49-4dae-9a6a-e199c7b2a05d','5f7d61cb-dc11-4509-8b0f-454d0eec0674');
INSERT INTO users_groups(user_id, group_id) -- Warlock Sam to Mega Gamer Group
VALUES('aecaa879-2f4f-48d3-a5a7-79d9c9454b5d','5f7d61cb-dc11-4509-8b0f-454d0eec0674');
INSERT INTO users_groups(user_id, group_id) -- Princess 117 to Mega Gamer Group
VALUES('8e7de19a-25a1-4832-9c9c-b396abc2c60b','5f7d61cb-dc11-4509-8b0f-454d0eec0674');
INSERT INTO users_groups(user_id, group_id) -- Mazter Cheif to Mega Gamer Group
VALUES('06a96bd8-74ac-41c2-acba-1c084d15268d','5f7d61cb-dc11-4509-8b0f-454d0eec0674');
INSERT INTO users_groups(user_id, group_id) -- grunt man to Mega Gamer Group
VALUES('5bd6d855-396f-429d-bf5c-0f149abb436b','5f7d61cb-dc11-4509-8b0f-454d0eec0674');
COMMIT;


INSERT INTO matches(id, group_id, mode_id)
VALUES('c45493a8-e220-4b62-a129-520d822d0f65','7e993503-cf82-4ae2-b9ca-1cba07cada4a','27b90427-c1d3-4c36-a13d-a37a91bf016d');
INSERT INTO matches(id, group_id, mode_id)
VALUES('546cc6d6-2d9d-43b2-bcce-d8e32c40808d','7e993503-cf82-4ae2-b9ca-1cba07cada4a','581740d3-fed3-40bc-9d09-1b61157f27c5');
INSERT INTO matches(id, group_id, mode_id)
VALUES('447946c1-5e94-4836-bea7-8e64288ed580','7e993503-cf82-4ae2-b9ca-1cba07cada4a','27b90427-c1d3-4c36-a13d-a37a91bf016d');
INSERT INTO matches(id, group_id, mode_id)
VALUES('b16058b2-66bb-4b56-818b-c64e9af38340','5f7d61cb-dc11-4509-8b0f-454d0eec0674','581740d3-fed3-40bc-9d09-1b61157f27c5');
COMMIT;


INSERT INTO teams(id, match_id)
VALUES('4b08ebad-195f-4b1a-9a18-c9c7d72c6c13','c45493a8-e220-4b62-a129-520d822d0f65');
INSERT INTO teams(id, match_id)
VALUES('918d6341-1042-443b-99ac-8c617d0b73c4','c45493a8-e220-4b62-a129-520d822d0f65');
INSERT INTO teams(id, match_id)
VALUES('cbee2cfb-5ea4-4fdc-95be-e05e1be73988','546cc6d6-2d9d-43b2-bcce-d8e32c40808d');
INSERT INTO teams(id, match_id)
VALUES('167d6447-b2f4-46e7-a8d7-00a02b6fd30f','546cc6d6-2d9d-43b2-bcce-d8e32c40808d');
INSERT INTO teams(id, match_id)
VALUES('937d7de1-189f-41e1-beb6-0edd49165fb7','447946c1-5e94-4836-bea7-8e64288ed580');
INSERT INTO teams(id, match_id)
VALUES('95d1bb83-3ad0-480a-a6a1-8d86a51958a2','447946c1-5e94-4836-bea7-8e64288ed580');
INSERT INTO teams(id, match_id)
VALUES('657437dd-8321-42b8-bb73-d4a18bfb167a','b16058b2-66bb-4b56-818b-c64e9af38340');
INSERT INTO teams(id, match_id)
VALUES('3eb6289c-b46b-4e79-9dd9-51998325504c','b16058b2-66bb-4b56-818b-c64e9af38340');
COMMIT;


INSERT INTO players(id, user_id, team_id, match_id)
VALUES('10889008-aea5-4c10-bdc9-9f14ae0ac5e5','cb6e8fa5-050e-4f07-a90a-8f60237c425e','4b08ebad-195f-4b1a-9a18-c9c7d72c6c13','c45493a8-e220-4b62-a129-520d822d0f65');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('936a9b7b-4541-48b6-827f-18b9d6100fa9','8e7de19a-25a1-4832-9c9c-b396abc2c60b','4b08ebad-195f-4b1a-9a18-c9c7d72c6c13','c45493a8-e220-4b62-a129-520d822d0f65');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('e48632fe-1260-42f0-bfa3-b20845e07ab6','aecaa879-2f4f-48d3-a5a7-79d9c9454b5d','918d6341-1042-443b-99ac-8c617d0b73c4','c45493a8-e220-4b62-a129-520d822d0f65');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('f4fe3487-c8a7-412f-a5f4-e55353ca84ff','5bd6d855-396f-429d-bf5c-0f149abb436b','918d6341-1042-443b-99ac-8c617d0b73c4','c45493a8-e220-4b62-a129-520d822d0f65');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('1470950b-bcc7-4f3d-bc97-c214353f9803','cb6e8fa5-050e-4f07-a90a-8f60237c425e','cbee2cfb-5ea4-4fdc-95be-e05e1be73988','546cc6d6-2d9d-43b2-bcce-d8e32c40808d');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('7922febb-96ab-4602-b10d-6f568a845092','8e7de19a-25a1-4832-9c9c-b396abc2c60b','cbee2cfb-5ea4-4fdc-95be-e05e1be73988','546cc6d6-2d9d-43b2-bcce-d8e32c40808d');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('e54a09a0-28d5-49bf-954d-6941008870e2','9953b579-53d3-4bf1-8097-c54ba62509ab','167d6447-b2f4-46e7-a8d7-00a02b6fd30f','546cc6d6-2d9d-43b2-bcce-d8e32c40808d');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('d9f29284-799d-4fa6-b6a6-9b8cc3c51ea9','06a96bd8-74ac-41c2-acba-1c084d15268d','167d6447-b2f4-46e7-a8d7-00a02b6fd30f','546cc6d6-2d9d-43b2-bcce-d8e32c40808d');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('511919aa-5372-40f1-b1af-ff8a9a42d46a','8f9f4c7c-3a49-4dae-9a6a-e199c7b2a05d','937d7de1-189f-41e1-beb6-0edd49165fb7','447946c1-5e94-4836-bea7-8e64288ed580');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('e7ff7536-da29-4b21-9270-67048f8c3aad','4da7c162-2626-423d-8c64-189d9c43e64e','937d7de1-189f-41e1-beb6-0edd49165fb7','447946c1-5e94-4836-bea7-8e64288ed580');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('e2cc7712-871d-46fe-abde-0047ca246311','aecaa879-2f4f-48d3-a5a7-79d9c9454b5d','95d1bb83-3ad0-480a-a6a1-8d86a51958a2','447946c1-5e94-4836-bea7-8e64288ed580');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('8eb1f015-f5e1-4fd5-88e2-3c0e7f98d2a7','9953b579-53d3-4bf1-8097-c54ba62509ab','95d1bb83-3ad0-480a-a6a1-8d86a51958a2','447946c1-5e94-4836-bea7-8e64288ed580');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('6a9c466b-8962-46c4-8f40-04570f96fe2f','cb6e8fa5-050e-4f07-a90a-8f60237c425e','657437dd-8321-42b8-bb73-d4a18bfb167a','b16058b2-66bb-4b56-818b-c64e9af38340');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('649f7bc3-766b-4546-a89f-9a80fcbb3cca','4da7c162-2626-423d-8c64-189d9c43e64e','657437dd-8321-42b8-bb73-d4a18bfb167a','b16058b2-66bb-4b56-818b-c64e9af38340');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('79fb4434-383e-4af8-9160-7fabc96786de','8e7de19a-25a1-4832-9c9c-b396abc2c60b','3eb6289c-b46b-4e79-9dd9-51998325504c','b16058b2-66bb-4b56-818b-c64e9af38340');
INSERT INTO players(id, user_id, team_id, match_id)
VALUES('98286fe7-37df-4c57-bfb7-47ef0b5b754b','9953b579-53d3-4bf1-8097-c54ba62509ab','3eb6289c-b46b-4e79-9dd9-51998325504c','b16058b2-66bb-4b56-818b-c64e9af38340');
COMMIT;