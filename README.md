# ForgetTheDeadline
Database project for CPSC 304 using JDBC and Oracle. 
Game store with three types of users: customer, manager, employee

[Proposal](https://docs.google.com/document/d/1jRus3NNavl_nG-SF4IYw8C5yOZpvsGORuIBG_94L5H8/edit#heading=h.fh6wsnxyoy1x)

[ERD/Relations + SQL DDL Draft](https://docs.google.com/document/d/1t7QMry3ubPheGd2jwYLlLljdpMoGy95Skcxa00zyEDM/edit?usp=sharing)

[Specifications](https://docs.google.com/document/d/17Hmr6atVv3qUQdcFeXA307YlfW4LnBfiLr5fSZowWrk/edit?usp=sharing)

[ER Diagram](https://www.gliffy.com/go/share/image/scuvjqj0fejiqt2ohga7.png?utm_medium=live-embed&utm_source=custom)

To connect to the database, create an ssh tunnel using:
```
ssh -l UNIXID -L localhost:1522:dbhost.ugrad.cs.ubc.ca:1522 remote.ugrad.cs.ubc.ca
```


## What Was Accomplished
Created an application to provide access to the database, and provide a set of queries available through the GUI. The tables were iteratively designed throughout the phases, resulting in our final database. 


## Changes to Final Schema
After starting to work on the project, we decided that the membership table was really unnecessary in its current state. We were only going to be doing a couple of very simple queries with it, so we removed it from the final design.

There were also some more minor changes: Changing a couple values from integer to char that we forgot, and changing SKU and EID in the stock table to allow null values, in the cases of a fired employee or a discontinued product.

## Scripts
The script to create the tables is located at  `/resources/sql/create_db.sql`

Likewise, the script to populate the tables is located at '`/resources/sql/populate_db.sql`

## Queries
See `/queries.sql`

## Functional Dependencies
**BID → Phone, Address**

The ID of the Branch uniquely identifies the Phone and Address

**EID → Name, Address, Phone Number, position**

An Employee’s ID will uniquely identify his name,address,phone number, and position

**DID → Name, Address, Phone Number**

A Developer’s identifier will uniquely identify their name, address, and phone number

**SKU → Name, Price, DID**

A Product’s SKU will uniquely identify its Name, Price, and Distributor

**BID, SKU → Quantity**

Given a Branch’s ID, and a Product’s SKU, you can find the quantity of the product stocked in the Branch

**CID → Address, Phone number, Name**

A customer’s ID will provide their Address, Phone number, and Name
*Phone Number, Name → CID, Address
A customer’s Phone number and name are unique and will identify their ID and address

**snum → payment, SKU, CID, EID, saleDate**

A sale’s number will uniquely identify the payment method/code, Product SKU, Customer ID, Employee ID, and the date of the sale

