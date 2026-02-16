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
