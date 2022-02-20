CREATE TABLE IF NOT EXISTS miyukievents_user
(
    uuid       CHAR(36)    NOT NULL,
    playername VARCHAR(20) NOT NULL,
    totalmoney TEXT        NOT NULL,
    totalcash  TEXT        NOT NULL,
    PRIMARY KEY (uuid)
) CHARSET=utf8mb4 COLLATE utf8mb4_bin;


CREATE TABLE IF NOT EXISTS miyukievents_userhistory(
    uuid    CHAR(36)     NOT NULL,
    game    VARCHAR(200) NOT NULL,
    wins    INTEGER      NOT NULL,
    defeats INTEGER      NOT NULL,
    kills   INTEGER      NOT NULL,
    deaths  INTEGER      NOT NULL,
    FOREIGN KEY (uuid) REFERENCES miyukievents_user (uuid)
) CHARSET=utf8mb4 COLLATE utf8mb4_bin;
CREATE INDEX IF NOT EXISTS miyukievents_userhistory_game ON miyukievents_userhistory (game);