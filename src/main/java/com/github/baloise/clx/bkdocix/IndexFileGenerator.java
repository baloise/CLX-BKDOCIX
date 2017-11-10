package com.github.baloise.clx.bkdocix;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class IndexFileGenerator {

	private static final String ENCODING = "UTF-8";
	String template = "BKDOCIX|0000008334|2 |%1$-25s|%2$2s|0|%3$-400s|%4$-4000s|%5$-256s|                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |%6$8s|%7$8s|%8$-256s|        |              | | |M| |02|%1$-25s|          |                                                                                                                        |                         |                                                  |                                |                                                                                                    |50640898                 |                         |                         ";
	private LocalDate validUntil;
	private LocalDate validFrom;  	

	public LocalDate getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(LocalDate validUntil) {
		this.validUntil = validUntil;
	}

	public LocalDate getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public IndexFileGenerator() {
		validFrom  = LocalDate.now();
		validUntil = validFrom.plus(1, ChronoUnit.MONTHS);
	}
	
	public void csvToBankDocumentIndex(InputStream in, OutputStream out) throws IOException{
		int lineNr = 1;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(in, ENCODING))){
			try(PrintStream writer = new PrintStream(out, false, ENCODING)){
				String csvLine;
				while ((csvLine = br.readLine()) != null) {
					String[] vector = csvLine.split(";");
					writer.println(format(template,			//
							vector[0],         		// 3 DESTINATION_NO        
							lang(vector[1]),   		// 4 LANG_CD               
							vector[2],         		// 6 MSG_SUBJECT           
							vector[3],         		// 7 MSG_BODY              
							attachment(vector[1]),  // 8 ATTACHMENT_1          
							date8(validFrom),       // 14 VALID_FROM           
							date8(validUntil),      // 15 VALID_UNTIL          
							lineNr++                // 16 SOURCE_REFERENCE     
							)
							);
				}
			}
		}
	}

	private String date8(Object date) {
		return format("%1$tY%1$tm%1$td", date);
	}

	private String attachment(String lang) {
		return format("file:./%s.pdf", lang(lang));
	}

	private String lang(String lang) {
		if(lang.toUpperCase().contains("I")) return "IT";
		if(lang.toUpperCase().contains("F")) return "FR";
		return "DE";
	}

	public Path csvToBankDocumentIndex(Path csv, Path outputDirectory) throws IOException {
		return csvToBankDocumentIndex(csv, outputDirectory, now());
	}
	
	public Path csvToBankDocumentIndex(Path csv, Path outputDirectory, String exportStart) throws IOException {
		assertStringLength(exportStart,16);
		Path outputFile = outputDirectory.resolve(
				format("0000000009.host.meldungen.%s.0100.inkr.dat", 
				exportStart
				));
		csvToBankDocumentIndex(new FileInputStream(csv.toFile()), new FileOutputStream(outputFile.toFile()));
		return outputFile;
	}

	private void assertStringLength(String string, int length) {
		if(string.length() != length) throw new IllegalArgumentException(format("expected '%s' to be of length %s but got length %s", string,length, string.length()));
	}

	public String now() {
		Date date = new Date();
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date) +new SimpleDateFormat("SSS").format(date).substring(0,2);
	}
	
}
