USE harba_Chinook;

-- a. Find distinct track names that start with “Z”. Sort the output alphabetically.
select distinct Name
from Track
where name like 'z%'
order by Name;

-- b. Find the first names of the employees who are older than their supervisor. Hint:
-- ReportsTo attribute in Employee table stores the EmployeeId of the supervisor.
-- Sort the output alphabetically.
select FirstName
from Employee as S
where exists
(select BirthDate
from Employee as T
where T.EmployeeID = S.ReportsTo
and T.Birthdate > S.Birthdate)
order by FirstName;

-- c. Find the name of the highest priced track. If more than one track has the highest
-- price, return the names of all such tracks. Sort the output alphabetically based on
-- the track name.
select Name
from Track
where UnitPrice = (select max(UnitPrice) from Track)
order by Name; 

-- d. Find a list containing the total amount spend by a customer. Include the
-- customer’s id and the last names along with the total amount. For customers who did not 
-- make any purchase, make sure to include them as well (the total should be 0.00 for those customers).
select LastName, CustomerID, sum(Total) as Total
from Customer as C natural join Invoice as S
where C.CustomerId = S.CustomerId
group by CustomerId;

-- e. Find the title of the highest priced album. (COULD BE BETTER)
select Title, sum(UnitPrice) as price
from Album natural join Track
group by AlbumId
order by price DESC
Limit 1;

-- f. Find a distinct list containing the titles of albums that are never sold. Consider an
-- album never sold if none of its tracks are sold. Sort the output alphabetically.
select distinct Title, AlbumId
from Album
where AlbumId not in
(select AlbumId
from InvoiceLine natural join Track
group by AlbumId)
order by Title;

-- g. Create a view that returns customers’ first and last names along with
-- corresponding sums of all their invoice totals. Name the view as “CustomerInvoices.”
create view CustomerInvoices as 
select FirstName, LastName, sum(Total)
from Customer natural join Invoice
group by CustomerId;

select *
from CustomerInvoices;
