package com.greenjack.pagecounters;

import com.greenjack.exceptions.PdfCounterIOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PageCounterPdfImpl implements PageCounter {
    @Override
    public int count(File file) {
        try (PDDocument doc = Loader.loadPDF(file)) {
            return doc.getNumberOfPages();
        } catch (IOException e) {
            throw new PdfCounterIOException(e);
        }
    }
}
