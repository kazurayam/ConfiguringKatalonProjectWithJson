import internal.GlobalVariable as GlobalVariable

/*
 * demonstrate that we can update the GlobalVariable in memory
 */
println "[TC2] GlobalVariable.config before update: " + GlobalVariable.config

GlobalVariable.config['home'] = 'mona lisa'

println "[TC2] GlobalVariable.confing after update: " + GlobalVariable.config