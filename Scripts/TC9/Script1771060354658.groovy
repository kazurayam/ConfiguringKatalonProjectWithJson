import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import internal.GlobalVariable as GlobalVariable

/*
 * demonstrate that we can retrieve the updated config file
 */

File f = new File('./ksconfig2.json')
println "[TC9] " + f.text

JsonSlurper slurper = new JsonSlurper()
def config = slurper.parse(f)

println "[TC9] bill: " + config["bill"]
println "[TC9] home: " + config["home"]
println "[TC9] money: " + config["money"]
