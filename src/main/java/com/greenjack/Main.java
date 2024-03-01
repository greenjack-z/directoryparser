package com.greenjack;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();

        Option path = Option.builder("p")
                .longOpt("path")
                .desc("root parsing directory tree")
                .hasArg()
                .argName("path")
                .required()
                .build();

        Option extensions = Option.builder("e")
                .longOpt("extensions")
                .desc("extensions to parse in directory tree (comma separated, without preceding dot)")
                .hasArgs()
                .argName("extension")
                .valueSeparator(',')
                .required()
                .build();

        options.addOption("h", "help", false,  "show this message")
                .addOption(path)
                .addOption(extensions);

        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
            List<FileTypes> parsedFiles = new ArrayList<>();
            for (String extension : commandLine.getOptionValues(extensions)) {
                parsedFiles.add(FileTypes.valueOf(extension.toUpperCase()));
            }
            System.out.println(new Parser(commandLine.getOptionValue(path), parsedFiles).parse());

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            helpFormatter.printHelp("derectoryparser -p path -e extension [extension...]", options);
        } catch (IllegalArgumentException e) {
            System.out.println("Not supported extension type:");
            StringBuilder typeList = new StringBuilder();
            for (FileTypes type : FileTypes.values()) {
                typeList.append(type.getExtension()).append(" ");
            }
            System.out.println("supported extensions: " + typeList);
        }
    }
}