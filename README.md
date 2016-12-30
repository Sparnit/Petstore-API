# Petstore API


## Build Project
Run `mvn clean install` to install all dependencies and to build the project. 

## Running Unit tests 

Run `mvn test` to run all test cases. 

## Starting up the application 

Install mongo db and run using the `mongod` command
Start the server using the `mvn spring-boot:run` command
Server will come up at the following domain when running locally `http://localhost:8080`

To avoid installing mongo db an embedded mongo server can be used 
Navigate to pom.xml and remove the scope element for the embedded mongo db to be used with the server
<dependency>  
	<groupId>de.flapdoodle.embed</groupId>
	<artifactId>de.flapdoodle.embed.mongo</artifactId>
	<version>1.50.2</version>
	<scope>test</scope>
</dependency> 

## API endpoings
To use the Pet Api endpoints an `Authorization` header must be passed in
to get a Authorization header value you must create the appropriate User Account with the right role
Sample UserAccount creation

`POST` for an admin user
http://localhost:8080/user
{
    "id": 1,
    "username": "userA",
    "password": "password",
    "role": "ADMIN"
 }
 
`POST` for an registered user
http://localhost:8080/user
{
    "id": 2,
    "username": "userB",
    "password": "password",
    "role": "USER"
 }

 To see all created users 
 `GET`
 http://localhost:8080/user/all
 
 ______________________________________
 To Retrieve a token for an account issue the following
 
 `POST`
 http://localhost:8080/login
 
 {
	"username":"userA",
	"password":"password"
 }

 sample response
 
 {
  "token": "$2a$10$Ln.LpTnraWriY91jRzpL3uQhcBAoMtKCez88ukd7w94b/umvUeJzO",
  "role": "ADMIN"
 }
 _________________________________________
 
 Pet API endpoints available
 
 `GET`  required role any USER/ADMIN 
 Retrieves all Pets
 http://localhost:8080/pet/all


 `GET`  required role any USER/ADMIN 
 Retrieves a particular Pet by its Id
 http://localhost:8080/pet/1
 
 `DELETE`  required role ADMIN 
 Deletes a particular Pet by its Id
 http://localhost:8080/pet/1
 
 
 `POST`  required role ADMIN 
 Creates a particular Pet 
 Pet object model available at `http://petstore.swagger.io/`
 
 http://localhost:8080/pet
 


