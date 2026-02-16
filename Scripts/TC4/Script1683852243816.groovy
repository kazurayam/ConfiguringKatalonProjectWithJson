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