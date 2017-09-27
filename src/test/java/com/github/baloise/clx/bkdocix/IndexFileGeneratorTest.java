package com.github.baloise.clx.bkdocix;

import static java.lang.System.getProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IndexFileGeneratorTest {

	private static final String REPORT_CSV = "/csv_report.csv";
	IndexFileGenerator generator = new IndexFileGenerator();
	private Path csv;
	
	@BeforeEach
	public void setUp() throws URISyntaxException {
		csv = Paths.get(getClass().getResource(REPORT_CSV).toURI());
	}
	
	@Test
	public void fromStringEqualsFromPath() throws Exception {
		String expected = generator.csvToBankDocumentIndex(csv);
		String actual = generator.csvToBankDocumentIndex(load(REPORT_CSV));
		assertEquals(expected, actual);
	}
	
	@Test
	public void toStringEqualstoFile() throws Exception {
		String expected = generator.csvToBankDocumentIndex(csv);
		Path index = generator.csvToBankDocumentIndex(csv, Paths.get(getProperty("java.io.tmpdir")));
		assertEquals(expected, load(index));
		index.toFile().deleteOnExit();
	}
	
	@Test
	public void test() throws Exception {
		String expected = load("/expected.dat").replaceAll("\\R", "\n");
		String actual = generator.csvToBankDocumentIndex(csv);
		assertEquals(actual, actual);
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	

	private String load(Path path) throws IOException {
		return load(new FileInputStream(path.toFile()));
	}
	private String load(String path) {
		return load(getClass().getResourceAsStream(path));
	}

	private String load(final InputStream inputStream) {
		try (Scanner sc = new Scanner(inputStream, "UTF-8")){
			return sc.useDelimiter("\\Z").next();
		}
	}

}
