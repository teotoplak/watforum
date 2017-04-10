# --- !Ups

INSERT INTO swtuser (id,username, password) VALUES (0,'admin','admin');
INSERT INTO swtuser (id,username, password) VALUES (1,'Albert','Albert');
INSERT INTO swtuser (id,username, password) VALUES (2,'Gabriel','Gabriel');

drop sequence user_seq;
create sequence user_seq START WITH 3;