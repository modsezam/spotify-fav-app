# spotify-fav-app
Spotify application to search for favorite tracks and authors and save them in favorites.

### application description
An application that allows the user to search for artists and music tracks using the WEB API Spotify. It is also saving the indicated songs / artists to the local database.
The application was made in Spring Boot on the Thymeleaf template engine and additionally as the Rest API.

### technology stack
backend: java, spring boot
database: NoSQL nitrite database
frontend: Thymeleaf, Bootstrap


### run application
The application is available at:
http://localhost:8080

### API documentation
API documentation generated by the Swagger plugin is located on github in the src / main / resources / doc folder:
https://github.com/modsezam/spotify-fav-app/blob/master/src/main/resources/doc/html2-documentation.zip

### testing application API
The API itself can be tested via the Swagger v2 plugin available at:
http://localhost:8080/documentation/swagger-ui.html

### databse
NoSQL database used - nitite
https://github.com/dizitart/nitrite-database.
The database does not require installation of additional components and additionally stores the archive in the db-repository.db file located in the temporary files AppData \ Local \ Temp \.

### log management
Logs from the dancyh database are only kept for 1 hour and because the database does not have such functionality, the logs are remove by ScheduledCleaningLogService service which cleans the database from logs older than 1 hour every 100 seconds.
To write logs to a file I used Slf4j with the appender set to 60 files after 1 minute.
The current file path is declared in the application.properties file in the log.path variable.
The compressed files are located in the target/slf4j/roll-by-time folder.

### WEB API Spotify implementation
Communication with WEB API Spotify goes on via Client Credentials Flow path.
The generated Tocken is stored in SpotifyTokenHolder Bean. Because it is generated for 3600 seconds, the time of tocken generation is saved and if it is exceeded a new tocken is generated.

### screen shots
screen shot 1
![spotify-fav-app 1](https://github.com/modsezam/spotify-fav-app/blob/master/src/main/resources/images/spotify-sc-2.png)
screen shot 2
![spotify-fav-app 2](https://github.com/modsezam/spotify-fav-app/blob/master/src/main/resources/images/spotify-sc-3.png)
screen shot 3
![spotify-fav-app 3](https://github.com/modsezam/spotify-fav-app/blob/master/src/main/resources/images/spotify-sc-4.png)
screen shot 4
![spotify-fav-app 4](https://github.com/modsezam/spotify-fav-app/blob/master/src/main/resources/images/spotify-sc-5.png)
screen shot documentation
![spotify-fav-app documentaion](https://github.com/modsezam/spotify-fav-app/blob/master/src/main/resources/images/spotify-sc-1.png)
