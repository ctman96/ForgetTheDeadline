1)

update Stock
set quantity = (select s.quantity
				from Stock s,Employee e
				where s.BID = e.BID and s.SKU = SKU)
where SKU = SKU