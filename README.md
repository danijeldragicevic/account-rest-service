# Account REST Service
Backend service who manages employee's accounts and their salaries. 
Service is able to:
- register new users, 
- manage their passwords and roles, 
- give access to the employee's payrolls,
- displays information about all users and
- log information security events.

Service use embedded H2 database and Basic authentication.

# Technology
- Java 11
- Spring Boot 2.7.1 (Spring Web MVC, Spring Data Jpa, Spring Security, Spring Validation, Project Lombok, H2 database)
- Gradle 7.4

# To run application:
Navigate to the project root directory and run **./gradlew bootRun**

# Exposed endpoints:
By default, service will run on **http://localhost:28852/** <br/>
Following endpoints will be exposed:

| Methods | Urls                   | Anonymous | User | Accountant | Administrator | Auditor | Action                                     |
|---------|------------------------|-----------|------|------------|---------------|---------|--------------------------------------------|
| GET     | /h2                    | +         | +    | +          | +             | +       | Access to the local database               |
| POST    | /h2                    | +         | +    | +          | +             | +       | Access to the local database               |
| POST    | /api/auth/signup       | +         | +    | +          | +             | +       | Allows the user to register on the service |
| POST    | /api/auth/changepass   | -         | +    | +          | +             | -       | Changes a user password                    |
| GET     | /api/empl/payment      | -         | +    | +          | -             | -       | Gives access to the employee's payrolls    |
| POST    | /api/acct/payments     | -         | -    | +          | -             | -       | Uploads payrolls                           |
| PUT     | /api/acct/payments     | -         | -    | +          | -             | -       | Updates payment information                |
| GET     | /api/admin/user/       | -         | -    | -          | +             | -       | Displays information about all users       |
| DELETE  | /api/admin/user/:email | -         | -    | -          | +             | -       | Deletes a user by it's :email              |
| PUT     | /api/admin/user/role   | -         | -    | -          | +             | -       | Changes user role                          |
| PUT     | /api/admin/user/access | -         | -    | -          | +             | -       | Lock or unlock user                        |
| GET     | /api/security/events   | -         | -    | -          | -             | +       | Show security events of the service        |

# Examples
**Example 1:** POST /api/auth/signup <br/>
First user created on the system will get ADMINISTRATOR role. <br/>
Request body:
```
{
   "name": "John",
   "lastname": "Doe",
   "email": "john.doe@acme.com",
   "password": "123456789ABC"
}
```
Response: 200 OK <br/>
Response body:
```
{
	"id": 1,
	"name": "John",
	"lastname": "Doe",
	"email": "john.doe@acme.com",
	"roles": [
		"ROLE_ADMINISTRATOR"
	]
}
```