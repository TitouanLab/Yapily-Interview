I have made 2 microservices : order-service and product-service

To run the application, just start both services in IntelliJ. The assignment didn't mention any sort of deployment so I assumed a local code editor was enough.
I have followed the URLs (I added "/api" to the path) and payloads of the assignment and have not created any additional ones. I believe a bulk API to find products by ids could be useful but not required for the purpose of this assignment.
I have manually assigned port 8080 to the product service and 8081 to the order service.

Nevertheless, here's an overview of the endpoints : 

Product service : 
GET http://localhost:8080/api/products (get all products)
GET http://localhost:8080/api/products/1 (get by ID)
POST http://localhost:8080/api/products (create new product)
payload : {
 		"name": "Gouda",
		 "price": 5.99,
 		"labels": ["food", "limited"]
	   }
DELETE http://localhost:8080/api/products/1 (delete product by ID)

Order service : 
GET http://localhost:8081/api/carts (get all carts)
POST http://localhost:8081/api/carts (create new cart)
PUT http://localhost:8081/api/carts/1 (update cart)
payload : [
    		{
        		"product_id": 1,
        		"quantity": 1
    		}
	  ]
POST http://localhost:8081/api/carts/1/checkout (checkout cart)

I look forward to discussing with you the decisions I have made throughout this assignment (DB choice, schema choices, behavior, design, validation)