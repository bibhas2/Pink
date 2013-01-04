drop table if exists DefectLog;
drop table if exists DefectComment;
drop table if exists Defect;
drop table if exists Project;
drop table if exists DefectState;
drop table if exists AppUser;

create table AppUser (
	id integer not null AUTO_INCREMENT,
	fullName char(128),
	email char(128) unique not null,
	password blob NOT NULL,
	
	primary key (id)
);

create table Project (
	id integer not null AUTO_INCREMENT,
	name char(128) not null,
	ownerId integer not null,
	
	PRIMARY KEY (id),
	constraint projectFK1 foreign key (ownerId)
		references AppUser (id)
);

create table DefectState (
	id char(35) not null,

	constraint statePK primary key (id)
);


create table Defect (
	id integer not null AUTO_INCREMENT,
	shortDescription varchar(1024) not null,
	longDescription varchar(3048) not null,
	ownerId integer not null,
	originatorId integer not null,
	stateId char(35) not null,
	projectId integer not null,
	severity integer not null,
	priority integer not null,

	constraint defectPK primary key (id),
	constraint defectFK1 foreign key (ownerId)
		references AppUser (id),
	constraint defectFK2 foreign key (originatorId)
		references AppUser (id),
	constraint defectFK3 foreign key (stateId)
		references DefectState (id),
	constraint defectFK4 foreign key (projectId)
		references Project (id)
);

create table DefectComment (
	id integer not null AUTO_INCREMENT,
	defectId integer not null,
	description varchar(3048) not null,
	userId integer not null,
	lastupdate timestamp not null,
	constraint attachmentPK primary key (id),
	constraint attachmentFK1 foreign key (defectId)
		references Defect (id) on delete cascade,
	constraint attachmentFK2 foreign key (userId)
		references AppUser (id)
);

create table DefectLog (
	id integer not null AUTO_INCREMENT,
	defectId integer not null,
	actorName varchar(256) not null,
	actionText char(128) not null,
	lastupdate timestamp not null,
	constraint statelogPK primary key (id),
	constraint statelogFK1 foreign key (defectId)
		references Defect (id) on delete cascade
);

insert into DefectState (id) values ('Open');
insert into DefectState (id) values ('Assigned');
insert into DefectState (id) values ('Completed');
insert into DefectState (id) values ('Returned');
insert into DefectState (id) values ('Accepted');
insert into DefectState (id) values ('Verified');
insert into DefectState (id) values ('Deferred');
insert into DefectState (id) values ('Cancelled');
insert into DefectState (id) values ('Rejected');

-- Default password is "admin"
insert into AppUser (fullName, email, password) values ('Administrator', 'admin@example.com', x'D033E22AE348AEB5660FC2140AEC35850C4DA997');
insert into AppUser (fullName, email, password) values ('John Doe', 'john.doe@example.com', x'D033E22AE348AEB5660FC2140AEC35850C4DA997');
-- Add a few sample projects
insert into Project (name, ownerId) values ('Public Web Site', (select id from AppUser where email='admin@example.com'));
insert into Project (name, ownerId) values ('Online Store', (select id from AppUser where email='admin@example.com'));
