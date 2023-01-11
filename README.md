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
| GET     | /h2                    | +         | +    | +          | +             | +       | Read from the local database               |
| POST    | /h2                    | +         | +    | +          | +             | +       | Write to the local database                |
| POST    | /api/auth/signup       | +         | +    | +          | +             | +       | Allows the user to register on the service |
| POST    | /api/auth/changepass   | -         | +    | +          | +             | -       | Changes a user password                    |
| GET     | /api/admin/user/       | -         | -    | -          | +             | -       | Displays information about all users       |
| PUT     | /api/admin/user/role   | -         | -    | -          | +             | -       | Changes user role                          |
| PUT     | /api/admin/user/access | -         | -    | -          | +             | -       | Lock or unlock user                        |
| DELETE  | /api/admin/user/:email | -         | -    | -          | +             | -       | Deletes a user by it's :email              |
| POST    | /api/acct/payments     | -         | -    | +          | -             | -       | Uploads payrolls for multiple users        |
| PUT     | /api/acct/payments     | -         | -    | +          | -             | -       | Updates payment information for one user   |
| GET     | /api/empl/payment      | -         | +    | +          | +             | +       | Gives access to the employee's payrolls    |
| GET     | /api/security/events   | -         | -    | -          | -             | +       | Show security events of the service        |

# Examples
**Example 1:** POST /api/auth/signup <br/>
First user created on the system will get ADMINISTRATOR role, every other user created will get USER role. <br/>
Additional roles can be provided to the user(s). <br/>

Service will check are all the fields filled with data, as well as whether the user already exists in the system.
Also service expects that every user have email of **acme.com** domain.

Password will be checked against a **set of breached passwords**. <br/>
For testing purposes, here is the list of breached passwords that is hardcoded in the application:
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
Request body (1st user):
```
{
  "name": "John",
  "lastname": "Doe",
  "email": "john.doe@acme.com",
  "password": "123456789ABC"
}
```
Response: 200 OK. <br/>
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
Request body (2nd user):
```
{
  "name": "Jane",
  "lastname": "Doe",
  "email": "jane.doe@acme.com",
  "password": "123456789ABC"
}
```
Response: 200 OK. <br/>
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
Request body (3rd user):
```
{
  "name": "Judy",
  "lastname": "Doe",
  "email": "judy.doe@acme.com",
  "password": "123456789ABC"
}
```
Response: 200 OK. <br/>
Response body:
```
{
  "id": 3,
  "name": "Judy",
  "lastname": "Doe",
  "email": "judy.doe@acme.com",
  "roles": [
    "ROLE_USER"
  ]
}
```
Request body (4th user):
```
{
  "name": "James",
  "lastname": "Doe",
  "email": "james.doe@acme.com",
  "password": "123456789ABC"
}
```
Response: 200 OK. <br/>
Response body:
```
{
  "id": 4,
  "name": "James",
  "lastname": "Doe",
  "email": "james.doe@acme.com",
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
Response: 200 OK. <br/>
Response body:
```
{
  "email": "john.doe@acme.com",
  "status": "The password has been updated successfully."
}
```
**Example 3:** GET/api/admin/user <br/>
With the correct authentication: username = "john.doe@acme.com", password = "123456789DEF". <br/>

Response: 200 OK. <br/>
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
  },
  {
    "id": 3,
    "name": "Judy",
    "lastname": "Doe",
    "email": "judy.doe@acme.com",
    "roles": [
      "ROLE_USER"
    ]
  },
  {
    "id": 4,
    "name": "James",
    "lastname": "Doe",
    "email": "james.doe@acme.com",
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
  "user": "judy.doe@acme.com",
  "role": "ACCOUNTANT",
  "operation": "GRANT"
}
```
Response 200 OK. <br/>
Response body:
```
{
  "id": 3,
  "name": "Judy",
  "lastname": "Doe",
  "email": "judy.doe@acme.com",
  "roles": [
    "ROLE_ACCOUNTANT",
    "ROLE_USER"
  ]
}
```
Request body:
```
{
  "user": "james.doe@acme.com",
  "role": "AUDITOR",
  "operation": "GRANT"
}
```
Response 200 OK. <br/>
Response body:
```
{
  "id": 4,
  "name": "James",
  "lastname": "Doe",
  "email": "james.doe@acme.com",
  "roles": [
    "ROLE_AUDITOR",
    "ROLE_USER"
  ]
}
```
Service recognises two groups of roles: 
- administrative role (ADMINISTRATOR) and 
- business roles (USER, ACCOUNTANT and AUDITOR).

Those two groups can not be combined, which means that the user from administrative group, 
can not have role from the business group and vice versa. In case it is attempted, bad request will be returned:
```
{
  "timestamp": "2023-01-11",
  "status": 400,
  "error": "Bad Request",
  "message": "The user cannot combine administrative and business roles!",
  "path": "/api/admin/user/role"
}
```
In case that unknown role or operation is requested, bad request similar to this will be returned:
```
{
  "timestamp": "2023-01-11",
  "status": 400,
  "error": "Bad Request",
  "message": "Operation field should be GRANT or REMOVE.",
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
Response 200 OK. <br/>
Response body:
```
{
  "status": "User jane.doe@acme.com is locked!"
}
```
Administrator can not be locked. In case it is attempted, bad request will be returned:
```
{
  "timestamp": "2023-01-11",
  "status": 400,
  "error": "Bad Request",
  "message": "Can't lock the ADMINISTRATOR!",
  "path": "/api/admin/user/access"
}
```
**Example 5:** DELETE /api/admin/user/jane.doe@acme.com <br/>
With the correct authentication: username = "john.doe@acme.com", password = "123456789DEF". <br/>

Response 200 OK. <br/>
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
  "timestamp": "2023-01-11",
  "status": 400,
  "error": "Bad Request",
  "message": "Can't remove ADMINISTRATOR role!",
  "path": "/api/admin/user/john.doe@acme.com"
}
```
**Example 6:** POST /api/acct/payments <br/>
With the correct authentication: username = "judy.doe@acme.com", password = "123456789ABC". <br/>

Request body:
```
[
  {
    "employee": "john.doe@acme.com",
    "period": "01-2021",
    "salary": 1234
  },
  {
    "employee": "judy.doe@acme.com",
    "period": "01-2021",
    "salary": 5678
  },
  {
    "employee": "james.doe@acme.com",
    "period": "01-2021",
    "salary": 9876
  }
]
```
Response 200 OK. <br/>
Response body:
```
{
  "status": "Added successfully!"
}
```
Service will check are all the fields filled with data. <br/>
Service will check are there duplicate entries, i.e. are there two salaries for the same user on a same month. <br/>
Also, service expects date in the format MM-YYYY and salary to be non-negative number. <br/>

In case it is attempted, similar error will be thrown:
```
{
  "timestamp": "2023-01-11",
  "status": 400,
  "error": "Bad Request",
  "message": "payments[0].employee: Employee can not be empty!, payments[1].period: Wrong date!, payments[2].salary: Salary must be non negative!",
  "path": "/api/acct/payments"
}
```
**Example 7:** PUT /api/acct/payments <br/>
With the correct authentication: username = "judy.doe@acme.com", password = "123456789ABC". <br/>

Request body:
```
{
  "employee": "john.doe@acme.com",
  "period": "01-2021",
  "salary": 9999
}
```
Response 200 OK. <br/>
Response body:
```
{
  "status": "Updated successfully!"
}
```
Service will check are all the fields filled with data. <br/>
Service will check is there already existing payment that we want to update, i.e. payment for requested user on a specified month. <br/>
Also, service expects date in the format MM-YYYY and salary to be non-negative number. <br/>

In case it is attempted, similar error will be thrown:
```
{
  "timestamp": "2023-01-11",
  "status": 400,
  "error": "Bad Request",
  "message": "Payment does not exists!",
  "path": "/api/acct/payments"
}
```
**Example 8:** GET /api/empl/payment <br/>
With authentication credentials of existing user, e.g. username = "john.doe@acme.com", password = "123456789DEF". <br/>

Response 200 OK.
Response body:
```
[
  {
    "name": "John",
    "lastname": "Doe",
    "period": "January-2021",
    "salary": "99 dollar(s) 99 cent(s)"
  }
]
```
Service will respond with the salary information for the user who requested it. <br/>

If user does not exist or entered credentials are not correct, error will be thrown:
```
{
  "timestamp": "2023-01-11",
  "status": 401,
  "error": "Unauthorized",
  "message": "Username or password are not correct!",
  "path": "/api/empl/payment"
}
```
**Example 9:** GET /api/security/events <br/>
With the correct authentication: username = "james.doe@acme.com", password = "123456789ABC". <br/>

Application logs following security events:

| Description                                                  | Event name      |
|--------------------------------------------------------------|-----------------|
| A user has been successfully registered                      | CREATE_USER     |
| A user has changed the password successfully                 | CHANGE_PASSWORD |
| A user is trying to access a resource without access rights  | ACCESS_DENIED   |
| Failed authentication                                        | LOGIN_FAILED    |
| A role is granted to a user                                  | GRANT_ROLE      |
| A role has been revoked                                      | REMOVE_ROLE     |
| The Administrator has locked the user                        | LOCK_USER       |
| The Administrator has unlocked the user                      | UNLOCK_USER     |
| The Administrator has deleted a user	                        | DELETE_USER     |
| A user has been blocked on suspicion of a brute force attack | BRUTE_FORCE     |

If user made more than three failed login attempts, BRUTE_FORCE event will be registered and that user will be locked.

Response 200 OK. <br/>
Response body:
```
[
  {
    "id": 1,
    "date": "2023-01-11",
    "action": "CREATE_USER",
    "subject": "Anonymous",
    "object": "john.doe@acme.com",
    "path": "/api/auth/signup"
  },
  {
    "id": 2,
    "date": "2023-01-11",
    "action": "CREATE_USER",
    "subject": "Anonymous",
    "object": "jane.doe@acme.com",
    "path": "/api/auth/signup"
  },
  {
    "id": 3,
    "date": "2023-01-11",
    "action": "CREATE_USER",
    "subject": "Anonymous",
    "object": "judy.doe@acme.com",
    "path": "/api/auth/signup"
  },
  {
    "id": 4,
    "date": "2023-01-11",
    "action": "CREATE_USER",
    "subject": "Anonymous",
    "object": "james.doe@acme.com",
    "path": "/api/auth/signup"
  },
  {
    "id": 5,
    "date": "2023-01-11",
    "action": "CHANGE_PASSWORD",
    "subject": "john.doe@acme.com",
    "object": "john.doe@acme.com",
    "path": "/api/auth/changepass"
  },
  {
    "id": 6,
    "date": "2023-01-11",
    "action": "GRANT_ROLE",
    "subject": "john.doe@acme.com",
    "object": "Grant role ROLE_ACCOUNTANT to judy.doe@acme.com",
    "path": "/api/admin/user/role"
  },
  {
    "id": 7,
    "date": "2023-01-11",
    "action": "GRANT_ROLE",
    "subject": "john.doe@acme.com",
    "object": "Grant role ROLE_AUDITOR to james.doe@acme.com",
    "path": "/api/admin/user/role"
  },
  {
    "id": 8,
    "date": "2023-01-11",
    "action": "LOCK_USER",
    "subject": "john.doe@acme.com",
    "object": "Lock user jane.doe@acme.com",
    "path": "/api/admin/user/access"
  },
  {
    "id": 9,
    "date": "2023-01-11",
    "action": "DELETE_USER",
    "subject": "john.doe@acme.com",
    "object": "jane.doe@acme.com",
    "path": "/api/admin/user/jane.doe@acme.com"
  },
  {
    "id": 10,
    "date": "2023-01-11",
    "action": "LOGIN_FAILED",
    "subject": "judy.doe@acme.com",
    "object": "/api/acct/payments",
    "path": "/api/acct/payments"
  },
  {
    "id": 11,
    "date": "2023-01-11",
    "action": "LOGIN_FAILED",
    "subject": "judy.doe@acme.com",
    "object": "/api/acct/payments",
    "path": "/api/acct/payments"
  },
  {
    "id": 12,
    "date": "2023-01-11",
    "action": "LOGIN_FAILED",
    "subject": "judy.doe@acme.com",
    "object": "/api/acct/payments",
    "path": "/api/acct/payments"
  },
  {
    "id": 13,
    "date": "2023-01-11",
    "action": "BRUTE_FORCE",
    "subject": "judy.doe@acme.com",
    "object": "/api/acct/payments",
    "path": "/api/acct/payments"
  },
  {
    "id": 14,
    "date": "2023-01-11",
    "action": "LOCK_USER",
    "subject": "judy.doe@acme.com",
    "object": "Lock user judy.doe@acme.com",
    "path": "/api/acct/payments"
  }
]
```
# Licence
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)