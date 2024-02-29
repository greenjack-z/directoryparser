package com.greenjack;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2 || args[0].equalsIgnoreCase("help")) {
            System.out.println("""
                    usage: parser root_path file_extension [file_extension...]
                        root_path           path from which parsing starts
                        file_extension      file extension to find and parse pages.
                        file_extension...   additional extensions to be parsed
                    """);
            return;
        }
        String root = args[0];
        List<FileTypes> types = new ArrayList<>();
        try {
            for (int i = 1; i < args.length; i++) {
                types.add(FileTypes.valueOf(args[i].toUpperCase().replaceFirst("\\.", "")));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not supported extension type:");
            StringBuilder typeList = new StringBuilder();
            for (FileTypes type : FileTypes.values()) {
                typeList.append(type.getExtension()).append(" ");
            }
            System.out.println("supported extensions: " + typeList.toString());
            return;
        }

        new Parser(root, types).parse();
    }
}