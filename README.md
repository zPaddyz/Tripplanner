## Trip planner API
This API facilitates users in efficiently adding and retrieving information about current trips and guides.

Overall, I believe I have made significant progress within the limited timeframe. I have successfully completed most tasks,
with the exception of gathering data from the external API. Other Key components such as the guide routes, 
controllers, DAOs, and services have all been implemented and are functioning as intended, as i feel like it's a worthy addition. If I had more time,
I would have focused on refining the project by removing unused code and adding more comprehensive comments throughout. Additionally, I would have explored the possibility of consolidating the guide and user classes into a single class to reduce the number of external classes.

### Comments
The application starts and automatically insert data into the database using the populate class, to avoid having to manually do it, however it is still possible to use the populate route and insert extra.

### How to run

1. Create a database in your local Postgres instance called `tripplanner`
2. Run the main method in the Main class to start the server on port 7070
3. See the routes in your browser at `http://localhost:7070/api/routes'
4. Use the user.http file to register a user and login (add admin access before signing in to see the admin routes)
5. Use the dev.http and guide.http files to test the routes


### dev.http Endpoint testing
                GET http://localhost:7070/api/trips/
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:37:20 GMT
Content-Type: application/json
Content-Length: 791

[
{
"id": 1,
"name": "USA Trip",
"date": [
2024,
11,
5
],
"startTime": [
10,
0
],
"endTime": [
11,
0
],
"longitude": -122.4194,
"latitude": 37.7749,
"price": 150.0,
"category": "city"
},
{
"id": 2,
"name": "BonBon Land",
"date": [
2024,
11,
6
],
"startTime": [
14,
0
],
"endTime": [
15,
0
],
"longitude": -73.935242,
"latitude": 40.73061,
"price": 200.0,
"category": "beach"
},
{
"id": 3,
"name": "Tivoli",
"date": [
2024,
11,
7
],
"startTime": [
9,
30
],
"endTime": [
10,
30
],
"longitude": -87.623177,
"latitude": 41.881832,
"price": 175.0,
"category": "snow"
},
{
"id": 4,
"name": "Bornholm",
"date": [
2024,
11,
7
],
"startTime": [
13,
0
],
"endTime": [
14,
0
],
"longitude": -95.358421,
"latitude": 29.749907,
"price": 180.0,
"category": "lake"
},
{
"id": 5,
"name": "Germany",
"date": [
2024,
11,
8
],
"startTime": [
9,
30
],
"endTime": [
10,
30
],
"longitude": -118.243683,
"latitude": 34.052235,
"price": 160.0,
"category": "city"
}
]

                GET http://localhost:7070/api/trips/2
HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:32:40 GMT
Content-Type: application/json
Content-Length: 385

{
"guide": {
"id": 3,
"firstName": "Emily",
"lastName": "Taylor",
"yearsOfExperience": 14,
"email": "emily.taylor@school.com",
"phone": 987654321,
"createdAt": [
2024,
11,
4,
11,
32,
21,
92279000
],
"updatedAt": [
2024,
11,
4,
11,
32,
21,
92279000
]
},
"trip": {
"id": 2,
"name": "BonBon Land",
"date": [
2024,
11,
6
],
"startTime": [
14,
0
],
"endTime": [
15,
0
],
"longitude": -73.935242,
"latitude": 40.73061,
"price": 200.0,
"category": "beach"
}
}

                POST http://localhost:7070/api/trips/

HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 10:40:25 GMT
Content-Type: application/json
Content-Length: 160

{
"id": 6,
"name": "Tur til søen",
"date": [
2024,
11,
5
],
"startTime": [
10,
0
],
"endTime": [
11,
0
],
"longitude": -122.4194,
"latitude": 37.7749,
"price": 100.0,
"category": "beach"
}
            
                PUT http://localhost:7070/api/trips/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:41:45 GMT
Content-Type: application/json
Content-Length: 160

{
"id": 1,
"name": "Spanien 2025",
"date": [
2025,
11,
6
],
"startTime": [
10,
30
],
"endTime": [
11,
30
],
"longitude": -122.4194,
"latitude": 37.7749,
"price": 120.0,
"category": "city"
}

                DELETE http://localhost:7070/api/trips/2

HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 10:42:38 GMT
Content-Type: text/plain

Response body is empty

                POST http://localhost:7070/api/trips/populate

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:43:19 GMT
Content-Type: text/plain
Content-Length: 0

Response body is empty

                PUT http://localhost:7070/api/trips/3/guides/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:44:54 GMT
Content-Type: application/json
Content-Length: 156

{
"id": 3,
"name": "Tivoli",
"date": [
2024,
11,
7
],
"startTime": [
9,
30
],
"endTime": [
10,
30
],
"longitude": -87.623177,
"latitude": 41.881832,
"price": 175.0,
"category": "snow"
}

                GET http://localhost:7070/api/trips/filter/beach

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 10:57:31 GMT
Content-Type: application/json
Content-Length: 162

[
{
"id": 2,
"name": "BonBon Land",
"date": [
2024,
11,
6
],
"startTime": [
14,
0
],
"endTime": [
15,
0
],
"longitude": -73.935242,
"latitude": 40.73061,
"price": 200.0,
"category": "beach"
}
]

    Theoretical question: Why do we suggest a PUT method for adding a guide to a trip instead of a POST method?
The PUT method is used to update the resource, in this case, the trip. The guide is a part of the trip, so it makes sense to update the trip with the guide. The POST method is used to create a new resource, so it would not make sense to use it to add a guide to a trip.

