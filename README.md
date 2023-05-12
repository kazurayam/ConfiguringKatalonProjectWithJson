# Configuring a Katalon Studio project with a JSON file

This is a [Katalon Studio](https://katalon.com/katalon-studio) project for demonstration purpose.
You can download the zip of this project from the [Release] page,
unzip it, open it with your local Katalon Studio.

This project was developed with Katalon Studio v8.3, but is
version-independent. It should work with any version.

I developed this project in the hope to propose a alternative
to the discussion in the Katalon User forum:

- https://forum.katalon.com/t/how-to-update-map-globalvariable-permanently/88025/5

## Problem to solve

Katalon Studio provides [Execution Profile and Global Variables](https://docs.katalon.com/docs/create-tests/data-driven-testing/global-variables-and-execution-profile).

>Execution Profile helps cover multiple and different environments to execute your automation test scripts with ease. You can configure the testing environment in terms of data and behaviors through Global variables.

However you can not persist the values of Global Variables into Execution Profiles during test runs. You can only edit the values manually using Katalon Studio's GUI.

I want to find a way to generate configuration programmatically and persist it into disk in a Test Suite run, then later reuse the updated configuration in another session of Test Suite. How can achieve do it?

## Solution

- I will add text files as a mean of configuration. In there I can express any information in JSON syntax. Thanks to the expressiveness of JSON, I can write anything in JSON.

- I will create a GlobalVariable of type `Null`.

- My test script will fully utilize the Groovy language [features of processing JSON](https://www.baeldung.com/groovy-json).

- My test script will explicitly load a JSON configuration file, turn it into an Object, substitute it into a GlobalVariable. The GlobalVariable will be shared by all Test Cases in a Test Suite. My test script will update the Object during test run, and will explicitly save it into a JSON file. Thanks to the Groovy's features of processing JSON, it is quite straight-forward to implement this processing. I will show you a full set of sample code.

- This approach is intended for seasoned programmers. I'm afraid, non-programmers can not enjoy this freedom.

## Description of the demo implementation


## Conclusion

I think that the built-in features in Katalon Studio GUI around GlobalVariable are designed with an assumption that *user will be happy and satisfied with updating Execution Profiles (= GlobalVariables) manually; they would never want to update Execution Profiles on disk programmatically during the executions of Test Case scripts.*

A [post](https://forum.katalon.com/t/how-to-update-map-globalvariable-permanently/88025/5) in Katalon user forum discussed how to break this built-in design by overwriting XML files as the serialized format of Execution Profiles. I do not think their approach is very successful. I think we had better not try to modify the behavior of Katalon Studio; we should leave it as is; we should add another way of configuring a Test Suite using Groovy scripting.

Here I proposed an alternative approach of configuring a Katalon Test Suite with a JSON text file. I think this approach is simpler; easier to understand; quickly customizable. Have a look and try.
