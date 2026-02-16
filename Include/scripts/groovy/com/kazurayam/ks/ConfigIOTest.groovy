package com.kazurayam.ks

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import com.kms.katalon.core.configuration.RunConfiguration

import groovy.json.JsonSlurper

@RunWith(JUnit4.class)
class ConfigIOTest {

	static Path outDir
	static Path inputFile
	static Path outputFile

	@BeforeClass
	static void beforeClass() {
		Path projectDir = Paths.get(RunConfiguration.getProjectDir())
		Path buildDir = projectDir.resolve("build")
		outDir = buildDir.resolve("tmp")
		Files.createDirectories(outDir)
	}

	@Before
	void setup() {
		inputFile = outDir.resolve("config.json")
		inputFile.text = """{
    "bill": "debit",
    "home": "debit",
    "money": "credit"
}"""
		outputFile = outDir.resolve("config2.json")
		if (Files.exists(outputFile)) {
			Files.delete(outputFile)
		}
	}

	@Test
	void testRead() {
		def config = ConfigIO.read(inputFile.toFile())
		assertNotNull(config)
		assertEquals(config["bill"], "debit")
		assertEquals(config["home"], "debit")
		assertEquals(config["money"], "credit")
	}

	@Test
	void testWrite() {
		String content = """{
    "bill": "debit",
    "home": "mona lisa",
    "money": 12345
}
"""
		JsonSlurper slurper = new JsonSlurper()
		def config = slurper.parse(new StringReader(content))
		ConfigIO.write(config, outputFile.toFile())
		assertTrue(Files.exists(outputFile))
		println outputFile.toFile().text
	}
}
