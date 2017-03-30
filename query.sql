--DDL and population can be found in /resorces/sql/

--===================
--1) Buying a product TESTED CONFIRMED
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
--2)Adding Customer TESTED CONFIRMED
--===================
--Inputs name, cid, phone, address
INSERT INTO Customer
 VALUES (?,?,?,?)
--?: (1,name), (2,cid), (3,phone), (4,address)

--===================
--4)Adding Employee to Store TESTED CONFIRMED
--===================
--Inputs: EID,BID,Wage,Position,Phone,Address
INSERT INTO Employee
VALUES(?,?,?,?,?,?)
--?: (1,EID),(2,BID),(3,Wage),(4,Position),(5,Phone),(6,Address)

--===================
--5)Remove Employee from a Store TESTED CONFIRMED
--===================
--Inputs: EID
DELETE FROM Employee e
WHERE e.EID = ?
--?: (1,EID)

--===================
--6)Add game to Database TESTED CONFIRMED
--===================
--Inputs: SKU,DID,name,price
INSERT INTO Product
VALUES(?,?,?,?)
--?: (1,name),(2,SKU),(3,price),(4,DID)

--===================
--7)Add game to Store TESTED CONFIRMED
--===================
--Inputs: BID,SKU,Quantity,maxQuantity
INSERT INTO Stock
VALUES(?,?,?,?)
--?: (1,BID),(2,SKU),(3,Quantity),(4,maxQuantity)

--===================
--8)Changing game price TESTED CONFIRMED
--===================
--Inputs: SKU,newPrice
UPDATE Product p
SET price = ?
WHERE p.SKU = ?
--?: (1,newPrice),(2,SKU)

--===================
--9)Check Customer Account TESTED CONFIRMED
--===================
--Inputs: CID  OR Name and Phone
--Also generated date for 30 days prior to today's date
--Case=CID
	SELECT * FROM Customer c WHERE c.CID=?;
	--?: (1,CID)
	SELECT Payment, Snum, SKU, saleDate
	FROM Sale s
	WHERE s.CID = ? AND s.saleDate >= ?
	--?: (1,CID),(2,generatedDate)
--Case Name and Phone
	SELECT * FROM Customer c WHERE c.Name=? AND c.Phone=?;
	--?: (1,Name),(2,Phone)
	SELECT Payment, Snum, SKU, saleDate
	FROM Sale s, Customer c
	WHERE s.CID=c.CID AND c.Name = ? AND c.Phone=? 
	AND s.saleDate >= ?
	--?: (1,Name),(2,Phone),(3,generatedDate)
--===================
--10) Purchase order TESTED CONFIRMED
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
--11) Update Product Quantity TESTED CONFIRMED
--===================
--Inputs: BID,SKU,addQuantity
UPDATE Stock s
SET quantity = s.quantity+?
WHERE s.BID=? AND s.SKU=?
--?: (1,addQuantity),(2,BID),(3,SKU)

--===================
--12) INVENTORY COUNT TESTED CONFIRMED
--===================
--Inputs: BID
SELECT Name, s.SKU, Price, Quantity
FROM Stock s, Product p
WHERE s.SKU=p.SKU AND s.BID=?
--?: (1,BID)

--===================
--13) SALE REPORT -- TESTED CONFIRMED
--===================
--Inputs: startDate, endDate
SELECT *
FROM Sale
WHERE ? <= SALEDATE AND SALEDATE <= ?
--?: (1,startDate), (2,endDate)

--===================
--13) SALE REPORT - BEST EMPLOYEES -- AGGREGATION -- TESTED CONFIRMED
--===================
--Inputs: startDate, endDate,
--optional: AGGREGATE

--SELECT AGGREGATE(count) FROM(
SELECT e.EID, COUNT(e.EID) count
FROM Sale s, Employee e
WHERE s.eid = e.eid 
	AND ? <= SALEDATE AND SALEDATE <= ?
GROUP BY e.EID
ORDER BY COUNT(e.EID) DESC
--)
--(1,startDate), (2,endDate)

--Aggregation query: Pick one query that requires the use of aggregation (min, max, average, or count 
--are all fine). Rerun with at least one other example of aggregation.

--===================
--14) SALE REPORT - PRODUCT&BRANCH -- NESTED AGGREGATION -- TESTED CONFIRMED
--===================
--Inputs: startDate, endDate
--optional: AGGREGATE

--SELECT BID, AGGREGATE(count) FROM (
SELECT SKU, BID, COUNT(SKU) count
FROM Sale s, Employee e
WHERE ? <= SALEDATE AND SALEDATE <= ?
	AND s.eid = e.eid
GROUP BY SKU, BID
ORDER BY BID, COUNT(SKU) DESC
--)
--?: (1,startDate), (2,endDate)

--Nested aggregation with group-by: Pick one query that finds some aggregated value for each group 
--(eg. the average number of items purchased per customer) and then rerun with a different aggregation.
--Provide an interface for the user to specify whether the average, min, max or count is requested.



--===================
--15)STORES THAT STOCK ALL PRODUCTS --Division --Untested TODO: change population script
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

