# --- !Ups

SET REFERENTIAL_INTEGRITY TRUE ;

INSERT INTO user (id,username, password, country) VALUES (0,'admin','admin','Croatia');
INSERT INTO user (id,username, password, country) VALUES (1,'Albert','Albert','Spain');
INSERT INTO user (id,username, password, country) VALUES (2,'Gabriel','Gabriel','Italy');

drop sequence user_seq;
create sequence user_seq START WITH 3;

SET REFERENTIAL_INTEGRITY TRUE;