@echo off
echo Building the project...
mvn clean package
mvn test
if %errorlevel% neq 0 (
    echo Build failed. Exiting...
    exit /b %errorlevel%
)

echo Running the JavaFX application...
mvn javafx:run
