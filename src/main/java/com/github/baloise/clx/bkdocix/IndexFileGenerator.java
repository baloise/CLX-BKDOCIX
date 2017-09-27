package com.github.baloise.clx.bkdocix;

import static java.lang.String.format;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.write;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class IndexFileGenerator {

	String template = "BKDOCIX|0000008334|2 |%1$-25s|%2$2s|0|%3$-400s|%4$-4000s|%5$-256s|                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |                                                                                                                                                                                                                                                                |%6$8s|%7$8s|%8$-256s|        |              | | |M| |02|%1$-25s|          |                                                                                                                        |                         |                                                  |                                |                                                                                                    |50640898                 |                         |                         ";  	
	
	public String csvToBankDocumentIndex(String csv) {
		return csvLineToBankDocumentIndex(asList(csv.split("\\R")));
	}

	public String csvLineToBankDocumentIndex(Collection<String> csvLines) {
		int lineNr = 1;
		List<String> ret = new ArrayList<>(csvLines.size());
		for (String csvLine : csvLines) {
			String[] vector = csvLine.split(";");
			LocalDate validFrom  = LocalDate.now();
			LocalDate validUntil = validFrom.plus(1, ChronoUnit.MONTHS);
			ret.add(format(template,			//
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
		return ret.stream().collect(joining("\n"));
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

	public String csvToBankDocumentIndex(Path csv) throws IOException {
		return csvLineToBankDocumentIndex(readAllLines(csv));
	}
	
	public Path csvToBankDocumentIndex(Path csv, Path outputDirectory) throws IOException {
		return csvToBankDocumentIndex(csv, outputDirectory, new Date());
	}
	
	public Path csvToBankDocumentIndex(Path csv, Path outputDirectory, Date exportStart) throws IOException {
		Path outputFile = outputDirectory.resolve(
				format("0000000009.host.meldungen.%s.0100.inkr.dat", 
				new SimpleDateFormat("yyyyMMddHHmmssSS").format(exportStart)
				));
		write(outputFile, csvToBankDocumentIndex(csv).getBytes());
		return outputFile;
	}

}
