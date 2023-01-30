# SPMSMobile
Muhammad Amirul Asraf bin Mustafa
B032110463

Application: SPMS mobile

Student Performance Monitoring System (SPMS) is a system which provide functionaility to manage
school, teachers and students information. While the main focus of the system is question bank
repositories, assessment management and performance report. The main system is a web-based application
which is served by REST back-end server application devloped using NodeJS with ExpressJs framework

Generally, the system is used by three group of users which is admin, teacher and students.
This application however is designed to be used by only students
SPMS mobile is an android application which allows students to answer assessments assgined to them
through their android devices for better mobility.
the application shares the same database with the main system which is managed using the MySql RDBMS
SPMS mobile interacts with the system through REST application program interface in the NodeJs server
the application uses token based authentication with JSON Web Token.

Functionality:
- Login
- View Assessment
- Attempt Assessment
- view Mark

Development Stack
- Java (android application)
- NodeJS (server application)
- MySql (database)

Table/Object
The main system database consist of 27 table, however the mobile version application only utilize 
- account    querying during login
- assessment queried for assessment listing
- assigned_question inserted during start of attempt & anser submission
question related data queried from:
- question
- question_answer
- question_appendix
- attachment

Dependencies
- volley: to create & handle http request 
- sweetalert: to show alert and dialogs
- security-crypto: to encrypt shared preference 
- joda: to handle DateTime
- glide: to handle image loading
