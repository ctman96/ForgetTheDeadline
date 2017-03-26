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
VALUES(?,?,?,?,?,?);
--?: (1,payment), (2,generated snum), (3,SKU), (4,Date), (5,CID), (6,EID)

--===================
--2)
--===================


--===================
--3)
--===================


--===================
--4)Adding Employee to Store TESTED CONFIRMED
--===================
--Inputs: EID,BID,Wage,Position,Phone,Address
INSERT INTO Employee
VALUES(?,?,?,?,?,?);
--?: (1,EID),(2,BID),(3,Wage),(4,Position),(5,Phone),(6,Address)

--===================
--5)Remove Employee from a Store TESTED CONFIRMED
--===================
--Inputs: EID
DELETE FROM Employee e
WHERE e.EID = ?;
--?: (1,EID)

--===================
--6)Add game to Database TESTED CONFIRMED
--===================
--Inputs: SKU,DID,name,price
INSERT INTO Product
VALUES(?,?,?,?);
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
WHERE p.SKU = ?;
--?: (1,newPrice),(2,SKU)

--===================
--9)Check Customer Account TESTED CONFIRMED
--===================
--Inputs: CID  OR Name and Phone
--Also generated date for 30 days prior to today's date
--Case=CID
	SELECT * FROM Customer c WHERE c.CID=?;
	--?: (1,CID)
	SELECT *
	FROM Sale s
	WHERE s.CID = ? AND s.saleDate >= ?;
	--?: (1,CID),(2,generatedDate)
--Case Name and Phone
	SELECT * FROM Customer c WHERE c.Name=? AND c.Phone=?;
	--?: (1,Name),(2,Phone)
	SELECT *
	FROM Sale s, Customer c
	WHERE s.CID=c.CID AND c.Name = ? AND c.Phone=? 
	AND s.saleDate >= ?;
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
      AND s.SKU=p.SKU AND maxquantity-quantity >0;
--?: (1,DID), (2,BID)

--===================
--11) Update Product Quantity TESTED CONFIRMED
--===================
--Inputs: BID,SKU,addQuantity
UPDATE Stock s
SET quantity = s.quantity+?
WHERE s.BID=? AND s.SKU=?;
--?: (1,addQuantity),(2,BID),(3,SKU)

--===================
--12) INVENTORY COUNT TESTED CONFIRMED
--===================
--Inputs: BID
SELECT Name, s.SKU, Price, Quantity
FROM Stock s, Product p
WHERE s.SKU=p.SKU AND s.BID=?;
--?: (1,BID)

--===================
--13) SALE REPORT -- TESTED CONFIRMED
--===================
--Inputs: startDate, endDate
SELECT *
FROM Sale
WHERE ? <= SALEDATE AND SALEDATE <= ?;
--?: (1,startDate), (2,endDate)