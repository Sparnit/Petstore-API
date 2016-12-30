# Petstore API


## Build Project
Run `mvn clean install` to install all dependencies and to build the project. 

## Running Unit tests 

Run `mvn test` to run all test cases. 

## Starting up the application 

Install mongo db and run using the `mongod` command <br />
Start the server using the `mvn spring-boot:run` command <br />
Server will come up at the following domain when running locally `http://localhost:8080` <br />

To avoid installing mongo db an embedded mongo server can be used  <br />
Navigate to pom.xml and remove the scope element for the embedded mongo db to be used with the server <br />
```
<dependency>  <br />
	<groupId>de.flapdoodle.embed</groupId> <br />
	<artifactId>de.flapdoodle.embed.mongo</artifactId> <br />
	<version>1.50.2</version> <br />
	<scope>test</scope> <br />
</dependency> <br />
```
## API endpoings
To use the Pet Api endpoints an `Authorization` header must be passed in <br />
to get a Authorization header value you must create the appropriate User Account with the right role<br />
Sample UserAccount creation <br />

`POST` for an admin user <br />
`http://localhost:8080/user` <br />
{<br />
    "id": 1,<br />
    "username": "userA",<br />
    "password": "password",<br />
    "role": "ADMIN"<br />
 }<br />
 
`POST` for an registered user <br />
`http://localhost:8080/user` <br />
{<br />
    "id": 2,<br />
    "username": "userB",<br />
    "password": "password",<br />
    "role": "USER"<br />
 }<br />

 To see all created users <br />
 `GET`<br />
 `http://localhost:8080/user/all`<br />
 
 ______________________________________
 To Retrieve a token for an account issue the following<br />
 
 `POST`<br />
 `http://localhost:8080/login`<br />
 
 {<br />
	"username":"userA",<br />
	"password":"password"<br />
 }<br />

 sample response<br />
 
 {<br />
  "token": "$2a$10$Ln.LpTnraWriY91jRzpL3uQhcBAoMtKCez88ukd7w94b/umvUeJzO",<br />
  "role": "ADMIN"<br />
 }<br />
 _________________________________________
 
 Pet API endpoints available<br />
 
 `GET`  required role any USER/ADMIN <br />
 Retrieves all Pets<br />
 `http://localhost:8080/pet/all`<br />


 `GET`  required role any USER/ADMIN <br />
 Retrieves a particular Pet by its Id<br />
 `http://localhost:8080/pet/1`<br />
 
 `DELETE`  required role ADMIN <br />
 Deletes a particular Pet by its Id<br />
 `http://localhost:8080/pet/1`<br />
 
 
 `POST`  required role ADMIN <br />
 Creates a particular Pet <br />
 Pet object model available at `http://petstore.swagger.io/`<br />
 
 `http://localhost:8080/pet` <br />
 


