package com.example.fernando.controlekm;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by Flavia on 30/12/2017.
 */

public class PageOrientation extends PdfPageEventHelper {
    protected PdfNumber orientation = PdfPage.LANDSCAPE;

    public void setOrientation(PdfNumber orientation) {
        this.orientation = orientation;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        writer.addPageDictEntry(PdfName.ROTATE, orientation);

    }


}
