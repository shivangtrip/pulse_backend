 create table user_app(user_id integer not null references users(userid), app_id integer not null references apps(app_id));