create table public.payments
(
    id                bigserial      primary key,
    value             decimal(19, 2) not null,
    name              varchar(100)   default null,
    number            varchar(19)    default null,
    expiration        varchar(7)     default null,
    code              varchar(3)     default null,
    status            varchar(255)   not null,
    order_id          bigint         not null,
    payment_method_id bigint         not null
);