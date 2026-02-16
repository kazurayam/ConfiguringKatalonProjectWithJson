import com.kazurayam.ks.ConfigIO

import groovy.json.JsonOutput
import internal.GlobalVariable

/*
 * load the config file and put it into a GlobalVariable to share 
 * in the scope of a Test Suite
 */
GlobalVariable.config = ConfigIO.read(new File('./ksconfig.json'))

println "[TC1] " + JsonOutput.prettyPrint(JsonOutput.toJson(GlobalVariable.config))