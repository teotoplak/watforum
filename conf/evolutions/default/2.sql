# --- !Ups

INSERT INTO wat_user (id,username, password, country) VALUES (0,'admin','admin','Croatia');
INSERT INTO wat_user (id,username, password, country) VALUES (1,'Albert','Albert','Spain');
INSERT INTO wat_user (id,username, password, country) VALUES (2,'Gabriel','Gabriel','Italy');

drop sequence user_seq;
create sequence user_seq START WITH 3;