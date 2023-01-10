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
| GET     | /api/admin/user/       | -         | -    | -          | +             | -       | Displays information about all users       |
| PUT     | /api/admin/user/role   | -         | -    | -          | +             | -       | Changes user role                          |
| PUT     | /api/admin/user/access | -         | -    | -          | +             | -       | Lock or unlock user                        |
| DELETE  | /api/admin/user/:email | -         | -    | -          | +             | -       | Deletes a user by it's :email              |
| POST    | /api/acct/payments     | -         | -    | +          | -             | -       | Uploads payrolls                           |
| PUT     | /api/acct/payments     | -         | -    | +          | -             | -       | Updates payment information                |
| GET     | /api/empl/payment      | -         | +    | +          | -             | -       | Gives access to the employee's payrolls    |
| GET     | /api/security/events   | -         | -    | -          | -             | +       | Show security events of the service        |

# Examples
**Example 1:** POST /api/auth/signup <br/>
First user created on the system will get ADMINISTRATOR role, every other user created will get USER role. <br/>

Service will check are the all mandatory fields filled with data, as well as whether the user already exists in the system.

Password will be checked against a **set of breached passwords**. <br/>
For testing purposes, here is the list of breached passwords:
```
"PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril", 
"PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust", 
"PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
```
In case of error, response with appropriate HTTP status will be returned, with the following body structure:
```
{
  "timestamp": "<date>",
  "status": <HTTP status>,
  "error": "<Error name>",
  "message": "<Error description>",
  "path": "<api>"
}
```

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
Request body:
```
{
   "name": "Jane",
   "lastname": "Doe",
   "email": "jane.doe@acme.com",
   "password": "123456789ABC"
}
```
Response: 200 OK <br/>
Response body:
```
{
    "id": 2,
    "name": "Jane",
    "lastname": "Doe",
    "email": "jane.doe@acme.com",
    "roles": [
        "ROLE_USER"
    ]
}
```

**Example 2:** POST /api/auth/changepass <br/> 
With the correct authentication: username = "john.doe@acme.com", password = "123456789ABC". <br/>

New password must be different from the old one. <br/>
Password will be checked against a **set of breached passwords**. <br/>

Request body:
```
{
    "new_password": "123456789DEF"
}
```
Response: 200 OK <br/>
Response body:
```
{
    "email": "john.doe@acme.com",
    "status": "The password has been updated successfully."
}
```
**Example 3:** GET/api/admin/user <br/>
With the correct authentication: username = "john.doe@acme.com", password = "123456789DEF". <br/>

Response: 200 OK <br/>
Response body:
```
[
  {
    "id": 1,
    "name": "John",
    "lastname": "Doe",
    "email": "john.doe@acme.com",
    "roles": [
      "ROLE_ADMINISTRATOR"
    ]
  },
  {
    "id": 2,
    "name": "Jane",
    "lastname": "Doe",
    "email": "jane.doe@acme.com",
    "roles": [
      "ROLE_USER"
    ]
  }
]
```
**Example 4:** PUT /api/admin/user/role <br/>
With the correct authentication: username = "john.doe@acme.com", password = "123456789DEF". <br/>
Request body:
```
{
  "user": "jane.doe@acme.com",
  "role": "ACCOUNTANT",
  "operation": "GRANT"
}
```
Response 200 OK <br/>
Response body:
```
{
  "id": 2,
  "name": "Jane",
  "lastname": "Doe",
  "email": "jane.doe@acme.com",
  "roles": [
    "ROLE_ACCOUNTANT",
    "ROLE_USER"
  ]
}
```

Service recognises two groups of roles:
- administrative (ADMINISTRATOR) role and 
- business (USER, ACCOUNTANT and AUDITOR) roles.

Those two groups can not be combined, which means that the user from administrative group, 
can not have role from the business group and vice versa. In case it is attempted, bad request will be returned:
```
{
  "timestamp": "2023-01-10",
  "status": 400,
  "error": "Bad Request",
  "message": "The user cannot combine administrative and business roles!",
  "path": "/api/admin/user/role"
}
```
**Example 4:** PUT /api/admin/user/access <br/>
With the correct authentication: username = "john.doe@acme.com", password = "123456789DEF". <br/>
Request body:
```
{
   "user": "jane.doe@acme.com",
   "operation": "LOCK"
}
```
Response 200 OK <br/>
Response body:
```
{
  "status": "User jane.doe@acme.com is locked!"
}
```
Administrator can not be locked. In case it is attempted, bad request will be returned:
```
{
  "timestamp": "2023-01-10",
  "status": 400,
  "error": "Bad Request",
  "message": "Can't lock the ADMINISTRATOR!",
  "path": "/api/admin/user/access"
}
```
**Example 5:** DELETE /api/admin/user/jane.doe@acme.com <br/>
With the correct authentication: username = "john.doe@acme.com", password = "123456789DEF". <br/>

Response 200 OK <br/>
Response body:
```
{
  "user": "jane.doe@acme.com",
  "status": "Deleted successfully!"
}
```
Administrator can not be removed. In case it is attempted, bad request will be returned:
```
{
  "timestamp": "2023-01-10",
  "status": 400,
  "error": "Bad Request",
  "message": "Can't remove ADMINISTRATOR role!",
  "path": "/api/admin/user/john.doe@acme.com"
}
```