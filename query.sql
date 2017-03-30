--DDL and population can be found in /resorces/sql/
--These queries can also be found in /src/sql/GameStoreDB.java

--===================
--1) Buying a product
--===================
--Inputs: EID, SKU, payment, CID
-- also generated snum and Date
UPDATE Stock s
SET Quantity = (SELECT s.Quantity
                FROM Stock s, Employee e
                WHERE e.EID = ? AND s.BID = e.BID AND s.SKU = ?)-1
WHERE s.SKU = ? AND s.bid = (SELECT s.BID
                 FROM Stock s, Employee e
                 WHERE e.EID = ? AND s.BID = e.BID AND s.SKU = ?);
--?: (1,EID), (2,SKU), (3,SKU), (4,EID), (5,SKU)
INSERT INTO Sale
VALUES(?,?,?,?,?,?)
--?: (1,payment), (2,generated snum), (3,SKU), (4,Date), (5,CID), (6,EID)

--===================
--2)Adding Customer
--===================
--Inputs name, cid, phone, address
INSERT INTO Customer
 VALUES (?,?,?,?)
--?: (1,name), (2,cid), (3,phone), (4,address)

--===================
--4)Adding Employee to Store
--===================
--Inputs: EID,BID,Wage,Position,Phone,Address
INSERT INTO Employee
VALUES(?,?,?,?,?,?)
--?: (1,EID),(2,BID),(3,Wage),(4,Position),(5,Phone),(6,Address)

--===================
--5)Remove Employee from a Store
--===================
--Inputs: EID
DELETE FROM Employee e
WHERE e.EID = ?
--?: (1,EID)

--===================
--6)Add game to Database
--===================
--Inputs: SKU,DID,name,price
INSERT INTO Product
VALUES(?,?,?,?)
--?: (1,name),(2,SKU),(3,price),(4,DID)

--===================
--7)Add game to Store
--===================
--Inputs: BID,SKU,Quantity,maxQuantity
INSERT INTO Stock
VALUES(?,?,?,?)
--?: (1,BID),(2,SKU),(3,Quantity),(4,maxQuantity)

--===================
--8)Changing game price
--===================
--Inputs: SKU,newPrice
UPDATE Product p
SET price = ?
WHERE p.SKU = ?
--?: (1,newPrice),(2,SKU)

--===================
--9)Check Customer Account
--===================
--Inputs: CID  OR Name and Phone
--Also generated date for 30 days prior to today's date
--Case=CID
	SELECT * FROM Customer c WHERE c.CID=?;
	--?: (1,CID)
	SELECT s.snum, s.Payment, s.saleDate, c.Name AS cname, p.Name AS pname, e.Name AS ename
	FROM Sale s
	WHERE s.CID = ? AND s.saleDate >= ?
	--?: (1,CID),(2,generatedDate)
--Case Name and Phone
	SELECT * FROM Customer c WHERE c.Name=? AND c.Phone=?;
	--?: (1,Name),(2,Phone)
	SELECT s.snum, s.Payment, s.saleDate, c.Name AS cname, p.Name AS pname, e.Name AS ename
	FROM Sale s, Customer c
	WHERE s.CID=c.CID AND c.Name = ? AND c.Phone=? 
	AND s.saleDate >= ?
	--?: (1,Name),(2,Phone),(3,generatedDate)
--===================
--10) Purchase order
--===================
--Inputs: DID, BID
SELECT Name, s.SKU, Price,
  maxquantity - quantity as orderQuantity,
  Price*(maxquantity - quantity) as orderPrice
FROM Stock s, Product p
WHERE p.DID = ? AND s.BID = ?
      AND s.SKU=p.SKU AND maxquantity-quantity >0
--?: (1,DID), (2,BID)

--===================
--11) Update Product Quantity
--===================
--Inputs: BID,SKU,addQuantity
UPDATE Stock s
SET quantity = s.quantity+?
WHERE s.BID=? AND s.SKU=?
--?: (1,addQuantity),(2,BID),(3,SKU)

--===================
--12) INVENTORY COUNT
--===================
--Inputs: BID
SELECT Name, s.SKU, Price, Quantity
FROM Stock s, Product p
WHERE s.SKU=p.SKU AND s.BID=?
--?: (1,BID)

--===================
--13) SALE REPORT
--===================
--Inputs: startDate, endDate
SELECT S.Snum, s.Payment, s.saleDate, c.Name AS cname, p.Name AS pname, e.Name AS ename
FROM Sale
WHERE ? <= SALEDATE AND SALEDATE <= ?
ORDER BY saleDate ASC
--?: (1,startDate), (2,endDate)

--===================
--13) SALE REPORT - EMPLOYEES
--===================
--Inputs: startDate, endDate,
--optional: AGGREGATE

--SELECT AGGREGATE(sum) num FROM(
SELECT e.EID, COUNT(e.EID) count, SUM(price) sum
FROM Sale s, Employee e, Product p
WHERE s.eid = e.eid AND s.SKU = p.SKU
	AND ? <= SALEDATE AND SALEDATE <= ?
GROUP BY e.EID
ORDER BY COUNT(e.EID) DESC
--)
--(1,startDate), (2,endDate)

--Aggregation query: Pick one query that requires the use of aggregation (min, max, average, or count 
--are all fine). Rerun with at least one other example of aggregation.

--===================
--14) SALE REPORT - PRODUCT&BRANCH
--===================
--Inputs: startDate, endDate
--optional: AGGREGATE

--SELECT SKU, AGGREGATE(count) num FROM (
SELECT SKU, BID, COUNT(SKU) count
FROM Sale s, Employee e
WHERE ? <= SALEDATE AND SALEDATE <= ?
	AND s.eid = e.eid
GROUP BY SKU, BID
ORDER BY BID, COUNT(SKU) DESC
--) GROUP BY SKU ORDER BY AGGREGATE(count) DESC
--?: (1,startDate), (2,endDate)

--Nested aggregation with group-by: Pick one query that finds some aggregated value for each group 
--(eg. the average number of items purchased per customer) and then rerun with a different aggregation.
--Provide an interface for the user to specify whether the average, min, max or count is requested.



--===================
--15)STORES THAT STOCK ALL PRODUCTS
--===================
SELECT b.BID
FROM Branch b
WHERE NOT EXISTS
((SELECT p.SKU FROM Product p)
 MINUS
 (SELECT s.SKU FROM Stock s
 WHERE s.BID=b.BID));
--Division query: Pick one query of this category and provide an interface for the user to choose this
--query (eg. find all the customers who bought all the items). Prove that your division results would 
--change based on the data in your database. You can do it either by inserting a new tuple into or 
--deleting an existing tuple from the appropriate tables.






--TRIGGERS:
--were hardcoded rather than scripted because our parser couldn't handle it
CREATE OR REPLACE TRIGGER branch_on_insert
BEFORE INSERT ON Branch
FOR EACH ROW
BEGIN
:new.BID := trim(to_char(branch_sequence.nextval, '00000000'));
END;

CREATE OR REPLACE TRIGGER employee_on_insert
BEFORE INSERT ON Employee
FOR EACH ROW
BEGIN
:new.EID := trim(to_char(employee_sequence.nextval, '00000000'));+
END;

CREATE OR REPLACE TRIGGER developer_on_insert
BEFORE INSERT ON Developer
FOR EACH ROW
BEGIN
:new.DID := trim(to_char(developer_sequence.nextval, '00000000'));
END;

CREATE OR REPLACE TRIGGER customer_on_insert
BEFORE INSERT ON Customer
FOR EACH ROW
BEGIN
:new.CID := trim(to_char(customer_sequence.nextval, '00000000'));
END;

CREATE OR REPLACE TRIGGER sale_on_insert
BEFORE INSERT ON Sale
FOR EACH ROW
BEGIN
:new.snum := trim(to_char(sale_sequence.nextval, '00000000'));
END;

