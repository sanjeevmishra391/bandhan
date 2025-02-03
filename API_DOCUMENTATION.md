
# API Documentation

## Authentication Service

### Signup

**Endpoint:**  
`POST {{host}}/auth/signup`

**Description:**  
Registers a new user, encodes the password before saving, and sanitizes the user before sending the response.

**Validations:**
- Ensure the user with the given mobile number does not already exist.
- Ensure the user with the given email does not already exist.
- Validate required fields: `firstname`, `email`, `mobile`.

---

### Login

**Endpoint:**  
`POST {{host}}/auth/login`

**Description:**  
Generates a JWT token for authentication, valid for 1 hour.

**Validations:**
- Validate required fields: `mobile`, `password`.
- Ensure the user is active.
- Ensure the user exists in the system.

## User Service

### Get All Users

**Endpoint:**  
`GET {{host}}/users`

**Description:**  
Retrieves all active users.

---

### Search Users

**Endpoint:**  
`GET {{host}}/search?`

**Description:**  
Searches users based on query parameters. Returns only active users based on the provided search criteria.

**Allowed Query Parameters:**
- `firstname`
- `lastname`
- `mobile`
- `email`
- `address`

---

### Update User By ID

**Endpoint:**  
`PUT {{host}}/cust/{id}`

**Description:**  
Updates the user with the given ID. Only updates the fields that are provided (non-null values).

**Validations:**
- Ensure the user exists (search by `id`).
- Validate required fields: `firstname`, `lastname`, `mobile`, `email`, `address`.

---

### Delete User By ID

**Endpoint:**  
`DELETE {{host}}/cust/{id}`

**Description:**  
Soft deletes the user by marking their status as `DEACTIVATED`.

**Validations:**
- Ensure the user with the given `id` exists.

---