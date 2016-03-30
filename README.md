# WITTY
An innovative E-commerce android app Prototype based on Firebase

To understand the app's concept let's start with a story

John knows a lot about buying products from the offline retailers, he has a decade worth of experience of shopping quality products, saving a lot of bucks by bargaining with the seller. 
He loves to talk to sellers. He is very cautious about where he is spending his money so he asks for every minute details of the product before buying it. 
John has also used E-commerce websites loaded with different features, but what he misses in every platform is the interaction and lack of details, also most of the sellers are overseas and have improper contact details so he is afraid of investing money in buying online.
He wants to buy products from online platforms because of the variety they offer. If he is assured about qualtity and details of the product he is willing to spend more bucks for getting a product.

Now let's see the situation from sellers point of view

Mark owns a Fashion-Store in a shopping mall. He is renowned for his quality and service in his area. Mark offers discounts and accept bargains to customers to attract customers. Now being confident of his products, he wants to expand his brand. One way would be to buy another shop in nearby area and manage both the shops, but there's lot of overhead and costs involved. Perhaps the easier way around for mark would be to start selling his product nation-wide via an e-commerce site. Now here's the catch, due to lack of communication between him and the customer, He will face two problems:
1. He doesn't know the exact price he should set. If he sets a price lower than what users can afford, he'll have to cut his profits. On the other hand , if the prices are high no customer would buy his product and he'll be at loss again.
2. He can't attract customers by giving the customized offers, discounts and bargains, customer's interests, trends they are following, styles they like etc.
His problem could easily be solved if we give him access to interact with customer personally and provide them stats and analysis about
They want to interact with their customers personally, they want to know each of their customer's interests, trends they are following, styles they like etc. So the questions that arise for him are  
1.Which E-commerce platform is developed enough to provide them the needed interaction with the user? 
2.Which of them can provide data like how many of the users have this particular product in their cart?
3.Which of their products are attracting the customers?
4.Which of their products people are not buying due to high prices?
From the above two perspectives we can see there is still a gap between Retailers and customers which E-commerce has not been able to fulfil properly yet. The idea of bridging this gap of communication led us to the development of application 'Witty'. 
'Witty' is made with the objective of:
1.Bridging the gap between retailer and customer 
2.Increasing competition between sellers and thereby reducing price.
3.Simulating a real world market experience like E-commerce where buyers and sellers will have more flexibility in terms of prices and products.
4.Making e-commerce browsing more interactive.
'Witty' is a win-win situation for users and as well as the sellers.

How will users benefit?

1. Witty has an option of requesting a bargain on each product and chat platform for each product to negotiate with sellers.
2. Users can clear all their doubts regarding product from retailer itself before buying.
3. Multiple sellers will be there on app, meaning a lot of competition because each seller will react differently to user's bargain requests.
4. Chats are recorded and making the returns and refunds easier, users can return the product if it's not like the seller promised.
5. More personalized experience.
How will sellers benefit?
1. We provide a lot of user insights to the seller so they can improve on that and have an edge over other sellers.
2. Disperencies and preference issues like payment methods can be resolved in the chat itself.
3. We show them what our users are demanding so that they can update their stock accordingly and earn more.
4. They can track their users, like... Which of their products are attracting the customers? How many users have the particular product in their cart. Seller will be able to personally chat with any user!
How do we prevent users from not bargaining all the time?
Basically there are hidden discounts based on the certain criteria for those who are buying directly instead of bargaining. That's why bargaining everytime would not be smart decision.

Let's see how witty is different from developer's point of view:
1. Everything is realtime , Data is stored as JSON, synced to all connected clients in realtime.
2. We have android app and website working out of the box in sync.(bargainhawk.firebaseapp.com)
3. Moreover you will still able to buy even if you go offline due to some network problem (not a good 3G connection or while passing through tunnel)'Witty' will store the data for you in queue and will sync them when the network arrives. Our Apps work offline also!
4. The website is SPA(Single Page Application) which means no reloading of repetitive stuffs which increases the browsing speed.
5. Android App even works offline, you can send messages, add products to the cart, request bargains on the products etc. 

Features Implemented in this prototype: 

Unique features
1. Sepereate Interfaces for users and sellers on both android and web platform.
2. We've tried to make it interactive and humorous and certain places by adding dialog boxes and tooltips.
3.User's ability to bargain and seller's ability to either reject,accept or offer a deal to the user based on the product which user can either accept or reject.
4. Live status of the bargain requests and offers is shown to both the sellers and users and once the bargain request or offer is accepted, prodcut is moved to the cart.

User Insights:
5.  Seller is able to see product stats like how many users have this particular product in their cart or have bidded on them.
6. User can demand for a product and sellers can see user demand stats and add the product in their stock accordingly.
7. Every user can chat with every seller for each product. You don't have the constraint like you have to buy that product only then you can chat with the seller, even if you are interested in product you can ask details! 
8. History of the bids and the chats are recorded and are used to resolve disperancies in future.
9. All the above with necessary features of E-commerce like carts, product categories, sorting etc.
