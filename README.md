# fabflix

This application is intended to be deployed on Apache Tomcat after being converted to a `.war` file after being imported as an Eclipse EE project.

In addition to this, the machine that this runs on must have port `3306` open for SQL connnections, and a mySQL user with username `mytestuser` and password `mypassword`. However, this can be modified in the `context.xml` file, allowing for variation in user and even the DB host (you may connect to a remote database using the `url` part of the Database resource file).

The included SQL scripts `createtable.sql` and `movie-data.sql` are to be run prior to using the database as well.
