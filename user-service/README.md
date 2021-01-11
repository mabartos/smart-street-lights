# User service

### Dev mode
Command for start this service (as all Quarkus app in dev mode):
```
mvn compile quarkus:dev
```

If you want to create default admin, just add flag `-DcreateAdmin=true` to the command, so:

```
mvn compile quarkus:dev -DcreateAdmin=true
```

### Production mode
In docker folder, just execute this fundamental command:
```
docker-compose up
```
Note: You can define your properties in `.env` file.

### Get token
There's an endpoint for getting access token.
You have to execute an endpoint `/api/auth/token` with `POST` method and in body, 
you have to include attributes `username` and `password`. 
Then, you will get the bearer token, which you should include in HTTP header as `Authorization: Bearer <token>`.

#####Example
If the default Admin user is created, you can do the following:
`POST` request to `localhost:8084/api/auth/token` with body 
```json
{
    "username":"admin",
    "password":"admin"
}
```
