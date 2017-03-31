drop table Sale;
drop table Stock;
drop table Product;
drop table Developer;
drop table Employee;
drop table Branch;
drop table Customer;

drop sequence branch_sequence;
drop sequence customer_sequence;
drop sequence developer_sequence;
drop sequence employee_sequence;
drop sequence sale_sequence;

CREATE SEQUENCE branch_sequence;
CREATE SEQUENCE customer_sequence;
CREATE SEQUENCE developer_sequence;
CREATE SEQUENCE employee_sequence;
CREATE SEQUENCE sale_sequence;

CREATE TABLE Branch
(BID		CHAR(8)			NOT NULL,
 Phone		CHAR(10)		NOT NULL,
 Address	VARCHAR(30)		NOT NULL,
 PRIMARY KEY (BID));

CREATE TABLE Employee
(EID		CHAR(8)			NOT NULL,
 Name		VARCHAR(30)		NOT NULL,
 Address	VARCHAR(30)		NOT NULL,
 Phone		CHAR(10)		NOT NULL,
 Wage		DECIMAL(4,2)	NOT NULL,
 pname		VARCHAR(10)		NOT NULL,
 BID		CHAR(8)		    NOT NULL,
 PRIMARY KEY (EID),
 FOREIGN KEY (BID)
 REFERENCES Branch(BID)
 ON DELETE CASCADE);

CREATE TABLE Developer
(DID		CHAR(8)			NOT NULL,
 Name		VARCHAR(20)		NOT NULL,
 Phone		CHAR(10)		NOT NULL,
 Address	VARCHAR(30)		NOT NULL,
 PRIMARY KEY (DID));

CREATE TABLE Product
(Name	VARCHAR(40)		NOT NULL,
 SKU	CHAR(8)			NOT NULL,
 Price	DECIMAL(4,2)	NOT NULL,
 DID	CHAR(8),
 CHECK(Price >= 0),
 PRIMARY KEY (SKU),
 FOREIGN KEY (DID)
 REFERENCES Developer(DID)
 ON DELETE SET NULL);

CREATE TABLE Stock
(BID			CHAR(8)	NOT NULL,
 SKU			CHAR(8)	NOT NULL,
 Quantity		INTEGER	NOT NULL,
 maxQuantity 	INTEGER NOT NULL,
 PRIMARY KEY (BID,SKU),
 FOREIGN KEY (BID)
 REFERENCES Branch(BID)
 ON DELETE CASCADE,
 FOREIGN KEY (SKU)
 REFERENCES Product(SKU));

CREATE TABLE Customer
(Name		VARCHAR(20)		NOT NULL,
 CID		CHAR(8)			NOT NULL,
 Phone		CHAR(10)		NOT NULL,
 Address	VARCHAR(30)		NOT NULL,
 PRIMARY KEY (CID),
 UNIQUE(Name, Phone));

CREATE TABLE Sale
(payment	VARCHAR(20)	NOT NULL,
 snum		CHAR(8)		NOT NULL,
 SKU		CHAR(8),
 saleDate	DATE		NOT NULL,
 CID		CHAR(8)		NOT NULL,
 EID		CHAR(8),
 PRIMARY KEY (snum),
 FOREIGN KEY (SKU)
 REFERENCES Product(SKU)
 ON DELETE SET NULL,
 FOREIGN KEY (CID)
 REFERENCES Customer(CID),
 FOREIGN KEY (EID)
 REFERENCES Employee(EID)
 ON DELETE SET NULL);