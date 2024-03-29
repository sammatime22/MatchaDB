![MatchaDB Build Status](https://github.com/sammatime22/MatchaDB/actions/workflows/gradle.yml/badge.svg)



**Project Name**: MatchaDB

**Summary**: _A simple DB implementation that can take in a json doc and develop an API from said JSON doc._

**Status**: Beta v1.0.3

**Recent Updates**:

2021/06/12 - Set up new tests for MatchaDbTable class

2021/06/27 - Switched response code for Delete from OK (200) to NO_CONTENT (204)

2021/07/18 - Added in status badge

2021/08/08 - Ensured all Model "toString" methods use similar formatting

2021/11/13 - Changed Insert portion of POST Request from 2D Array to JSON Object

**System Requirements**

Java: 

    - java 14.0.1 2020-04-14

    - Java(TM) SE Runtime Environment (build 14.0.1+7)

    - Java HotSpot(TM) 64-Bit Server VM (build 14.0.1+7, mixed mode, sharing)

Gradle:

    - Gradle 6.6.1

    - Build time:   2020-08-25 16:29:12 UTC

    - Revision:     f2d1fb54a951d8b11d25748e4711bec8d128d7e3

    - Kotlin:       1.3.72

    - Groovy:       2.5.12

    - Ant:          Apache Ant(TM) version 1.10.8 compiled on May 10 2020

    - JVM:          14.0.1 (Oracle Corporation 14.0.1+7)


**Instructions on Running MatchaDB**

Upon building, run the following command:
    
    java -jar --enable-preview -DdatabaseFile=path/to/yourjsonfile.json -DdatabaseName="Your DB Name" build/libs/MatchaDB-<version>-SNAPSHOT.war
