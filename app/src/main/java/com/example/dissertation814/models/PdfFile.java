package com.example.dissertation814.models;

import android.net.Uri;
import java.util.Comparator;

public class PdfFile {
    private String pdfFileName;
    private Uri pdfFileUri;

    public PdfFile() {
    }

    public PdfFile(String fileName, Uri fileUri) {
        this.pdfFileName = fileName;
        this.pdfFileUri = fileUri;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public Uri getPdfFileUri() {
        return pdfFileUri;
    }

    public void setPdfFileUri(Uri pdfFileUri) {
        this.pdfFileUri = pdfFileUri;
    }

    //for sorting
    public static Comparator<PdfFile> myFile = new Comparator<PdfFile>() {
        @Override
        public int compare(PdfFile f1, PdfFile f2) {
            return f1.getPdfFileName().compareTo(f2.getPdfFileName());
        }
    };
}
