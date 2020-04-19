# Apestronaut - Uridium

A 2D multiplayer fighting game developed in Java.

PLEASE SET YOUR COMPUTER DISPLAY LAYOUT AT 100%.

To run this game, it needs to connect to a MySQL Database first.
 
You need to set up Administration - Users and Privileges of the Database and run the src/GameDatabase/game.sql first, 

The default database setting we use is 

Login Name: Players
Password: Players@UK

Also, we customize an administrative role.

You can get more details in the Google Drive. 

Double-click to run ApestronautClient.jar , this will first need to connect to the database successfully using the settings upon, before launching the game.
Double-click to run ApestronautServer.jar will run the server in the background directly.

The game can be played on muliplayer only on the localhost if someone wants to play it outside they need to run the server on an external machine and connect to that IP address with all the clients

You can change settings in src/GameDatabase/jdbc.properties to adjust with your database settings when run the game using IDE.

The Client Main class is the src/managers/MenuController.java

The Server Main class is the src/GameServer/Server.java

Link to Google Drive: https://drive.google.com/drive/folders/11ek7OWPNVAjtRDFZ9rayHr9yuMyCmPbk?usp=sharing
