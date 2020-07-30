insert into supplier (id, name) values (1, 'SupplierA');
insert into supplier (id, name) values (2, 'SupplierB');

insert into product (id, name) values (1, 'T-shirts');

insert into supplier_product (id, supplier_id, product_id, delivery_times, in_stock) values (
    1,
    (select id from supplier where name = 'SupplierA'),
    (select id from product where name = 'T-shirts'),
    '{"eu": 1}',
    6
);
insert into supplier_product (id, supplier_id, product_id, delivery_times, in_stock) values (
    2,
    (select id from supplier where name = 'SupplierB'),
    (select id from product where name = 'T-shirts'),
    '{"eu": 1}',
    7
);
