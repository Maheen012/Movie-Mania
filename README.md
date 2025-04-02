# Movie Mania

Movie Mania is a GUI-based application that allows users to manage and interact with a collection of movies. Users can sign up, sign in, track watch history, manage their favorites list, and search movies. Admins have special privileges to add, update and delete movies from the catalogue. If users don't want to sign up, they can view our catalogue by logging in as a guest.

---

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 11 or higher.
- **Maven**: For building and managing dependencies.
- **Batch Files** are available on our repository to automatically run the application and tests.
  
---

## Setup Instructions

### 1. Clone the Repository
Clone the repository to your local machine:

```bash
git clone https://github.com/Maheen012/Movie-Mania.git
cd movie-mania
```

### 2. Run the Application
1. Navigate to the Build Files folder
2. Double-click on the **"build"** file to start the application.

### 3. Run the Tests
1. Navigate to the Build Files folder
2. Double-click on the **"test"** file to run the tests.

## Interact with the Application

### Sign Up:
- Enter a unique username and password.
- Click **Sign Up** to register.

### Sign In:
To sign in as **User**:
- Enter your username and password and click **Login**.
  
To sign in as **Guest**:
- Click the **Login as Guest** button.

To sign in as **Admin**, use the following credentials:
- **Username**: `admin`
- **Password**: `admin123`

---

### A. Admin Access
Admins have full control over the movie catalogue. Here's how to navigate through the application:

- **Add Movie**: Click on the **Add Movie** button, input movie details, and click **Add Movie** to add the movie to the catalogue.
- **View Movies**: Click on the **View Movies** button to update, delete, and view movies in the catalogue.
  - **Update Movie**: Select a movie from the list, click **Update Movie** to update movie details.
  - **Delete Movie**: Select a movie from the catalogue and click **Delete** to remove it from the catalogue.
- **Logout**: To logout, simply click the **Logout** button.

---

### B. User Access
Users have access to personalized features like watch history, favorites, and catalogue browsing. Here's how to navigate:

- **View Movies**: Click on any movie cover in the catalogue to view its details.
- **Search and Filter Movies**: Enter keywords and / or select different filters in the search bar to find movies.
- **Favorites List**: While viewing a movie, click the **Add to Favorites** button to save it in your favorites list. Then, view your saved favorites in **View Favorites**.
- **Watch History**: While viewing a movie, click the **Add to Watch History** button to save it in your watch history list. Then, view your changes in **Watch History**.
- **Logout**: To logout, simply click the **Logout** button.

---

### C. Guest Access
Guests have limited access to the system and can only browse the movie catalog. Guests can simply click the **Login as Guest** button on the main page and then they'll automatically be redirected to our movie catalogue for browsing movies.

## How the Code is Organized in Our Repository

The project is structured into three main components:

### **Controller**
- **UserManager.java**: Handles user data, favorites, and watch history.
- **MovieManager.java**: Handles the movie database.

### **Model**
- **User.java**: Represents the user object with properties like username and password.
- **Movie.java**: Represents the movie object with properties like movie ID, title, year, main cast, rating, genre, description and movie cover.

### **View**
- **AdminGUI.java**: Admin interface for managing movie catalogue, users, and movie operations (add, update, delete).
- **GuestGUI.java**: Guest interface for browsing movies and viewing movie details.
- **UserGUI.java**: User interface for logged-in users, displaying personal favorites, watch history, and browsing movies.
- **MovieViewer.java**: Displays the movie catalogue and its details when a movie is selected.
- **LoginGUI.java**: Handles user sign-up, sign-in, and authentication.

### **Main.java**
- The entry point to start the application and launch the appropriate GUI based on user role (Admin, User, Guest).




