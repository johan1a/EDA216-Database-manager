-- Delete the tables if they exist. Set foreign_key_checks = 0 to
-- disable foreign key checks, so the tables may be dropped in
-- arbitrary order.
set foreign_key_checks = 0;

drop table if exists Cookies;
drop table if exists IngredientStorages;
drop table if exists Ingredients;
drop table if exists CookieIngredients;
drop table if exists Customers;
drop table if exists Orders;
drop table if exists Pallets;
drop table if exists BlockedProducts;
drop table if exists OrderCookies;

set foreign_key_checks = 1;

-- Create the tables.
create table Cookies(
	cookieName varchar(30) primary key
	);

create table IngredientStorages(
	deliveryID int, 
	amount int, 
	deliveryDate date,
	primary key(deliveryID)
	);

create table Ingredients(
	ingredientName varchar(30), 
	unit varchar(5), 
	deliveryID int,
	primary key(ingredientName),
	foreign key (deliveryID) references IngredientStorages(deliveryID)
	);

create table CookieIngredients(
	cookieName varchar(30), 
	ingredientName varchar(30), 
	amount float,
	primary key(cookieName, ingredientName),
	foreign key (cookieName) references Cookies(cookieName),
	foreign key (ingredientName) references Ingredients(ingredientName)
	);

create table Customers(
	customerName varchar(30),
	address varchar(30),
	primary key(customerName)
	);

create table Orders(
	orderID int auto_increment,
	customerName varchar(30),
	deliveryDate date,
	primary key(orderID),
	foreign key (customerName) references Customers(customerName)
	);

create table Pallets(
	barcodeID int auto_increment,
	cookieName varchar(30),
	orderID int,
	productionDate date,
	productionTime time,
	deliveryDate date,
	deliveryTime time,
	primary key(barcodeID),
	foreign key (cookieName) references Cookies(cookieName),
	foreign key (orderID) references Orders(orderID)
	);

create table BlockedProducts(
	cookieName varchar(30),
	intervalStart date,
	intervalEnd date,
	primary key(cookieName, intervalStart, intervalEnd),
	foreign key (cookieName) references Cookies(cookieName)
	);

create table OrderCookies(
	orderID int,
	cookieName varchar(30),
	nbrPallets int,
	primary key(orderID, cookieName),
	foreign key (orderID) references Orders(orderID),
	foreign key (cookieName) references Cookies(cookieName)
	);

create view Recipes as 
select cookieName, ingredientName, amount, unit 
from CookieIngredients natural join Ingredients;

-- Insert data into the tables.

insert into Cookies values('Nut ring');
insert into Cookies values('Nut cookie');
insert into Cookies values('Amneris');
insert into Cookies values('Tango');
insert into Cookies values('Almond delight');
insert into Cookies values('Berliner');


insert into IngredientStorages values(1, 1000000, '2014-02-04');
insert into IngredientStorages values(2, 1000000, '2014-02-04');
insert into IngredientStorages values(3, 1000000, '2014-02-04');
insert into IngredientStorages values(4, 1000000, '2014-02-04');
insert into IngredientStorages values(5, 1000000, '2014-02-04');
insert into IngredientStorages values(6, 1000000, '2014-02-04');
insert into IngredientStorages values(7, 1000000, '2014-02-04');
insert into IngredientStorages values(8, 1000000, '2014-02-04');
insert into IngredientStorages values(9, 1000000, '2014-02-04');
insert into IngredientStorages values(10, 1000000, '2014-02-04');
insert into IngredientStorages values(11, 1000000, '2014-02-04');
insert into IngredientStorages values(12, 1000000, '2014-02-04');
insert into IngredientStorages values(13, 1000000, '2014-02-04');
insert into IngredientStorages values(14, 1000000, '2014-02-04');
insert into IngredientStorages values(15, 1000000, '2014-02-04');
insert into IngredientStorages values(16, 1000000, '2014-02-04');


insert into Ingredients values('Flour', 'g', 1);
insert into Ingredients values('Butter', 'g', 2);
insert into Ingredients values('Icing sugar', 'g', 3);
insert into Ingredients values('Roasted, chopped nuts', 'g', 4);
insert into Ingredients values('Fine-ground nuts', 'g', 4);
insert into Ingredients values('Ground, roasted nuts', 'g', 4);
insert into Ingredients values('Bread crumbs', 'g', 5);
insert into Ingredients values('Sugar', 'g', 6);
insert into Ingredients values('Egg whites', 'dl', 7);
insert into Ingredients values('Eggs', 'g', 7);
insert into Ingredients values('Chocolate', 'g', 8);
insert into Ingredients values('Marzipan', 'g', 9);
insert into Ingredients values('Potato starch', 'g', 10);
insert into Ingredients values('Wheat flour', 'g', 11);
insert into Ingredients values('Sodium bicarbonate', 'g', 12);
insert into Ingredients values('Vanilla', 'g', 13);
insert into Ingredients values('Chopped almonds', 'g', 14);
insert into Ingredients values('Cinnamon', 'g', 15);
insert into Ingredients values('Vanilla sugar', 'g', 16);


insert into CookieIngredients values('Nut ring', 'Flour', 450);
insert into CookieIngredients values('Nut ring', 'Butter', 450);
insert into CookieIngredients values('Nut ring', 'Icing sugar', 190);
insert into CookieIngredients values('Nut ring', 'Roasted, chopped nuts', 225);

insert into CookieIngredients values('Nut cookie', 'Fine-ground nuts', 750);
insert into CookieIngredients values('Nut cookie', 'Ground, roasted nuts', 625);
insert into CookieIngredients values('Nut cookie', 'Bread crumbs', 125);
insert into CookieIngredients values('Nut cookie', 'Sugar', 375);
insert into CookieIngredients values('Nut cookie', 'Egg whites', 3.5);
insert into CookieIngredients values('Nut cookie', 'Chocolate', 50);

insert into CookieIngredients values('Amneris', 'Marzipan', 750);
insert into CookieIngredients values('Amneris', 'Butter', 250);
insert into CookieIngredients values('Amneris', 'Eggs', 250);
insert into CookieIngredients values('Amneris', 'Potato starch', 25);
insert into CookieIngredients values('Amneris', 'Wheat flour', 25);


insert into Customers values('Finkakor AB', 'Helsingborg');
insert into Customers values('Småbröd AB', 'Malmö');
insert into Customers values('Kaffebröd AB', 'Landskrona');
insert into Customers values('Bjudkakor AB', 'Ystad');
insert into Customers values('Kalaskakor AB', 'Trelleborg');
insert into Customers values('Partykakor AB', 'Kristianstad');
insert into Customers values('Gästkakor AB', 'Hässleholm');
insert into Customers values('Skånekakor AB', 'Perstorp');


