package com.example.dissertation814.models;

public class PdfPage {
    private String pageText;
    private String pageType;
    private int pageImage;
    private String imageType;

    public PdfPage() {
    }

    public PdfPage(String pageText, String pageType, int pageImage, String imageType) {
        this.pageText = pageText;
        this.pageType = pageType;
        this.pageImage = pageImage;
        this.imageType = imageType;
    }

    public String getPageText() {
        return pageText;
    }

    public void setPageText(String pageText) {
        this.pageText = pageText;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public int getPageImage() {
        return pageImage;
    }

    public void setPageImage(int pageImage) {
        this.pageImage = pageImage;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
