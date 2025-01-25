## About the Project
AutoVault provides a platform where users can sign up, log in, and manage their vehicles collections. Users can upload, edit, and delete vehicle details and associated documents. 
Search and sort functionalities allow efficient vehicles management, while secure authentication ensures data protection.

## Built With
- Spring Boot
- Hibernate
- JDBC UserDetailsManager
- JSP
- JPQL
- JSTL
- Docker
- MySQL

### Usage

#### User Perspective:
1. **User Registration and Login:**
   - Create an account or log in with existing credentials.
   - Passwords are securely managed for data protection.
2. **User Profile Management:**
   - Users can update their profile information, including: Email address, date of birth, first name, last name, and password.
   - Users also have the option to delete their accounts entirely, ensuring full control over their data.
3. **Dashboard Overview:**
   - Upon logging in, users land on a dashboard displaying their car collection.
   - Users can add, view, update, or delete vehicles.
4. **Core Functionalities:**
   - **Add a Vehicle:**
     - Click the "Add Vehicle" button to fill out a form with vehicle details like make, model, year, and upload associated documents (e.g., registration, insurance).
   - **Edit/Delete Vehicle and Documents Details:**
     - Select a vehicle from the list to update its information or delete it. Multiple vehicles could also be selected and deleted at the same time.
     - Documents associated with each vehicle can be managed:
         - New documents can be added.
         - Existing documents can be deleted individually.
   - **Search and Sort:**
     - Use 'Search Vehicle in the navigation bar to filter vehicles by make, model or other attributes
     - Use 'Search Document' to search for cars by document criteria, such as document name, extension, or both. The app also supports case-sensitive searches for document names.
     - Vehicles can be sorted by attributes like purchased date, make, year or others.
5. **Validation:**
   - The app validates input to ensure data accuracy and integrity:
     - Prevents duplicate usernames or email addresses, invalid email patterns, and future date of bith during signup.
     - Displays errors for invalid car detail fields, such as incorrect number formats, future dates for the purchase date, or invalid file extensions for document uploads.
     - Enforces password strength requirements during signup: passwords must be at least 8 characters long and include at least two of the following—uppercase letters, lowercase letters, numbers, or symbols (`!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~`).
     - If the selected make and model do not match, the app validates the input and provides an error message, ensuring accurate data entry.
     - Documents greater than 5MB will be rejected for upload
5. **Profile Management:**
   - Update personal profile details or delete the account from the profile settings page.

---

#### Technical Perspective:
1. **Account Creation & Authentication:**
   - User authentication is managed using **Spring Security** with a **JDBC UserDetailsManager**.
   - Passwords are hashed for security.
2. **Database Interaction:**
   - Vehicle and user data are stored in a **MySQL database**.
   - **Hibernate** and **JPQL** are used for database interaction.
3. **File Management:**
   - Documents are stored as a separate entity called `Documents`, with a ManyToOne relationship to the `Car` entity. Each `Car` can have multiple associated documents through a OneToMany relationship.
   - The `Documents` entity stores information such as file name, size, extension, type, and binary data (`@Lob`) in the database, along with the `car_id` foreign key linking it to the associated vehicle.
4. **Frontend-Backend Communication:**
   - The app uses **JSP pages** and **JSTL** for dynamic rendering, connected to backend services via **Spring MVC**.
   - Form submissions for adding/editing vehicles use POST requests with server-side validation.
   - The frontend is enhanced with JavaScript for interactivity.
5. **Validation:**
   - Validations are performed at field or object levels using `BindingResult` and `jakarta.validation.Valid` for username, password, email addresses, date of birth, purchase dates, car detail fields and document extensions 
6. **Containerized Deployment:**
   - The application is containerized using **Dockerfile** making it easy to deploy in various environments.
