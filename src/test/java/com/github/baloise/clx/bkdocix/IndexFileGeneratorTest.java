package com.github.baloise.clx.bkdocix;

import static java.lang.System.getProperty;
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;


public class IndexFileGeneratorTest {

	private static final String REPORT_CSV = "/csv_report.csv";
	IndexFileGenerator generator;
	private Path csv;
	
	@Before
	public void setUp() throws URISyntaxException {
		generator = new IndexFileGenerator();
		generator.setValidFrom(LocalDate.of(2017, 9, 27));
		generator.setValidUntil(LocalDate.of(2017, 10, 27));
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
