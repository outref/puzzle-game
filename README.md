
# 🧩 Puzzle Game

Puzzle Game/Cutter/Solver with rectangular puzzle-pieces, made from your images, running on SpringBoot and Thymeleaf for dynamic front-end.

## 🚀 Demo
The application is deployed on AWS EC2, you can try it with this link:  
http://13.48.85.241:8090/  
Use credentials: Username - puzzleGame, Password - puzzleGame

## 🔐 Security
Application implements In-Memory authentication with a single configured user.  
(Security is needed mainly to be able to deploy the app safely)  

Please use these credentials:  
Username - puzzleGame  
Password - puzzleGame  

## 🎯 Features
- `/`   [Home] : display all uploaded pictures (puzzles) with links to play with them or download the Zip file with all puzzle-pieces in .jpg format.
- `/upload`  [Upload] : upload your JPG image and select how it will be sliced into puzzle tiles.
- `/game` [Game] : swap and rotate tiles on the table, check the solution correctness and display reference image
- `/solver`  [Puzzle Solver] : upload your puzzle-pieces (rectangular JPG images that are part of one sliced image). Puzzle tiles can be of any size, aspekt-ratio and not necessarily all equal (the only conditins are - JPG format, uprigth position, 5MB combined size). Algorithm works very well with quantities of up to 100 tiles, but can partially fail with bigger amounts.

## 📖 Structure
![structure](https://raw.githubusercontent.com/outref/readme-recources/main/puzzle-game-structure.jpg)
- <b> controller/ </b> - all the <b>@Controller</b>s serving the application's endpoints.
- <b> dto/ </b> - package for <b>DTO</b> classes.
- <b> exception/ </b> - holds all custom exception classes.
- <b> model/ </b> - model classes, main entities used in application: 
-- <b> GameBoard </b> encapsulates the information about all tiles on the board with other game-related information
-- <b> GameTile </b> keeps track of tile's image, position and rotation during the game.
-- <b> SolverTile </b> holds all the information about each tile that puzzle-solving algorithm needs to assemble the solution.
- <b> repository/ </b> - contains interface responsible for saving images as files.
- <b> service/ </b> - service layer of the application, all the logic is here.
- <b> PuzzleApplication </b> - @SpringBootApplication class with additional configuration.
- <b> StorageInitializr </b> - Bean that is responsible for creating a folder for keeping images on application startup.
- <b> WebSecurityConfig </b> - @Configuration class with security-config beans.
- <b> resources/static.styles/ </b> - CSS styles.
- <b> resources/templates/ </b> - Thymeleaf .html template files. 
- <b> application.properties </b> - properties used by SpringBoot to auto-configure the application.
 
Application is structured according to N-Tier structure pattern with Presentation, Service and Data layers.

## 🤖 Technologies
- <b>Java 17</b>
- <b>Spring Boot 3.1.0</b>
- <b> Thymeleaf </b>
- <b>Spring Security</b>
- Deployed on Amazon <b>AWS EC2</b> Linux instance.

## ⚙️ How to run locally
1.  Clone this repository to your local machine using `git clone`.
2.  Navigate to the project directory using `cd <path to directory>/puzzle-game`.
3.  Ensure that you have Java 17 installed on your system.
4. Build the application using `mvn clean install`.
5.  Start the application with `java -jar target/puzzle-0.0.1-SNAPSHOT.jar` command. Application will create the folder for keeping images next to the .jar file.
6.  Go to `http://localhost:8090/` and use provided credentials to login.
