package com.greenjack;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Parser {
    private final String  root;
    private final List<FileTypes> parsedTypes;

    public void parse() {
        System.out.println("Looking for files in " + root + " and subdirectories...");
        Path rootPath = Path.of(root);
        Map<Path, FileTypes> filesMap = new HashMap<>();
        Map<FileTypes, Integer> pagesCounter = new EnumMap<>(FileTypes.class);

        try (Stream<Path> pathStream = Files.walk(rootPath)) {
            pathStream
                .forEach(path -> {
                    if (path.toFile().isDirectory()) {
                        return;
                    }
                    String extension = FilenameUtils.getExtension(path.getFileName().toString());
                    try {
                        FileTypes type = FileTypes.valueOf(extension.toUpperCase());
                        if (!parsedTypes.contains(type)) {
                            return;
                        }
                        int pageCount = type.getPageCounter().count(path.toFile());
                        filesMap.put(path, type);
                        pagesCounter.merge(type, pageCount, Integer::sum);
                    } catch (IllegalArgumentException e) {
                        filesMap.put(path, FileTypes.OTHER);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Overall documents: " + filesMap.values().stream().filter(v -> !v.equals(FileTypes.OTHER)).count());
        System.out.println("Overall pages: " + pagesCounter.values().stream().reduce(0, Integer::sum));
        for (FileTypes type : parsedTypes) {
            System.out.println(type.getExtension() +
                    " files : " +
                    filesMap.values().stream().filter(v -> v.equals(type)).count() +
                    " pages: " +
                    pagesCounter.entrySet().stream().filter(e -> e.getKey().equals(type)).map(Map.Entry::getValue).reduce(0, Integer::sum));
        }

    }
}
