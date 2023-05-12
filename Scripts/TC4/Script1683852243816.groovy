import groovy.json.JsonOutput
import internal.GlobalVariable as GlobalVariable

/*
 * demonstrate that we can save the updated config into an external JSON file
 */
def myconfig = GlobalVariable.config
myconfig['money'] = 12345

File f = new File('./myconfig2.json')   
// you can specify the original file name 'myconfig.json' to overwrite it if you want

f.text = JsonOutput.prettyPrint(JsonOutput.toJson(myconfig)) 
println f.text