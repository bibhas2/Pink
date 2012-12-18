drop table if exists Cart;
drop table if exists CartItem;
drop table if exists Address;
drop table if exists Product;

CREATE TABLE Product (
	id integer NOT NULL AUTO_INCREMENT,
	sku varchar(256) NOT NULL,
	name varchar(1024) NOT NULL,
	description varchar(1024),
	price decimal(19, 4) NOT NULL,
	imageLink varchar(1024),
	
	UNIQUE KEY ProductU1 (sku),
	PRIMARY KEY (id)
);

CREATE TABLE Address (
	id integer NOT NULL AUTO_INCREMENT,
	customerName varchar(1024) NOT NULL,
	email varchar(256),
	phone varchar(65),
	street1 varchar(1024),
	street2 varchar(1024),
	city varchar(256),
	state varchar(256),
	country varchar(256),
	zip varchar(128),
	
	PRIMARY KEY (id)
);
	
CREATE TABLE Cart (
	id integer NOT NULL AUTO_INCREMENT,
	shippingAddressId integer,
	billingAddressId integer,
	productTotal decimal(19, 4),
	productTax decimal(19, 4),
	shipping decimal(19, 4),
	shippingTax decimal(19, 4),
	grandTotal decimal(19, 4),
	placedOn date,
	status char(1) NOT NULL,
	
	PRIMARY KEY (id),
	CONSTRAINT CartFK1 FOREIGN KEY (shippingAddressId) REFERENCES Address (id),
	CONSTRAINT CartFK2 FOREIGN KEY (billingAddressId) REFERENCES Address (id)	
);

CREATE TABLE CartItem (
	id integer NOT NULL AUTO_INCREMENT,
	cartId integer NOT NULL,
	productId integer NOT NULL,
	quantity integer NOT NULL,
	
	PRIMARY KEY (id),
	CONSTRAINT CartItemFK1 FOREIGN KEY (cartId) REFERENCES Cart (id),
	CONSTRAINT CartItemFK2 FOREIGN KEY (productId) REFERENCES Product (id)	
);

CREATE INDEX CartItemI1 ON CartItem (cartId);

INSERT INTO Product (sku, name, description, price) values ('P001', 'T-Shirt', '100% Cotton T-Shirt', 18.99);
INSERT INTO Product (sku, name, description, price) values ('P002', 'Mug', 'Cofee mug with company logo', 7.99);
