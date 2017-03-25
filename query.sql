--1) TESTED CONFIRMED
UPDATE Stock s
SET Quantity = (SELECT s.Quantity
                FROM Stock s, Employee e
                WHERE e.EID = '30000000' AND s.BID = e.BID AND s.SKU = '10000000')-1
WHERE s.SKU = '10000000' AND s.bid = (SELECT s.BID
                 FROM Stock s, Employee e
                 WHERE e.EID = '30000000' AND s.BID = e.BID AND s.SKU = '10000000');



--10) Purchase order
SELECT Name, SKU, maxquantity-quantity as orderquantity
FROM Stock s, Product p
WHERE p.DID = %orderDID AND s.BID = %orderBID

--12) INVENTORY COUNT

--13) SALE REPORT -- TESTED CONFIRMED

SELECT *
FROM SALE
WHERE TO_DATE('20161201', 'YYYYMMDD') <= SALEDATE AND
      SALEDATE <= TO_DATE('20170110', 'YYYYMMDD');