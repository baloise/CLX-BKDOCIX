package com.github.baloise.clx.bkdocix;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

public class IndexFileGeneratorCLI {

	public static void main(String[] args) throws IOException, ParseException {
		IndexFileGenerator generator = new IndexFileGenerator();
		switch (args.length) {
		case 1: 
			printSuccess(generator.csvToBankDocumentIndex(
					Paths.get(args[0]),
					Paths.get(".")
					));
			break;
		case 2: 
			printSuccess(generator.csvToBankDocumentIndex(
					Paths.get(args[0]), 
					Paths.get(args[1])
					));
			break;
		case 3: 
			printSuccess(generator.csvToBankDocumentIndex(
					Paths.get(args[0]), 
					Paths.get(args[1]), 
					args[2]
					));
			break;
		default: 
			printUsage();
			break;
		}
	}

	private static void printSuccess(Path bankDocumentIndex) {
		System.out.println("Index written to "+bankDocumentIndex);
		
	}

	private static void printUsage() {
		System.out.println(IndexFileGeneratorCLI.class.getSimpleName()+" <inputCSV> [<outputDirectory>] [<YYYYMMDDhhmmssmm>]");
	}

}
