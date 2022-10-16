# Test task

1. Run mongo instance `docker-compose up -d`
2. Build the jar `mvn clean package`
3. Run the jar `java -jar target/DocuSketchTestTask-0.0.1-SNAPSHOT.jar`

Database will be populated with user 

username: admin password: admin role ROLE_ADMIN

and roles: ROLE_ADMIN and ROLE_USER

All new created users have ROLE_USER. No username duplications allowed

Swagger is available at http://localhost:8080/swagger-ui.html#

Security:
1. New pair of tokens can be generated only with refresh token.
2. All endpoints except /auth/token are accessable by an access token
3. DELETE /blogs/id can be executed only with ROLE_ADMIN role
4. PUT /blogs/id can be executed only by a user who made this blog
5. DELETE /users/id can be executed only with ROLE_ADMIN role
