# Configuring a Katalon Studio project with JSON files

This is a [Katalon Studio](https://katalon.com/katalon-studio) project for demonstration purpose.
You can download the zip of this project from the [Release](https://github.com/kazurayam/ConfiguringKatalonProjectWithJson/releases/) page,
unzip it, open it with your local Katalon Studio.

This project was developed with Katalon Studio v8.3, but is
version-independent. It should work with any version.

I developed this project in the hope to propose a alternative
to the discussion in the Katalon User forum:

- https://forum.katalon.com/t/how-to-update-map-globalvariable-permanently/88025/

## Problem to solve

Katalon Studio provides [Execution Profile and Global Variables](https://docs.katalon.com/docs/create-tests/data-driven-testing/global-variables-and-execution-profile).

>Execution Profile helps cover multiple and different environments to execute your automation test scripts with ease. You can configure the testing environment in terms of data and behaviors through Global variables.

You can update the value of GlobalVariable in memory during test runs but you can not persist the updates into Execution Profiles. If you want to alter the values of Execution Profiles, editing the values in Katalon Studio's GUI is the only way.

I am not satisfied with this constraint. I want to find a way to update the config information programmatically and persist it into disk during a Test Suite run; then later I want to reuse the updated config information in another Test Suite run. How can achieve it?

## Solution

- In the project folder, I will add JSON text files as a container of config information. Thanks to the expressiveness of JSON, I can express anything.

- I will create a GlobalVariable of type `Null`.

- My test script will fully utilize the Groovy language's [features of processing JSON](https://www.baeldung.com/groovy-json).

- My test script will explicitly load a JSON configuration file, turn it into an Object, substitute it into a GlobalVariable. The GlobalVariable will be shared by all Test Cases in a Test Suite. My test script will update the Object during test run, and will explicitly save it into a JSON file.

- Thanks to the Groovy's features of processing JSON, it is quite easy and straight-forward to implement this processing. I will show you a full set of sample code.

- This approach is intended for seasoned programmers. I'm afraid, non-programmers can not enjoy the power of my approach.

## Description of the demo implementation

### JSON configuration file

I created a text file in JSON format: [`<projectDir>/ksconfig.json`](https://github.com/kazurayam/ConfiguringKatalonProjectWithJson/blob/master/ksconfig.json)

```
{
	"bill": "debit",
	"home": "debit",
	"money": "credit"
}
```

### GlobalVariable.config

I created a GlobalVariable named `config` in the `default` Execution Profile. I assigned the type `Null` to it.

![GlobalVariable.config](https://kazurayam.github.io/ConfiguringKatalonProjectWithJson/images/01_GlobalVariable.config.png)

This GlobalVariable is used as a placeholder for an Object deserialized from JSON file. The GlobalVariable will be visible for all Test Cases in a Test Suite.

### Groovy class that wraps reading/writing JSON

[Keywords/com/kazurayam/ks/ConfigIO.groovy](https://github.com/kazurayam/ConfiguringKatalonProjectWithJson/blob/develop/Keywords/com/kazurayam/ks/ConfigIO.groovy) implements the `read` method and the `wrtie` method for JSON file. The TC1 and TC4 use this Groovy class to read / write JSON files.

```
package com.kazurayam.ks

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

public class ConfigIO {

	public static Object read(File jsonFile) {
		assert jsonFile.exists()
		assert jsonFile.isFile()
		JsonSlurper slurper = new JsonSlurper()
		def config = slurper.parse(jsonFile)
		return config
	}

	public static void write(Object config, File jsonFile) {
		assert config != null
		ensureParentDirectory(jsonFile)
		jsonFile.text = JsonOutput.prettyPrint(JsonOutput.toJson(config))
	}

	private static void ensureParentDirectory(File file) {
		File parent = file.getParentFile()
		if (!parent.exists()) {
			boolean b = parent.mkdirs()
			if (!b) throw new IOException("failed to create " + parent.toString())
		}
	}
}
```

### Test Case TC1 --- load the JSON to GlobalVariable

[Test Cases/TC1](https://github.com/kazurayam/ConfiguringKatalonProjectWithJson/blob/develop/Scripts/TC1/Script1683851520241.groovy) loads a JSON file, deserialize it into an Object, put the Object into the placeholder GlobalVariable.

```
import com.kazurayam.ks.ConfigIO

import groovy.json.JsonOutput
import internal.GlobalVariable

/*
 * load the config file and put it into a GlobalVariable to share
 * in the scope of a Test Suite
 */
GlobalVariable.config = ConfigIO.read(new File('./ksconfig.json'))

println "[TC1] " + JsonOutput.prettyPrint(JsonOutput.toJson(GlobalVariable.config))
```

### Test Case TC2 --- update information programmatically

[Test Cases/TC2](https://github.com/kazurayam/ConfiguringKatalonProjectWithJson/blob/develop/Scripts/TC2/Script1683851532368.groovy) updates the Object programmatically.

```
import internal.GlobalVariable as GlobalVariable

/*
 * demonstrate that we can update the GlobalVariable in memory
 */
println "GlobalVariable.config before update: " + GlobalVariable.config

GlobalVariable.config['home'] = 'mona lisa'

println "GlobalVariable.confing after update: " + GlobalVariable.config
```

### Test Case TC3 --- demonstrate that the updated information is carried over

[Test Cases/TC3](https://github.com/kazurayam/ConfiguringKatalonProjectWithJson/blob/develop/Scripts/TC3/Script1683851540202.groovy) demostrates that the Object updated by the preceding `TC2` is carried over and visible for `TC3`.

```
import internal.GlobalVariable as GlobalVariable

/*
 * demostrate that the GlobalVariable which was updated by the preceding Test Case TC2
 * is globaly shared in the Test Suite scope
 */
println "GlobalVariable.confing carried over: " + GlobalVariable.config
```

### Test Case TC4 --- persist the information into an external file

[Test Cases/TC4](https://github.com/kazurayam/ConfiguringKatalonProjectWithJson/blob/develop/Scripts/TC4/Script1683852243816.groovy) serializes the updated Object into JSON and save it into a text file.

```
import com.kazurayam.ks.ConfigIO

import groovy.json.JsonOutput
import internal.GlobalVariable as GlobalVariable

/*
 * demonstrate that we can save the updated config into an external JSON file
 */
GlobalVariable.config['money'] = 12345

// save the config into a file
// you can specify the original file name 'ksconfig.json' to overwrite it if you want to
File f2 = new File('./ksconfig2.json')
ConfigIO.write(GlobalVariable.config, f2)

// look into the saved text
println "[TC4] " + f2.text
```

Please pay attention to the output file name.
If you specify a new name, then a new file will be created.
If you specify the name of source JSON file, then the file will be overwritten.
Which way to take?
It is up to you.


### Test Suite TS1 --- run TC1 + TC2 + TC3 + TC4

I made a Test Suite `TS1`, which just runs TC1, TC2, TC3 and TC4 in this sequence.
When I ran it, I got the following output in the console:

```
...
GlobalVariable.config before update: [bill:debit, home:debit, money:credit]
...
GlobalVariable.confing after update: [bill:debit, home:mona lisa, money:credit]
...
GlobalVariable.confing carried over: [bill:debit, home:mona lisa, money:credit]
...
{
    "bill": "debit",
    "home": "mona lisa",
    "money": 12345
}
...
```

### Test Suite Collection --- run TS1 and TS2 where the ksconfig2.json file is passed

I added a Test Suite (TS2) and a Test Suite Collection (TSC0). The TSC0 binds the TS1 and the TS2 sequentially.

When I run the TSC0,
The TS1 printed this:

```
[TC1] {
    "bill": "debit",
    "home": "debit",
    "money": "credit"
}
[TC2] GlobalVariable.config before update: [bill:debit, home:debit, money:credit]
[TC2] GlobalVariable.confing after update: [bill:debit, home:mona lisa, money:credit]
[TC3] GlobalVariable.confing carried over: [bill:debit, home:mona lisa, money:credit]
[TC4] {
    "bill": "debit",
    "home": "mona lisa",
    "money": 12345
}
```

And the TS2 printed this:

```
[TC9] {
    "bill": "debit",
    "home": "mona lisa",
    "money": 12345
}
[TC9] bill: debit
[TC9] home: mona lisa
[TC9] money: 12345
```

The output clearly shows that the TS2 (precisely TC9) could read the `ksconfig2.json` file which was updated by the preceding TS1.

## Conclusion

I think that the built-in features in Katalon Studio GUI around GlobalVariable are designed with an assumption that *users will be happy and satisfied with manually edited Execution Profiles (= GlobalVariables); they would never want to update Execution Profiles on disk programmatically during the executions of Test Case scripts. I think that this assumption is appropriate as long as Katalon Studio is designed for non-programmers.

On the other hand, A [post](https://forum.katalon.com/t/how-to-update-map-globalvariable-permanently/88025/5) in Katalon user forum discussed how to break this design by overwriting XML files as the serialized format of Execution Profiles. I do not think their approach would be successful. I think we shouldn't try to modify the behavior of Katalon Studio. Just leave it as is. I think, we had better invent an alternative way of configuring a Test Suite by custom Groovy scripting.

A shortage of my approach is that it does not provide any GUI component to edit/view the JSON configuration as a part of Katalon Studio GUI. But I don't mind it. I don't need any GUI support. A text editor is saficient for me to handle JSON files.
