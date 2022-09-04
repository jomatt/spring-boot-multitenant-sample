CREATE TABLE IF NOT EXISTS tenant1.usr ("name" varchar PRIMARY KEY);
CREATE TABLE IF NOT EXISTS tenant2.usr ("name" varchar PRIMARY KEY);

INSERT INTO tenant1.usr VALUES ('alice');
INSERT INTO tenant1.usr VALUES ('alex');

INSERT INTO tenant2.usr VALUES ('bob');
INSERT INTO tenant2.usr VALUES ('bella');
