Multithreaded AWS Deployed Server

This project is a multithreaded java server which uses the AWS SDK to access java resources. The server uses resources including an RDS MySQL database and S3 objects. The client sends requests for lookups from, and updates to, the MySQL database. This project was for experimentation purposes, and the AWS resource identifiers contained in the code and properties files are no longer valid.

**AWS Client**
This module contains a single class, called projectClient.
The client connects to the server over a TCP connection, to send requests and receive response messages through the command line.

**AWS Server**
This module contains the code for the server to which the client connects and contains the following classes.

**ServerDriver**
This class contains the main method of the server-side maven project. It first updates a table in the RDS database from a file of coma separated values which is stored in S3.  The main method then enters an infinite loop, accepting incoming TCP connections form clients, and spinning off a new thread to service the client. 
 **ServerThread**
 This class implements Runnable, and its run() method. The run() method prints the IP address of the client the Socket is connected to , and then sends a welcome message to the client to confirm the connection.  Usernames and passwords are also stored in the RDS MySQL database, and the thread prompts the client to enter a username and password, which is checked for validity. After authentication, upon receiving requests from the client, which can be entered in the command prompt using the client module, the server adds or retrieves records to and from the table. 

**SQLManager**

This class contains methods to query the database as necessary, and to look up a username and password provided by a client.  All is done using JDBC.

 **S3 Manager**
 This class contains methods to get an input stream for a coma seperated text file, and to add entries to the file, using the bucket name and file name provided in s3.properties. 