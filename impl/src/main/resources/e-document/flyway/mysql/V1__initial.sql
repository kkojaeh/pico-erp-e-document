create table edc_document (
	id binary(16) not null,
	content_length bigint not null,
	content_name varchar(100),
	content_type varchar(100),
	created_date datetime,
	creator_id varchar(50),
	name varchar(100),
	storage_key varchar(50),
	subject_id varchar(50),
	primary key (id)
) engine=InnoDB;

create table edc_document_subject (
	id varchar(50) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	name varchar(50),
	template longtext,
	primary key (id)
) engine=InnoDB;
