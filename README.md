# Configuring a Katalon Studio project with a JSON file

This is a Katalon Studio project for demonstration purpose.
You can download the zip of this project from the [Release] page,
unzip it, open it with your local Katalon Studio.

This project was developed with Katalon Studio v8.3, but is
version-independent. It should work with any version.

I developed this project in the hope to propose a alternative
to the discussion in the Katalon User forum:

- https://forum.katalon.com/t/how-to-update-map-globalvariable-permanently/88025/5

## Problem to solve

## Solution

## Description of the demo implementation


## Conclusion

I think that the built-in features in Katalon Studio GUI around GlobalVariable are designed with an assumption that *user will be happy in updating Execution Profiles (= GlobalVariables) manually; they would not want to update Execution Profiles (= GlobalVariables) programmatically during the executions of Test Case scripts.*

A [post](https://forum.katalon.com/t/how-to-update-map-globalvariable-permanently/88025/5) in Katalon user forum discussed how to break this built-in design by overwriting XML files as the serialized format of Execution Profiles. I do not think their approach is very successful.

Here I proposed an alternative approach of configuring a Katalon Test Suite with a JSON text file. I think this approach is simpler; easier to understand; quickly customizable. Have a look and try.





