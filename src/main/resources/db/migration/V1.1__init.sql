drop table if exists client;

create table client (
    id bigint primary key auto_increment,
    name varchar(100) not null,
    city varchar(100),
    postal_code varchar(10), 
    email varchar(100), 
    birth_date date,
    positive_number int
)
