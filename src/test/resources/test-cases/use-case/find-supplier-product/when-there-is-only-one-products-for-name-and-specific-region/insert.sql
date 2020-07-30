insert into supplier (id, name) values (1, 'SupplierA');
insert into supplier (id, name) values (2, 'SupplierB');

insert into product (id, name) values (1, 'tshirt');

insert into supplier_product (supplier_id, product_id, delivery_times, in_stock) values (
    (select id from supplier where name = 'SupplierA'),
    (select id from product where name = 'tshirt'),
    '{"eu": 1}',
    10
);
insert into supplier_product (supplier_id, product_id, delivery_times, in_stock) values (
    (select id from supplier where name = 'SupplierB'),
    (select id from product where name = 'tshirt'),
    '{"us": 1}',
    2
);
