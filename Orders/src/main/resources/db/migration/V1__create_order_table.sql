create table public.orders
(
    id        bigserial primary key,
    date_time timestamp    not null,
    status    varchar(255) not null
);

