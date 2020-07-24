# alpha-mart
Steps to Run the project:
1) run the project as java application
2) Open http://localhost:8080/index.html on browser

UI
1) Products
	- User will see three tabs: Apparel, Book and Search
	- Apparels tab will list all the apparels
	- Books will list all the books
	- Search tab to be used for searching products
		# By Id - Enter between 1-8 to get products
		# By Name - Like search
		# By Type - Valid values are Book and Apparel.
	- Add to cart button against all products in each tab
	- If the item is already in the cart, quantity will be increased by 1
2) Cart
	- User can update the quantity for a given product
	- User can remove a given product from the cart
	- User can remove all products from a cart
	- If user enters negative then an alert will be shown and product will be deleted from the cart.
	- Total amount due is displayed.

Database:
http://localhost:8080/h2
JDBC URL - jdbc:h2:file:~/alphamart

select * from product;
select * from user;
select * from cart;
select * from cart_detail;

Note:
User Id is padded as request parameter
