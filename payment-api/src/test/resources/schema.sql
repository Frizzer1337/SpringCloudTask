create table client
(
    id            int auto_increment
        primary key,
    name          varchar(45)    not null,
    surname       varchar(45)    not null,
    phone         varchar(45)    not null,
    salary        int            not null,
    social_credit decimal(15, 2) not null
);

create table credit
(
    id                int auto_increment
        primary key,
    last_payment_date varchar(45)                                                                                          not null,
    penalty           decimal(15, 2)                                                                                       not null,
    client_id         int                                                                                                  not null,
    credit_balance    decimal(15, 2)                                                                                       not null,
    credit_status     enum ('CREATED', 'APPROVED', 'NOT_APPROVED', 'NEED_HUMAN_APPROVE', 'NEED_COLLECTOR', 'CREDIT_PAYED') not null
);

create table payment
(
    id        int auto_increment
        primary key,
    payment   int                                      not null,
    credit_id int                                      not null,
    status    enum ('PENDING', 'APPROVED', 'CANCELED') not null
);

