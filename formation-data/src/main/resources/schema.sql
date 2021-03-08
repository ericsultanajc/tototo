drop table if exists SKILL;

drop table if exists SUBJECT;

drop table if exists PERSON;

drop table if exists ADRESS;

/*==============================================================*/
/* Table : ADRESS                                               */
/*==============================================================*/
create table ADRESS
(
   ID                   integer not null AUTO_INCREMENT,
   STREET               varchar(255),
   COMPLEMENT           varchar(255),
   ZIPCODE              varchar(10),
   CITY                 varchar(100),
   COUNTRY              varchar(100),
   primary key (ID)
);

/*==============================================================*/
/* Table : PERSON                                               */
/*==============================================================*/
create table PERSON
(
   ID                   integer not null AUTO_INCREMENT,
   TYPE             	varchar(1) not null,
   CIVILITY             varchar(10),
   LASTNAME             varchar(255),
   FIRSTNAME            varchar(255),
   EMAIL                varchar(255),
   BIRTHDATE            date,
   HIREDATE             date,
   EXPERIENCE           integer,
   INTERNE              boolean,
   ADRESS_ID            integer,
   TRAINER_ID           integer,
   primary key (ID)
);

/*==============================================================*/
/* Table : SKILL                                                */
/*==============================================================*/
create table SKILL
(
   TRAINER_ID           integer not null,
   SUBJECT_ID           integer not null,
   primary key (TRAINER_ID, SUBJECT_ID)
);

/*==============================================================*/
/* Table : SUBJECT                                              */
/*==============================================================*/
create table SUBJECT
(
   ID                   integer not null AUTO_INCREMENT,
   NAME                 varchar(100),
   DURATION             integer,
   DIFFICULTY           varchar(20),
   primary key (ID)
);

alter table PERSON add constraint FK_PERSON_ADRESS_ID foreign key (ADRESS_ID)
      references ADRESS (ID);

alter table PERSON add constraint FK_PERSON_TRAINER_ID foreign key (TRAINER_ID)
      references PERSON (ID);

alter table SKILL add constraint FK_SKILL_SUBJECT_ID foreign key (SUBJECT_ID)
      references SUBJECT (ID);

alter table SKILL add constraint FK_SKILL_TRAINER_ID foreign key (TRAINER_ID)
      references PERSON (ID);

