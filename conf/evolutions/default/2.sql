# --- !Ups

INSERT INTO swtuser (id,username, password, email) VALUES (0,'admin','admin','admin@wat.com');
INSERT INTO swtuser (id,username, password, email) VALUES (1,'Albert','Albert','albert@wat.com');
INSERT INTO swtuser (id,username, password, email) VALUES (2,'Gabriel','Gabriel','gabriel@wat.com');
create sequence swtuser_seq START WITH 3;

# --- !Downs

drop sequence swtuser_id_seq;