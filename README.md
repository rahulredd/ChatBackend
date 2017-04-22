
# Chat Backend

## Steps 
1. Clone the project from github.
2. Import the project into IDE as a 'Existing Maven Project'. 
3. Build the project.
4. Create a database called 'asapp' in a local database.
5. Run the `schema.sql`. 
  Ex:- `mysql -u < username > -p < db-name > < schema.sql`;
6. In `src/hibernate.cfg.xml` file, set `connection.username` and `connection.password` property to the local db username and password. 
7. Start the Tomcat server on localhost port 8080.
8. Set the values(Ex: username, password, messageSender, message receiver)  to enter in the db in `cfg.properties` file. 
9. Run the `BackendClient.java` class. 
10. `mvn install` will run all the test cases.
