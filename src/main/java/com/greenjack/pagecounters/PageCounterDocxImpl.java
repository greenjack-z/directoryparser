package com.greenjack.pagecounters;

import com.greenjack.exceptions.DocxCounterIOException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PageCounterDocxImpl implements PageCounter {
    @Override
    public int count(File file) {
        try (InputStream is = new FileInputStream(file)) {
            org.apache.poi.xwpf.usermodel.XWPFDocument doc = new XWPFDocument(is);
            return doc.getProperties().getExtendedProperties().getPages();
        } catch (IOException e) {
            throw new DocxCounterIOException(e);
        }
    }
}
