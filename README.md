LyftDemo - By Zachary Nwabudike
===============================================

Lyft Bonus Question, Android implementation

Intro:
----------------------------------------------
This is the Lyft Programming Challenge:

"Calculate the detour distance between two different rides. Given four latitude / longitude pairs, 
where driver one is traveling from point A to point B and driver two is traveling from point C to point D,
write a function (in your language of choice) to calculate the shorter of the detour distances the drivers
would need to take to pick-up and drop-off the other driver."

This is my own Android implementation of a solution to the problem.  
It uses GoogleMaps API v3 and determines the shortest distance, time, 
and which path is most efficient for each cnosideration.

Contents:
-----------------------------------------------
DistanceToolTest - This is the Android JUnit Test Case project

LyftBonus - This is the library which contains the class to be tested.  
It has everything it needs to make calls to the web and run a test successfully.

Instructions:  
-----------------------------------------------
After importing this project and test case, set the variables in the init() method 
located in /LyftDemo/DistanceToolTest/com.drawingboardapps.lyfttest/DistanceToolTest.java

Add the LyftBonus project to the DistanceToolTest build path as a Library.

Run the test case.

Happy Coding!

++Zach Nwabudike
z.nwabudike@gmail.com
