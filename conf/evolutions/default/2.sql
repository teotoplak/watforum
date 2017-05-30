# --- !Ups

INSERT INTO swtuser (id,username, password, email, anonymous) VALUES (0,'admin','admin','admin@wat.com', 'FALSE' );
INSERT INTO swtuser (id,username, password, email, anonymous) VALUES (1,'Albert','Albert','albert@wat.com', 'FALSE');
INSERT INTO swtuser (id,username, password, email, anonymous) VALUES (2,'Gabriel','Gabriel','gabriel@wat.com', 'FALSE');
create sequence swtuser_seq START WITH 3;

# --- !Downs

drop sequence swtuser_id_seq;