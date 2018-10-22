
    create table operations (
       id number(19,0) not null,
        name varchar2(45 char) not null,
        primary key (id)
    );

    create table role_operation (
       role_id number(19,0) not null,
        operation_id number(19,0) not null
    );

    create table roles (
       id number(19,0) not null,
        name varchar2(45 char) not null,
        primary key (id)
    );

    create table SEQUENCE_TABLE (
       SEQ_NAME varchar2(255 char) not null,
        SEQ_COUNT number(19,0),
        primary key (SEQ_NAME)
    );

    insert into SEQUENCE_TABLE(SEQ_NAME, SEQ_COUNT) values ('USER_SEQ',0);

    insert into SEQUENCE_TABLE(SEQ_NAME, SEQ_COUNT) values ('ROLE_SEQ',0);

    insert into SEQUENCE_TABLE(SEQ_NAME, SEQ_COUNT) values ('OPERATION_SEQ',0);

    create table user_role (
       user_id number(19,0) not null,
        role_id number(19,0) not null
    );

    create table users (
       id number(19,0) not null,
        email varchar2(255 char) not null,
        enabled number(1,0),
        firstname varchar2(255 char),
        lastname varchar2(255 char),
        password varchar2(255 char),
        primary key (id)
    );

    alter table users 
       add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table role_operation 
       add constraint FK2vfefbuhg00mfg90w0ak0041i 
       foreign key (operation_id) 
       references operations;

    alter table role_operation 
       add constraint FK88ndygeokny3rrbchw50j9ahu 
       foreign key (role_id) 
       references roles;

    alter table user_role 
       add constraint FKt7e7djp752sqn6w22i6ocqy6q 
       foreign key (role_id) 
       references roles;

    alter table user_role 
       add constraint FKj345gk1bovqvfame88rcx7yyx 
       foreign key (user_id) 
       references users;
