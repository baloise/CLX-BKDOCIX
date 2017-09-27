package com.github.baloise.clx.bkdocix;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class IndexFileGeneratorTest {

	@Test
	public void test() {
		IndexFileGenerator generator = new IndexFileGenerator();
		String expected = load("/0000000009.host.meldungen.2017071315020504.0100.inkr.dat");
		String csv = load("/csv_report.csv");
		String actual = generator.csvToBankDocumentIndex(csv);
		assertEquals(expected, actual);
	}

	private String load(String path) {
		try (Scanner sc = new Scanner(getClass().getResourceAsStream(path), "UTF-8")){
			return sc.useDelimiter("\\Z").next();
		}
	}

}
