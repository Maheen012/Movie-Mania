# Movie Mania

Movie Mania is a Java-based application for managing movies. It provides separate interfaces for **Admin** and **User** roles. Admins can add, delete, update, and view movies, while users can view movies and access additional features like favorites and watch history (placeholders).

---

## Features

### Admin Features
- **Add Movie**: Add a new movie with details like title, year, main cast, rating, genre, and description.
- **Delete Movie**: Delete a movie by its title.
- **Update Movie**: Update the details of an existing movie.
- **View Movies**: View a list of all movies.
- **Logout**: Log out

### User Features
- **View Movies**: View a list of all movies.
- **View Favorites**: Placeholder for viewing favorite movies (not implemented).
- **Watch History**: Placeholder for viewing watch history (not implemented).
- **Logout**: Log out

---

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 11 or higher.
- **Maven**: For building and managing dependencies.
  
---

## Setup Instructions

### 1. Clone the Repository
Clone the repository to your local machine:

```bash
git clone https://github.com/Maheen012/Movie-Mania.git
cd movie-mania
```

### 2. Import the Project into IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Click on ***Open**.
3. Navigate to the directory where you cloned the repository and select the root folder of the project (movie-mania).
4. Click **OK** to import the project.

IntelliJ IDEA will automatically recognize it as a Maven project and download the required dependencies.

### 3. Run the Main File
1. In IntelliJ, locate the `Main.java` file inside the `src/main/java/org/example` directory.
2. Right-click the file and select **Run 'Main'** or click the green play button on the top of the page.
3. Alternatively, you can press `Shift + F10` to run the application.

## Interact with the Application

After running the `Main.java` file, you'll be prompted with the login screen. Here's what to do next:

#### If you're logging in as a **User**:
1. You will need to **sign up** first before you can log in. Provide a **username** and **password** during sign-up.
2. Once signed up, log in with your username and password to access the user features.
3. You can **view all movies**.
4. Features like **viewing favorites** and **watch history** are placeholders and aren't implemented yet, but will be displayed as such.

#### If you're logging in as an **Admin**:
1. Use the following credentials to log in:
   - **Username**: `admin`
   - **Password**: `admin123`
2. As an admin, you will have access to the following features:
   - **Add Movie**: Add a new movie with details like title, year, main cast, rating, genre, and description.
   - **Delete Movie**: Delete a movie by its title.
   - **Update Movie**: Update the details of an existing movie.
   - **View Movies**: View a list of all movies.

### Logout

Once you're done interacting with the application, you can **log out** by clicking the **Logout** button, which will close the current session and terminate the application.



