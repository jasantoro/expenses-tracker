expenes-tracker
===============

## Prerequisites

* Download & Install Java 8 [from here](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Install MySQL Database

## Database configuration

* Create a database called *expenses-app*.

You can modify the existent persistent configuration changing the persistence.properties file into the application. 
When the application starts creates the different tables.

## Build the application

```
cd expenses-tracker
mvn clean package assembly:single
```

## Build the application

```
cd expenses-tracker/target
cp expenses-tracker-dist.tar ~/.
cd ~/
tar -xvf expenses-tracker-dist.tar
```

After done those steps the tar distribution file is copied into your HOME directory and unpacked into expenses-tracker directory.

## Run Application

To run the application expand the tar.

```
cd expenses-tracker
java -jar expenses-tracker <PORT>
```
