package com.greenjack;

import com.greenjack.pagecounters.PageCounterDocxImpl;
import com.greenjack.pagecounters.PageCounter;
import com.greenjack.pagecounters.PageCounterPdfImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileTypes {

    DOCX(".docx", new PageCounterDocxImpl()),
    PDF(".pdf", new PageCounterPdfImpl()),
    OTHER("other", null);

    private final String extension;
    private final PageCounter pageCounter;
}
