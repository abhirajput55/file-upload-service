# file-upload-service

 Instructions on How to Run and Test the Microservice

# Prerequisites
Ensure you have the following installed:
- Java 1.8 (Ensure `JAVA_HOME` is set)
- Maven (For building the project)
- PostgreSQL 15.10 (Ensure the database is running)
- Postman or Curl (For testing API endpoints)

# Step 1: Clone the Repository
```sh
git clone https://github.com/abhirajput55/file-upload-service.git
cd file-upload-service
```

# Step 2: Configure the Database
Update `application.properties` in `src/main/resources`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/file_upload_service_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
```
Create the database manually in PostgreSQL:
```sql
CREATE DATABASE file_upload_service_db;
```

# Step 3: Build and Run the Microservice
Run as spring boot app.

# Step 4: Testing the Endpoints
## Authentication: Get JWT Token
Use Postman or Curl to request a token:
```sh
curl -X POST http://localhost:8080/fileuploadservice/api/v1/auth/login 
```
Request:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
Response:
```json
{
  "SUCCESS": "your-jwt-token",
  "TOKEN": "your-jwt-token"
}
```

## CSV Upload API (Admin Only)
Use the JWT token obtained in the previous step to upload a CSV file:
```sh
curl -X POST http://localhost:8080/fileuploadservice/api/v1/upload/csv
     -H "Authorization: Bearer your-jwt-token" \
     -H "Content-Type: multipart/form-data" \
     -F "file=@yourfile.csv"
```
Expected Response:
```json
"File uploaded and processed successfully."
```

---

 CSV File Format and Data Processing Rules
# CSV File Format
The CSV file must follow this structure:
```
EmployeeID,ReviewDate,Goal,Achievement,Rating,Feedback
1001,2024-02-01,Increase Sales,Achieved 20% growth,5,Excellent work
1002,2024-02-02,Improve Client Relations,Positive feedback from clients,4,Good job
```

# Data Processing Rules
1. Field Extraction:
   - `EmployeeID` → Long (Required)
   - `ReviewDate` → Converted to UTC format
   - `Goal` → String (Required)
   - `Achievement` → String (Optional)
   - `Rating` → Integer (Required, 1-5)
   - `Feedback` → String (Optional)

2. Validations:
   - EmployeeID must be a valid number.
   - ReviewDate must be a valid date in `yyyy-MM-dd` format.
   - Rating must be an integer between `1` and `5`.
   - Missing required fields will result in an error.

3. Transformation:
   - `ReviewDate` is converted to UTC before saving.
   - Any invalid data will trigger an exception.

4. Database Handling:
   - Data is inserted in batches for efficiency.
   - Duplicate entries are updated if `EmployeeID` already exists.
