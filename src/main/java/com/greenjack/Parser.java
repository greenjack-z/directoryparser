package com.greenjack;

import com.greenjack.exceptions.ParserIOException;
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
    private final String root;
    private final List<FileTypes> parsedTypes;

    public String parse() {
        System.out.println("Looking for files in " + root + " and subdirectories...");
        Path rootPath = Path.of(root);
        Map<FileTypes, Integer> filesCounter = new EnumMap<>(FileTypes.class);
        Map<FileTypes, Integer> pagesCounter = new EnumMap<>(FileTypes.class);

        try (Stream<Path> pathStream = Files.walk(rootPath)) {
            pathStream
                    .filter(path -> !path.toFile().isDirectory())
                    .forEach(path -> {
                        String extension = FilenameUtils.getExtension(path.getFileName().toString());
                        try {
                            FileTypes type = FileTypes.valueOf(extension.toUpperCase());
                            int pageCount = type.getPageCounter().count(path.toFile());
                            filesCounter.merge(type, 1, Integer::sum);
                            pagesCounter.merge(type, pageCount, Integer::sum);
                        } catch (IllegalArgumentException e) {
                            filesCounter.merge(FileTypes.OTHER, 1, Integer::sum);
                        }
                    });
        } catch (IOException e) {
            throw new ParserIOException(e);
        }
        StringBuilder result = new StringBuilder();
        int docs = filesCounter.entrySet().stream()
                .filter(e -> !e.getKey().equals(FileTypes.OTHER))
                .map(Map.Entry::getValue)
                .reduce(0, Integer::sum);
        result.append(String.format("Overall documents: %d%n", docs));

        int pages = pagesCounter.values().stream().reduce(0, Integer::sum);
        result.append(String.format("Overall pages: %d%n", pages));

        for (FileTypes type : parsedTypes) {
            int typeFilesCount = filesCounter.entrySet().stream()
                    .filter(e -> e.getKey().equals(type))
                    .map(Map.Entry::getValue)
                    .reduce(0, Integer::sum);

            int typePagesCount = pagesCounter.entrySet().stream()
                    .filter(e -> e.getKey().equals(type))
                    .map(Map.Entry::getValue)
                    .reduce(0, Integer::sum);

            result.append(String.format("%s : %d files (%d pages)%n", type.getExtension(), typeFilesCount, typePagesCount));
        }
        return result.toString();
    }
}
