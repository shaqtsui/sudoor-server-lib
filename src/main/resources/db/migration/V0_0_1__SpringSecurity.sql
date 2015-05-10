create table users(
    username varchar_ignorecase(50) not null primary key,
    password varchar_ignorecase(80) not null,
    enabled boolean not null
);

create table authorities (
    username varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username,authority);


INSERT INTO users VALUES ('admin', '$2a$10$1C./yRXWExLyay1ccRa3SeMedkW0/Tjv18P7jBxu1unGAScWSHB4m',true);
INSERT INTO authorities VALUES ('admin', 'ROLE_ADMIN');