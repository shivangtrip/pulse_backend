create table users(userid serial primary key , username text, password text not null,email text not null unique, roles text not null);
