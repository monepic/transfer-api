# Transfer API Case Study

## Installation Instructions
* **Prerequisites** Java(8 or later), Maven
* Check out the project with git `git clone https://github.com/monepic/transfer-api.git`
* cd to the directory `cd transfer-api`
* either run directly with maven by typing `mvn`
  or
  -  build the jar file with `mvn clean package` which will also run the tests and package the jar
  -  run the jar file `java -jar target/transfer-api-0.0.1-SNAPSHOT.jar`
* the app is an executable Spring Boot jar and will run by default on http://localhost:8080 but that is configurable in the usual way

## API

### Supported Endpoints
#### Note: all endpoints are expecting the `Content-Type:application/json` header

`GET /accounts`  - provides a paged list of accounts. This supports optional paging and sorting parameters [as detailed here](https://docs.spring.io/spring-data/rest/docs/2.0.0.M1/reference/html/paging-chapter.html)
`POST /accounts` - creates a new account with inital balance. The expected format is `{"accountNumber":"<accountNumber>","openingBalance":<openingBalance>}` This sets a location header to indicate how to access the newly created account.

`GET /accounts/<accountNumber>` - returns the account with the given accountNumber

`GET /transactions` - provides a paged list of transactions. This supports optional paging and sorting parameters [as detailed here](https://docs.spring.io/spring-data/rest/docs/2.0.0.M1/reference/html/paging-chapter.html) 

`GET /transactions/<id>` - returns the transaction with the given id 

`POST /transfer` - this initiates a balance transfer from one account to another. The expected format is `{"sourceAccountNumber":"<sourceAccountNumber>","destinationAccountNumber":"<destinationAccountNumber>","amount":<amount>}` This returns a location header to indicate how to access the newly enacted transaction

### Examples
* Create an account
    `curl -H "Content-Type: application/json" --data '{ "accountNumber":"myNewAccount", "openingBalance": 10.11 }' localhost:8080/accounts -v'`
* Initiate a transfer
    `curl -H "Content-Type: application/json" --data '{ "sourceAccountNumber":"myNewAccount","destinationAccountNumber":"anotherAccount", "amount":5 }' localhost:8080/transfer -v

### Notes
* Syntactical errors in requests will return a **400** response and a JSON object containing vailidation errors.
* If the API is unable to comply with a syntactically correct request, it will return a **422** error and an associated error message.
* Currency values are **rounded down** to two decimal places - i.e. specifying a value of **123.456** will be understood as **123.45**
* There is no explicitly defined currency - it's assumed to be a generic currency with two decimal places

### Going Further
Some suggestions for improvements include
* Using the new Java [Money and Currency API](https://jcp.org/en/jsr/detail?id=354)
* Implementing a [Double-Entry Bookkeeping](https://en.wikipedia.org/wiki/Double-entry_bookkeeping) model
* Implementing security, internal/external transfers, soft account deletion and support for different currencies

