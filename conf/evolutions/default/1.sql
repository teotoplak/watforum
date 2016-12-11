# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table rating (
  user_id                       bigint,
  watplace_id                   bigint,
  rating                        integer,
  rated_at                      timestamp
);

create table user (
  id                            bigint not null,
  username                      varchar(255),
  password                      varchar(255),
  country                       varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  birth                         timestamp,
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id)
);
create sequence user_seq;

create table wat_place (
  id                            bigint not null,
  google_id                     varchar(255),
  name                          varchar(255),
  phone_number                  varchar(255),
  address                       varchar(255),
  constraint uq_wat_place_google_id unique (google_id),
  constraint pk_wat_place primary key (id)
);
create sequence wat_place_seq;

alter table rating add constraint fk_rating_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_rating_user_id on rating (user_id);

alter table rating add constraint fk_rating_watplace_id foreign key (watplace_id) references wat_place (id) on delete restrict on update restrict;
create index ix_rating_watplace_id on rating (watplace_id);


# --- !Downs

alter table rating drop constraint if exists fk_rating_user_id;
drop index if exists ix_rating_user_id;

alter table rating drop constraint if exists fk_rating_watplace_id;
drop index if exists ix_rating_watplace_id;

drop table if exists rating;

drop table if exists user;
drop sequence if exists user_seq;

drop table if exists wat_place;
drop sequence if exists wat_place_seq;

