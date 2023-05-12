import groovy.json.JsonSlurper
import internal.GlobalVariable

/*
 * load the config file and put it into a GlobalVariable to share
 */
JsonSlurper slurper = new JsonSlurper()
GlobalVariable.config = slurper.parse(new File('./myconfig.json'))