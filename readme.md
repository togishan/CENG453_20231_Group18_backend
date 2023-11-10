mvn compile

---------

mvn exec:java -Dexec.mainClass="Application"

or

mvn spring-boot:run

or 

mvn spring-boot:run -e

or

mvn spring-boot:run -X

---------------

mvn clean
mvn install

-----------

login: Post
signup: Post

{
  "username": "testuser",
  "email": "testuser@example.com",
  "password": "testpassword"
}

userinfo: Get

header: Session-Key

resetPassword: Post

{
  "username": "testuser",
  "email": "testuser@example.com"
}

changePassword: Post

header: Reset-Key

{
  "username": "testuser",
  "email": "testuser@example.com",
  "password": "testpassword"
}
