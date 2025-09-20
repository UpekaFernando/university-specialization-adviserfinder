# 🎯 Quick Postman Testing Reference

## 📱 **Application Status:** ✅ RUNNING on http://localhost:8080

---

## 🚀 **Screenshot 5: Student API Endpoint Tests - Postman**

### **Quick Setup for Postman Screenshot:**

### **1. Environment Variables**
```
baseUrl: http://localhost:8080
```

### **2. Test These Endpoints:**

#### **📝 POST Create Student**
```
URL: http://localhost:8080/api/students
Method: POST
Content-Type: application/json

Body:
{
  "firstName": "John",
  "lastName": "Doe", 
  "email": "john.doe@university.edu",
  "studentId": "STU001",
  "program": "Computer Science",
  "year": 3,
  "gpa": 3.85
}

Expected: 201 Created
```

#### **📋 GET All Students**
```
URL: http://localhost:8080/api/students
Method: GET

Expected: 200 OK with array of students
```

#### **🔍 GET Student by ID**
```
URL: http://localhost:8080/api/students/1
Method: GET

Expected: 200 OK with student object
```

#### **✏️ PUT Update Student**
```
URL: http://localhost:8080/api/students/1
Method: PUT
Content-Type: application/json

Body:
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@university.edu", 
  "studentId": "STU001",
  "program": "Computer Science",
  "year": 4,
  "gpa": 3.92
}

Expected: 200 OK with updated data
```

#### **❌ POST Invalid Email (Error Test)**
```
URL: http://localhost:8080/api/students
Method: POST
Content-Type: application/json

Body:
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "invalid-email",
  "studentId": "STU002", 
  "program": "Mathematics",
  "year": 2
}

Expected: 400 Bad Request with error message
```

---

## 📊 **Postman Tests to Add:**

### **For Each Request - Add these in Tests tab:**

```javascript
// Status Code Test
pm.test("Status code is correct", function () {
    pm.response.to.have.status(201); // Change to expected code
});

// Response Time Test  
pm.test("Response time < 2000ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});

// JSON Response Test
pm.test("Response is JSON", function () {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("application/json");
});

// Data Validation Test
pm.test("Response contains required fields", function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson).to.have.property('id');
    pm.expect(responseJson).to.have.property('firstName');
});
```

---

## 📸 **Screenshot Elements to Show:**

✅ **Request Panel:**
- HTTP Method (POST/GET/PUT)
- URL with localhost:8080
- Headers (Content-Type)
- JSON Request Body

✅ **Response Panel:**
- Status Code (201, 200, 400)
- Response Time
- JSON Response Data
- Headers

✅ **Tests Panel:**
- Test assertions
- Pass/Fail indicators (green checkmarks)
- Test execution results

✅ **Collection View:**
- Multiple API requests organized
- Request names and methods

---

## 🎯 **Key Assertions to Highlight:**

1. **Response Code:** 201 Created, 200 OK, 400 Bad Request
2. **Response Payload:** JSON structure validation
3. **Data Validation:** Field presence and correctness  
4. **Performance:** Response time under 2000ms
5. **Error Handling:** Proper error messages for invalid data

---

**Ready for Screenshot 5!** Open Postman and test these endpoints to create your API testing demonstration.