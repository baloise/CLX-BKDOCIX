package com.github.baloise.clx.bkdocix;

import static java.lang.System.getProperty;
import static java.nio.file.Paths.get;
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
	public void test() throws Exception {
		Path bankDocumentIndex = generator.csvToBankDocumentIndex(csv, get(getProperty("java.io.tmpdir")));
		String expected = load("/expected.dat").replaceAll("\\R", "\n");
		String actual = load(bankDocumentIndex).replaceAll("\\R", "\n");
		System.out.println(actual);
		bankDocumentIndex.toFile().delete();
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
