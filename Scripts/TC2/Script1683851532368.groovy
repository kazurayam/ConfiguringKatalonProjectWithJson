import internal.GlobalVariable as GlobalVariable

/*
 * demonstrate that we can update the GlobalVariable in memory
 */
println "GlobalVariable.config before update: " + GlobalVariable.config

GlobalVariable.config['home'] = 'mona lisa'

println "GlobalVariable.confing after update: " + GlobalVariable.config