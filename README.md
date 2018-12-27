# WAES Diff Service

## Usage
The easiest way of testing this application is by running the following command:
```
mvn clean package -Dspring.profiles.active=local spring-boot:run
```

It will spin up a server running on the port 8080.

You have also the option of running it standalone independently of maven by doing:
```
mvn clean install
```
and then...
```
java -jar -Dspring.profiles.active=local ./target/waes-diff-service-0.0.1-SNAPSHOT.jar
```

As this is an executable ZIP file, it can be delivered as is.

## Assumptions

* It was assumed I would not have to thing about the rules of doing a diff in two contents, so I imported a public implementation/library from google as a maven dependency. (you can check it out here: https://github.com/google/diff-match-patch)
* I assumed I could set an embedded mongoDB to avoid problems to whoever is gonna test it, so the performance will obviously decrease.
* I haven't seen so much sense on limiting the entries comparing by size as the exercise suggests, so I'm allowing any content length.
* I'm limiting the possible IDs to UUIDs compatible only. Other ids will throw an error asking for UUIDs.
* I assumed the input was gonna be base64 but the output would be a human readable text, so I'm converting it to plain text again.


## Improvements
* Add more unit tests
* Separate validation tests from the other functional tests to make them more consistent and maintainable.
* Add Log/traceability to the application.
* Make the application production ready removing embedded mongoDB and enabling the spring data connection to a real mongodb or any other database server.

_by Rômero Ricardo._