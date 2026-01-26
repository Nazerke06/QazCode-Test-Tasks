CREATE TABLE mc1entity
(
    id            INT      NOT NULL,
    session_id    INT      NULL,
    mc1timestamp  datetime NULL,
    mc2timestamp  datetime NULL,
    mc3timestamp  datetime NULL,
    end_timestamp datetime NULL,
    CONSTRAINT pk_mc1entity PRIMARY KEY (id)
);