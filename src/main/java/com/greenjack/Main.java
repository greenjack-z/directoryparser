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


        options.addOption("h", "help", false, "show this message")
                .addOption(path);


        CommandLineParser commandLineParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
            System.out.println(new Parser(commandLine.getOptionValue(path)).parse());

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            helpFormatter.printHelp("directoryparser -p path", options);
        }
    }
}