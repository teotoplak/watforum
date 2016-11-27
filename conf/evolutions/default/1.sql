# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table course (
  id                            bigint not null,
  name                          varchar(255),
  constraint pk_course primary key (id)
);
create sequence course_seq;

create table enrollment (
  id                            bigint not null,
  course_id                     bigint,
  student_id                    bigint,
  grade                         integer,
  constraint pk_enrollment primary key (id)
);
create sequence enrollment_seq;

create table student (
  id                            bigint not null,
  name                          varchar(255),
  birth_date                    timestamp,
  stud_id                       bigint,
  picture                       varbinary(255),
  constraint pk_student primary key (id)
);
create sequence student_seq;

create table student_tag (
  student_id                    bigint not null,
  tag_id                        bigint not null,
  constraint pk_student_tag primary key (student_id,tag_id)
);

create table tag (
  id                            bigint not null,
  name                          varchar(255),
  constraint pk_tag primary key (id)
);
create sequence tag_seq;

alter table enrollment add constraint fk_enrollment_course_id foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_enrollment_course_id on enrollment (course_id);

alter table enrollment add constraint fk_enrollment_student_id foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_enrollment_student_id on enrollment (student_id);

alter table student_tag add constraint fk_student_tag_student foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_student_tag_student on student_tag (student_id);

alter table student_tag add constraint fk_student_tag_tag foreign key (tag_id) references tag (id) on delete restrict on update restrict;
create index ix_student_tag_tag on student_tag (tag_id);


# --- !Downs

alter table enrollment drop constraint if exists fk_enrollment_course_id;
drop index if exists ix_enrollment_course_id;

alter table enrollment drop constraint if exists fk_enrollment_student_id;
drop index if exists ix_enrollment_student_id;

alter table student_tag drop constraint if exists fk_student_tag_student;
drop index if exists ix_student_tag_student;

alter table student_tag drop constraint if exists fk_student_tag_tag;
drop index if exists ix_student_tag_tag;

drop table if exists course;
drop sequence if exists course_seq;

drop table if exists enrollment;
drop sequence if exists enrollment_seq;

drop table if exists student;
drop sequence if exists student_seq;

drop table if exists student_tag;

drop table if exists tag;
drop sequence if exists tag_seq;

