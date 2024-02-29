package com.greenjack;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
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
        Map<FileTypes, Integer> filesCounter = new EnumMap<>(FileTypes.class);
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
                        filesCounter.merge(type, 1, Integer::sum);
                        pagesCounter.merge(type, pageCount, Integer::sum);
                    } catch (IllegalArgumentException e) {
                        filesCounter.merge(FileTypes.OTHER, 1, Integer::sum);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int docs = filesCounter.entrySet().stream()
                .filter(e -> !e.getKey().equals(FileTypes.OTHER))
                .map(Map.Entry::getValue)
                .reduce(0, Integer::sum);
        System.out.printf("Overall documents: %d%n", docs);

        int pages = pagesCounter.values().stream().reduce(0, Integer::sum);
        System.out.printf("Overall pages: %d%n", pages);

        for (FileTypes type : parsedTypes) {
            int typeFilesCount = filesCounter.entrySet().stream()
                    .filter(e -> e.getKey().equals(type))
                    .map(Map.Entry::getValue)
                    .reduce(0, Integer::sum);

            int typePagesCount = pagesCounter.entrySet().stream()
                    .filter(e -> e.getKey().equals(type))
                    .map(Map.Entry::getValue)
                    .reduce(0, Integer::sum);

            System.out.printf("%s : %d files (%d pages)%n", type.getExtension(), typeFilesCount, typePagesCount);
        }

    }
}
