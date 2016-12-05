# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user (
  id                            bigint not null,
  username                      varchar(255),
  password                      varchar(255),
  country                       varchar(255),
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id)
);
create sequence user_seq;

create table wat_place (
  google_id                     varchar(255) not null,
  name                          varchar(255),
  phone_number                  varchar(255),
  address                       varchar(255),
  constraint pk_wat_place primary key (google_id)
);


# --- !Downs

drop table if exists user;
drop sequence if exists user_seq;

drop table if exists wat_place;

