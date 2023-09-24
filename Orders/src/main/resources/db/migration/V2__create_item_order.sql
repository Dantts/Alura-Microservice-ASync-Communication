create table public.item_order
(
    id          bigserial
        primary key,
    description varchar(255),
    quantity    integer not null,
    order_id    bigint  not null
        constraint fk8cvi933jxg2n8ojl4wxnt1ri5
            references public.orders
);