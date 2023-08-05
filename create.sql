create table auction
(
    price       decimal(38, 2),
    category_id bigint,
    created_at  datetime(6),
    id          bigint not null auto_increment,
    item_count  bigint,
    market_id   bigint,
    posted_at   datetime(6),
    updated_at  datetime(6),
    description varchar(255),
    status      enum ('DRAFT','IN_PROGRESS','SOLD','UNSOLD'),
    type        enum ('Dutch','English','Sealed'),
    primary key (id)
) engine = InnoDB;
create table bid
(
    amount     decimal(38, 2),
    auction_id bigint,
    created_at datetime(6),
    id         bigint not null auto_increment,
    updated_at datetime(6),
    user_id    bigint,
    primary key (id)
) engine = InnoDB;
create table category
(
    created_at datetime(6),
    id         bigint not null auto_increment,
    parent_id  bigint,
    updated_at datetime(6),
    tag        varchar(255),
    primary key (id)
) engine = InnoDB;
create table market
(
    active_auctions bigint,
    created_at      datetime(6),
    id              bigint not null auto_increment,
    total_auctions  bigint,
    updated_at      datetime(6),
    status          enum ('CLOSED','OPEN'),
    primary key (id)
) engine = InnoDB;
create table rating
(
    stars       float(23),
    created_at  datetime(6),
    id          bigint not null auto_increment,
    receiver_id bigint,
    updated_at  datetime(6),
    type        enum ('AUCTION','BUYER','SELLER'),
    primary key (id)
) engine = InnoDB;
create table reputation
(
    reputation_points float(53),
    created_at        datetime(6),
    id                bigint not null auto_increment,
    updated_at        datetime(6),
    user_id           bigint,
    primary key (id)
) engine = InnoDB;
create table transaction
(
    amount     decimal(38, 2),
    auction_id bigint,
    buyer_id   bigint,
    created_at datetime(6),
    id         bigint not null auto_increment,
    seller_id  bigint,
    updated_at datetime(6),
    primary key (id)
) engine = InnoDB;
create table user
(
    created_at      datetime(6),
    id              bigint not null auto_increment,
    updated_at      datetime(6),
    email           varchar(255),
    first_name      varchar(255),
    last_login_date varchar(255),
    last_name       varchar(255),
    password        varchar(255),
    role            varchar(255),
    primary key (id)
) engine = InnoDB;
alter table user
    add constraint UK_t8tbwelrnviudxdaggwr1kd9b unique (email);
alter table auction
    add constraint FKn177e55w3c2qy0osa460lcmgc foreign key (category_id) references category (id);
alter table auction
    add constraint FKt01lk1guo24oi9gtd1tkxex29 foreign key (market_id) references market (id);
alter table bid
    add constraint FKhexc6i4j8i0tmpt8bdulp6g3g foreign key (auction_id) references auction (id);
alter table bid
    add constraint FK4abkntgv9nvsfi86p7kfl63au foreign key (user_id) references user (id);
alter table category
    add constraint FK2y94svpmqttx80mshyny85wqr foreign key (parent_id) references category (id);
alter table reputation
    add constraint FK9wusesff0dkdlob6d87cdkudw foreign key (user_id) references user (id);
alter table transaction
    add constraint FKqa8twg42fqfh9n4o10drf3sfg foreign key (auction_id) references auction (id);
alter table transaction
    add constraint FKosd6qqlkyqp8gk4gjisggqev0 foreign key (buyer_id) references user (id);
alter table transaction
    add constraint FKs37irexq9hyvl7pqyqya2i0dn foreign key (seller_id) references user (id);
