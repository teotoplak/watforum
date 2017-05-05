# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table swtoauth_user (
  id                            bigserial not null,
  oauth_id                      varchar(255) not null,
  client                        integer not null,
  user_id                       bigint,
  constraint ck_swtoauth_user_client check (client in (0)),
  constraint pk_swtoauth_user primary key (id)
);

create table swtplace (
  id                            bigserial not null,
  google_id                     varchar(255),
  constraint uq_swtplace_google_id unique (google_id),
  constraint pk_swtplace primary key (id)
);

create table swtrating (
  id                            bigserial not null,
  swtplace                      bigint,
  rating                        integer,
  comment                       varchar(255),
  providing_housing             boolean,
  work_load                     integer,
  payment                       integer,
  work_position                 varchar(255),
  swtyear_id                    bigint,
  created_at                    timestamp,
  updated_at                    timestamp,
  constraint pk_swtrating primary key (id)
);

create table swtuser (
  id                            bigserial not null,
  username                      varchar(255),
  password                      varchar(255),
  email                         varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  profile_picture_url           varchar(255),
  country                       varchar(20),
  gender                        integer,
  birth                         timestamp,
  living_location               varchar(255),
  constraint ck_swtuser_gender check (gender in (0,1)),
  constraint uq_swtuser_username unique (username),
  constraint uq_swtuser_email unique (email),
  constraint pk_swtuser primary key (id)
);

create table swtyear (
  id                            bigserial not null,
  year                          integer,
  agency                        varchar(255),
  user_id                       bigint,
  constraint pk_swtyear primary key (id)
);

alter table swtoauth_user add constraint fk_swtoauth_user_user_id foreign key (user_id) references swtuser (id) on delete restrict on update restrict;
create index ix_swtoauth_user_user_id on swtoauth_user (user_id);

alter table swtrating add constraint fk_swtrating_swtplace foreign key (swtplace) references swtplace (id) on delete restrict on update restrict;
create index ix_swtrating_swtplace on swtrating (swtplace);

alter table swtrating add constraint fk_swtrating_swtyear_id foreign key (swtyear_id) references swtyear (id) on delete restrict on update restrict;
create index ix_swtrating_swtyear_id on swtrating (swtyear_id);

alter table swtyear add constraint fk_swtyear_user_id foreign key (user_id) references swtuser (id) on delete restrict on update restrict;
create index ix_swtyear_user_id on swtyear (user_id);


# --- !Downs

alter table if exists swtoauth_user drop constraint if exists fk_swtoauth_user_user_id;
drop index if exists ix_swtoauth_user_user_id;

alter table if exists swtrating drop constraint if exists fk_swtrating_swtplace;
drop index if exists ix_swtrating_swtplace;

alter table if exists swtrating drop constraint if exists fk_swtrating_swtyear_id;
drop index if exists ix_swtrating_swtyear_id;

alter table if exists swtyear drop constraint if exists fk_swtyear_user_id;
drop index if exists ix_swtyear_user_id;

drop table if exists swtoauth_user cascade;

drop table if exists swtplace cascade;

drop table if exists swtrating cascade;

drop table if exists swtuser cascade;

drop table if exists swtyear cascade;

