
# Chat Backend

## Steps 
1. Clone the project from github.
2. Import the project into IDE as a 'Existing Maven Project'. 
3. Build the project.
4. Create a database called 'asapp' in a local mysql database.
5. Run the `schema.sql`. 
  Ex:- `mysql -u < username > -p < db-name > < schema.sql`;
6. In `src/hibernate.cfg.xml` file, set `connection.username` and `connection.password` property to the local db username and password. 
7. Start the Tomcat server on localhost port 8080.
8. Set the values(Ex: username, password, messageSender, message receiver)  to enter in the db in `cfg.properties` file. 
9. Run the `BackendClient.java` class. 
10. `mvn install` will run all the test cases.

## Execution Flow Chart
  ![alt tag](https://github.com/rahulredd/ChatBackend/blob/master/flowchart.png)
  
##  Tools and Technologies Used
1. Hibernate 5.0
2. Junit 4.8
3. MySQL 5.1.6
4. Jbcrypt 0.3m
5. Jersey 1.9
6. Java 7
7. H2 database 1.4
8. Slf4j 1.6.6
9. Maven 3.3
10. Jdom 1.1
