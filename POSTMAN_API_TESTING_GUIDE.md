# 🚀 University Advisor Finder - Postman API Testing Guide

## 📋 API Endpoints Testing with Postman

### 🎯 **Screenshot 5: Student API Endpoint Tests - Postman Collection**

---

## 📝 **Postman Collection Setup Instructions**

### **Collection Name:** `University Advisor Finder API Tests`
### **Base URL:** `http://localhost:8080`

---

## 🔧 **1. Student API Endpoints**

### **POST /api/students - Create New Student**

**Request Configuration:**
```
Method: POST
URL: {{baseUrl}}/api/students
Headers:
  Content-Type: application/json
```

**Request Body (JSON):**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@university.edu",
  "studentId": "STU2024001",
  "program": "Computer Science",
  "year": 3,
  "gpa": 3.85
}
```

**Expected Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@university.edu",
  "studentId": "STU2024001",
  "program": "Computer Science",
  "year": 3,
  "gpa": 3.85,
  "createdAt": "2024-09-14T09:30:00"
}
```

**Postman Tests (Add in Tests tab):**
```javascript
// Test 1: Status Code Assertion
pm.test("Status code is 201 Created", function () {
    pm.response.to.have.status(201);
});

// Test 2: Response Time Assertion
pm.test("Response time is less than 2000ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});

// Test 3: Response Body Structure
pm.test("Response has required fields", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson).to.have.property('id');
    pm.expect(responseJson).to.have.property('firstName');
    pm.expect(responseJson).to.have.property('email');
    pm.expect(responseJson).to.have.property('studentId');
});

// Test 4: Data Validation
pm.test("Student data is correct", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.firstName).to.eql("John");
    pm.expect(responseJson.lastName).to.eql("Doe");
    pm.expect(responseJson.email).to.eql("john.doe@university.edu");
    pm.expect(responseJson.program).to.eql("Computer Science");
});

// Test 5: Save student ID for subsequent tests
pm.test("Save student ID", function () {
    const responseJson = pm.response.json();
    pm.globals.set("studentId", responseJson.id);
});
```

---

### **GET /api/students/{id} - Retrieve Student by ID**

**Request Configuration:**
```
Method: GET
URL: {{baseUrl}}/api/students/{{studentId}}
Headers: None required
```

**Expected Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@university.edu",
  "studentId": "STU2024001",
  "program": "Computer Science",
  "year": 3,
  "gpa": 3.85,
  "active": true
}
```

**Postman Tests:**
```javascript
// Test 1: Status Code
pm.test("Status code is 200 OK", function () {
    pm.response.to.have.status(200);
});

// Test 2: Content Type
pm.test("Response is JSON", function () {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("application/json");
});

// Test 3: Student ID Match
pm.test("Student ID matches request", function () {
    const responseJson = pm.response.json();
    const requestedId = pm.globals.get("studentId");
    pm.expect(responseJson.id).to.eql(parseInt(requestedId));
});

// Test 4: Required Fields Present
pm.test("All required fields are present", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson).to.have.all.keys(
        'id', 'firstName', 'lastName', 'email', 
        'studentId', 'program', 'year', 'gpa', 'active'
    );
});
```

---

### **GET /api/students - Get All Students**

**Request Configuration:**
```
Method: GET
URL: {{baseUrl}}/api/students
Headers: None required
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@university.edu",
    "program": "Computer Science"
  }
]
```

**Postman Tests:**
```javascript
// Test 1: Status Code
pm.test("Status code is 200 OK", function () {
    pm.response.to.have.status(200);
});

// Test 2: Response is Array
pm.test("Response is an array", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson).to.be.an('array');
});

// Test 3: Array Contains Students
pm.test("Array contains at least one student", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.length).to.be.at.least(1);
});

// Test 4: Student Object Structure
pm.test("Each student has required fields", function () {
    const responseJson = pm.response.json();
    responseJson.forEach(student => {
        pm.expect(student).to.have.property('id');
        pm.expect(student).to.have.property('firstName');
        pm.expect(student).to.have.property('email');
    });
});
```

---

### **PUT /api/students/{id} - Update Student**

**Request Configuration:**
```
Method: PUT
URL: {{baseUrl}}/api/students/{{studentId}}
Headers:
  Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@university.edu",
  "studentId": "STU2024001",
  "program": "Computer Science",
  "year": 4,
  "gpa": 3.92
}
```

**Postman Tests:**
```javascript
// Test 1: Status Code
pm.test("Status code is 200 OK", function () {
    pm.response.to.have.status(200);
});

// Test 2: Updated Fields
pm.test("Student data updated correctly", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.year).to.eql(4);
    pm.expect(responseJson.gpa).to.eql(3.92);
});

// Test 3: Unchanged Fields
pm.test("Unchanged fields remain same", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.firstName).to.eql("John");
    pm.expect(responseJson.email).to.eql("john.doe@university.edu");
});
```

---

### **POST /api/students - Email Validation Test**

**Request Configuration:**
```
Method: POST
URL: {{baseUrl}}/api/students
Headers:
  Content-Type: application/json
```

**Request Body (Invalid Email):**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "invalid-email-format",
  "studentId": "STU2024002",
  "program": "Mathematics",
  "year": 2
}
```

**Postman Tests:**
```javascript
// Test 1: Status Code for Validation Error
pm.test("Status code is 400 Bad Request", function () {
    pm.response.to.have.status(400);
});

// Test 2: Error Message Present
pm.test("Error message contains validation info", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson).to.have.property('message');
    pm.expect(responseJson.message).to.include('email');
});

// Test 3: Response Time
pm.test("Validation response is fast", function () {
    pm.expect(pm.response.responseTime).to.be.below(1000);
});
```

---

## 🎯 **Screenshot Preparation Instructions**

### **For Screenshot 5 - Student API Endpoint Tests:**

1. **Open Postman** and create new collection
2. **Set up Environment Variables:**
   - `baseUrl`: `http://localhost:8080`
   - `studentId`: (will be set dynamically)

3. **Create Requests in this order:**
   - POST Create Student
   - GET Student by ID  
   - GET All Students
   - PUT Update Student
   - POST Invalid Email Test

4. **Configure Tests for each request** using the JavaScript code above

5. **Run Collection** and capture screenshots showing:
   - Request configuration (URL, method, body)
   - Response data (JSON)
   - Test results (green checkmarks)
   - Response time and status codes

### **Key Elements to Show in Screenshot:**
- ✅ **Request Panel** - Method, URL, Headers, Body
- ✅ **Response Panel** - Status code, response time, JSON data
- ✅ **Tests Panel** - Test assertions with pass/fail status
- ✅ **Collection Runner** - Multiple test execution results

---

## 📊 **Expected Test Results Summary**

| Endpoint | Method | Status Code | Tests | Pass Rate |
|----------|--------|-------------|-------|-----------|
| /api/students | POST | 201 | 5 tests | 100% |
| /api/students/{id} | GET | 200 | 4 tests | 100% |
| /api/students | GET | 200 | 4 tests | 100% |
| /api/students/{id} | PUT | 200 | 3 tests | 100% |
| /api/students (invalid) | POST | 400 | 3 tests | 100% |

**Total: 19 tests, 19 passed, 0 failed**

---

## 💡 **Postman Collection Export**

After creating your collection, export it as JSON for sharing:
1. Right-click collection → Export
2. Choose Collection v2.1
3. Save as `University-Advisor-Finder-API-Tests.postman_collection.json`

This documentation provides everything you need for comprehensive API testing screenshots with Postman!