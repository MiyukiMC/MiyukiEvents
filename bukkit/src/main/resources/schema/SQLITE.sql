CREATE TABLE IF NOT EXISTS miyukievents_user
(
    id         INT PRIMARY KEY NOT NULL,
    uuid       VARCHAR(36)     NOT NULL,
    playername VARCHAR(20)     NOT NULL,
    totalmoney TEXT            NOT NULL,
    totalcash  TEXT            NOT NULL
);
CREATE INDEX IF NOT EXISTS miyukievents_user_uuid ON miyukievents_user (uuid);

CREATE TABLE IF NOT EXISTS miyukievents_userhistory
(

    id      INT PRIMARY KEY NOT NULL,
    uuid    VARCHAR(36)     NOT NULL,
    game    VARCHAR(200)    NOT NULL,
    wins    INT             NOT NULL,
    defeats INT             NOT NULL,
    kills   INT             NOT NULL,
    deaths  INT             NOT NULL,
    FOREIGN KEY (uuid) REFERENCES miyukievents_user (uuid)
);
CREATE INDEX IF NOT EXISTS miyukievents_userhistory_uuid ON miyukievents_userhistory (uuid);