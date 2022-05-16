
----------------------TABLE ACTION_LIST----------------------------
CREATE TABLE action_list (
    action_id INTEGER NOT NULL,
    name      VARCHAR2(60) NOT NULL,
    deleted   NUMBER(2) NOT NULL
);

ALTER TABLE action_list ADD CONSTRAINT action_list_pk PRIMARY KEY ( action_id );

CREATE SEQUENCE action_list_seq START WITH 1;

CREATE OR REPLACE TRIGGER action_list_id_gen
BEFORE INSERT ON action_list
FOR EACH ROW
BEGIN
  SELECT action_list_seq.NEXTVAL
  INTO   :new.action_id
  FROM   dual;
END;
/



-------------------------------TABLE LOGGING------------------------
CREATE TABLE logging (
    log_id                INTEGER NOT NULL,
    log_time              TIMESTAMP NOT NULL,
    users_user_id         INTEGER NOT NULL,
	task_id 			  INTEGER,
    deleted               NUMBER(2) NOT NULL,
    action_list_action_id INTEGER NOT NULL
);

ALTER TABLE logging ADD CONSTRAINT logging_pk PRIMARY KEY ( log_id );

CREATE SEQUENCE logging_seq START WITH 1;

CREATE OR REPLACE TRIGGER logging_id_gen
BEFORE INSERT ON logging
FOR EACH ROW
BEGIN
  SELECT logging_seq.NEXTVAL
  INTO   :new.log_id
  FROM   dual;
END;
/

----------------------------------TABLE PRIORITY--------------------
CREATE TABLE priority (
    priority_id INTEGER NOT NULL,
    name        VARCHAR2(60) NOT NULL,
    deleted     NUMBER(2) NOT NULL
);

ALTER TABLE priority ADD CONSTRAINT priority_pk PRIMARY KEY ( priority_id );

CREATE SEQUENCE priority_seq START WITH 1;

CREATE OR REPLACE TRIGGER priority_id_gen
BEFORE INSERT ON priority
FOR EACH ROW
BEGIN
  SELECT priority_seq.NEXTVAL
  INTO   :new.priority_id
  FROM   dual;
END;
/



-------------------------------------------TABLE STATUS----------------------------
CREATE TABLE status (
    status_id INTEGER NOT NULL,
    name      VARCHAR2(60) NOT NULL,
    deleted   NUMBER(2) NOT NULL
);

ALTER TABLE status ADD CONSTRAINT status_pk PRIMARY KEY ( status_id );

CREATE SEQUENCE status_seq START WITH 1;

CREATE OR REPLACE TRIGGER status_id_gen
BEFORE INSERT ON status 
FOR EACH ROW
BEGIN
  SELECT status_seq.NEXTVAL
  INTO   :new.status_id
  FROM   dual;
END;
/




---------------------------------------------TABLE TASKS------------------------------
CREATE TABLE tasks (
    task_id              INTEGER NOT NULL,
    name                 VARCHAR2(60) NOT NULL,
    description          CLOB NOT NULL,
    responsible_id       INTEGER NOT NULL,
    verifying_id         INTEGER,
    priority_priority_id INTEGER NOT NULL,
    status_status_id     INTEGER NOT NULL,
	deadline			 VARCHAR(20) NOT NULL,
    deleted              NUMBER(2) NOT NULL
);

ALTER TABLE tasks ADD CONSTRAINT tasks_pk PRIMARY KEY ( task_id );

CREATE SEQUENCE task_seq START WITH 1;

CREATE OR REPLACE TRIGGER task_id_gen
BEFORE INSERT ON tasks 
FOR EACH ROW
BEGIN
  SELECT task_seq.NEXTVAL
  INTO   :new.task_id
  FROM   dual;
END;
/

alter table tasks modify status_status_id default 1;

-------------------------------------------USERS-------------------------------------
CREATE TABLE users (
    user_id  INTEGER NOT NULL,
    login    VARCHAR2(60) NOT NULL,
    password VARCHAR2(60) NOT NULL,
    deleted  NUMBER(2) NOT NULL
);

ALTER TABLE users ADD CONSTRAINT users_pk PRIMARY KEY ( user_id );

ALTER TABLE users ADD CONSTRAINT users__un UNIQUE ( login );

CREATE SEQUENCE user_seq START WITH 1;

CREATE OR REPLACE TRIGGER user_id_gen
BEFORE INSERT ON users 
FOR EACH ROW
BEGIN
  SELECT user_seq.NEXTVAL
  INTO   :new.user_id
  FROM   dual;
END;
/

---------------------------------------------END TABLE CREATION-------------------------------------




ALTER TABLE logging
    ADD CONSTRAINT logging_action_list_fk FOREIGN KEY ( action_list_action_id )
        REFERENCES action_list ( action_id );

ALTER TABLE logging
    ADD CONSTRAINT logging_users_fk FOREIGN KEY ( users_user_id )
        REFERENCES users ( user_id );

ALTER TABLE tasks
    ADD CONSTRAINT tasks_priority_fk FOREIGN KEY ( priority_priority_id )
        REFERENCES priority ( priority_id );

ALTER TABLE tasks
    ADD CONSTRAINT tasks_status_fk FOREIGN KEY ( status_status_id )
        REFERENCES status ( status_id );

ALTER TABLE tasks
    ADD CONSTRAINT tasks_users_fk FOREIGN KEY ( responsible_id )
        REFERENCES users ( user_id );

ALTER TABLE tasks
    ADD CONSTRAINT tasks_users_fkv2 FOREIGN KEY ( verifying_id )
        REFERENCES users ( user_id );

ALTER TABLE logging modify deleted default 0;
ALTER TABLE users modify deleted default 0;
ALTER TABLE tasks modify deleted default 0;
ALTER TABLE priority modify deleted default 0;
ALTER TABLE action_list modify deleted default 0;
ALTER TABLE status modify deleted default 0;

ALTER TABLE logging modify log_time default SYSDATE;


Insert into tasks (name, description, responsible_id, verifying_id, priority_priority_id, deadline) 
values ('Дизайн', 'Создать паттерн для обложки видео', 2, 1, 3, '11.07.2022');

Insert into tasks (name, description, responsible_id, verifying_id, priority_priority_id, deadline) 
values ('Копирайтинг', 'Написать 10 постов на тему путешествий', 1, 3, 2, '6.06.2022');

Insert into tasks (name, description, responsible_id, verifying_id, priority_priority_id, deadline) 
values ('Презентация', 'Презентовать заказчику дизайн сайта', 1, 2, 1, '3.08.2022');


