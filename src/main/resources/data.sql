insert into authority (authority_name) values ('MEMBER');
insert into authority (authority_name) values ('ADMIN');

insert into member(name, password, is_activated,authority)
VALUES ('test', '$2a$10$uqRvy5nAXIHVD4ZswJ8AQOw6R5q8Bak.oyL8IUqXK8p9fBcajKRN2', 1, 'MEMBER')
ON DUPLICATE KEY UPDATE name     = 'test',
                        password = '$2a$10$uqRvy5nAXIHVD4ZswJ8AQOw6R5q8Bak.oyL8IUqXK8p9fBcajKRN2',
                        authority     = 'MEMBER';

insert into member(name, password, is_activated,authority)
VALUES ('admin', '$2a$10$uqRvy5nAXIHVD4ZswJ8AQOw6R5q8Bak.oyL8IUqXK8p9fBcajKRN2', 1, 'ADMIN')
ON DUPLICATE KEY UPDATE name     = 'admin',
                        password = '$2a$10$uqRvy5nAXIHVD4ZswJ8AQOw6R5q8Bak.oyL8IUqXK8p9fBcajKRN2',
                        authority     = 'ADMIN';