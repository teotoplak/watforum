# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table rating (
  user_id                       bigint,
  watplace_id                   bigint,
  rating                        integer,
  rated_at                      timestamp
);

create table swtuser (
  id                            bigint not null,
  username                      varchar(255),
  password                      varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  contact                       varchar(255),
  email                         varchar(255),
  living_location               varchar(255),
  constraint uq_swtuser_username unique (username),
  constraint uq_swtuser_email unique (email),
  constraint pk_swtuser primary key (id)
);
create sequence swtuser_seq;

create table swtwork_place (
  id                            bigserial not null,
  name                          varchar(255),
  address                       varchar(255),
  phone_number                  varchar(255),
  icon                          varchar(255),
  created_at                    timestamp,
  updated_at                    timestamp,
  google_id                     varchar(255),
  weekday_text                  varchar(255),
  website                       varchar(255),
  google_maps                   varchar(255),
  constraint pk_swtwork_place primary key (id)
);

create table swtwork_rating (
  id                            bigserial not null,
  swtyear_id                    bigint,
  created_at                    timestamp,
  updated_at                    timestamp,
  swtworkplace_id               bigint,
  rating                        integer,
  rated_at                      timestamp,
  comment                       varchar(255),
  providing_housing             boolean,
  hours_per_week_work           integer,
  payment                       integer,
  constraint pk_swtwork_rating primary key (id)
);

create table swtyear (
  id                            bigserial not null,
  year                          integer,
  country                       varchar(255),
  agency                        varchar(255),
  user_id                       bigint,
  constraint pk_swtyear primary key (id)
);

create table wat_place (
  id                            bigint not null,
  google_id                     varchar(255),
  name                          varchar(255),
  phone_number                  varchar(255),
  address                       varchar(255),
  icon                          varchar(255),
  weekday_text                  varchar(255),
  website                       varchar(255),
  google_maps                   varchar(255),
  constraint uq_wat_place_google_id unique (google_id),
  constraint pk_wat_place primary key (id)
);
create sequence wat_place_seq;

create table wat_user (
  id                            bigint not null,
  username                      varchar(255),
  password                      varchar(255),
  country                       varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  birth                         timestamp,
  constraint uq_wat_user_username unique (username),
  constraint pk_wat_user primary key (id)
);
create sequence wat_user_seq;

alter table rating add constraint fk_rating_user_id foreign key (user_id) references wat_user (id) on delete restrict on update restrict;
create index ix_rating_user_id on rating (user_id);

alter table rating add constraint fk_rating_watplace_id foreign key (watplace_id) references wat_place (id) on delete restrict on update restrict;
create index ix_rating_watplace_id on rating (watplace_id);

alter table swtwork_rating add constraint fk_swtwork_rating_swtyear_id foreign key (swtyear_id) references swtyear (id) on delete restrict on update restrict;
create index ix_swtwork_rating_swtyear_id on swtwork_rating (swtyear_id);

alter table swtwork_rating add constraint fk_swtwork_rating_swtworkplace_id foreign key (swtworkplace_id) references swtwork_place (id) on delete restrict on update restrict;
create index ix_swtwork_rating_swtworkplace_id on swtwork_rating (swtworkplace_id);

alter table swtyear add constraint fk_swtyear_user_id foreign key (user_id) references swtuser (id) on delete restrict on update restrict;
create index ix_swtyear_user_id on swtyear (user_id);


# --- !Downs

alter table if exists rating drop constraint if exists fk_rating_user_id;
drop index if exists ix_rating_user_id;

alter table if exists rating drop constraint if exists fk_rating_watplace_id;
drop index if exists ix_rating_watplace_id;

alter table if exists swtwork_rating drop constraint if exists fk_swtwork_rating_swtyear_id;
drop index if exists ix_swtwork_rating_swtyear_id;

alter table if exists swtwork_rating drop constraint if exists fk_swtwork_rating_swtworkplace_id;
drop index if exists ix_swtwork_rating_swtworkplace_id;

alter table if exists swtyear drop constraint if exists fk_swtyear_user_id;
drop index if exists ix_swtyear_user_id;

drop table if exists rating cascade;

drop table if exists swtuser cascade;
drop sequence if exists swtuser_seq;

drop table if exists swtwork_place cascade;

drop table if exists swtwork_rating cascade;

drop table if exists swtyear cascade;

drop table if exists wat_place cascade;
drop sequence if exists wat_place_seq;

drop table if exists wat_user cascade;
drop sequence if exists wat_user_seq;

