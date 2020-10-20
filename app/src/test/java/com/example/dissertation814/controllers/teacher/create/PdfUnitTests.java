package com.example.dissertation814.controllers.teacher.create;

import com.example.dissertation814.R;
import com.example.dissertation814.models.PdfPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PdfUnitTests {
    private List<PdfPage> testList;

    @Before
    public void setup(){
        testList = new ArrayList<>();
    }

    @After
    public void arrayClear(){
        testList.clear();
    }

    @Test
    public void createPdfPageOfTypeText_WhenNone_ShouldReturnNotNullAndSetAllProperties() {
        //Arrange
        PdfPage pdfPage = new PdfPage();

        //Act
        pdfPage.setPageText("Test Text");
        pdfPage.setPageType("Text");

        //Assert object is not null
        assertNotNull(pdfPage);

        //Now assert that the page properties were set correctly
        assertEquals("Test Text", pdfPage.getPageText());
        assertEquals("Text", pdfPage.getPageType());
    }

    @Test
    public void createPdfPageOfTypeImage_WhenNone_ShouldReturnNotNullAndSetAllProperties() {
        //Arrange
        PdfPage pdfPage = new PdfPage();

        //Act
        pdfPage.setPageType("Image");
        pdfPage.setPageImage(R.drawable.a_blues_scale_android);
        pdfPage.setImageType("Scale");

        //Assert object is not null
        assertNotNull(pdfPage);

        //Now assert that the page properties were set correctly
        assertEquals("Image", pdfPage.getPageType());
        assertEquals(R.drawable.a_blues_scale_android, pdfPage.getPageImage());
        assertEquals("Scale", pdfPage.getImageType());
    }

    @Test
    public void addTextPage_WhenNone_ShouldReturn1() {
        //Arrange
        PdfPage pdfPage = new PdfPage();
        pdfPage.setPageType("Text");
        //Act
        testList.add(pdfPage);
        int count = testList.size();
        //Assert
        assertEquals(1, count);
    }

    @Test
    public void addImagePage_WhenNone_ShouldReturn1() {
        //Arrange
        PdfPage pdfPage = new PdfPage();
        pdfPage.setPageType("Image");
        //Act
        testList.add(pdfPage);
        int count = testList.size();
        //Assert
        assertEquals(1, count);
    }

    @Test
    public void DeleteTextPage_ThatExists_ShouldReturn0(){
        //Arrange
        PdfPage pdfPage = new PdfPage();
        pdfPage.setPageText("Test Text");
        pdfPage.setPageType("Text");
        //Act
        testList.add(pdfPage);
        testList.remove(0);
        int count = testList.size();
        //Assert
        assertEquals(0, count);
    }

    @Test
    public void DeleteImagePage_ThatExists_ShouldReturn0(){
        //Arrange
        PdfPage pdfPage = new PdfPage();
        pdfPage.setPageType("Image");
        pdfPage.setPageImage(R.drawable.a_blues_scale_android);
        pdfPage.setImageType("Scale");
        //Act
        testList.add(pdfPage);
        testList.remove(0);
        int count = testList.size();
        //Assert
        assertEquals(0, count);
    }

    @Test
    public void EditTextPage_WhenAdded_ShouldReturnEditedPage(){
        //Arrange
        PdfPage pdfPage = new PdfPage();
        pdfPage.setPageType("Text");
        pdfPage.setPageText("Test Text 1");
        testList.add(pdfPage);

        //Act
        pdfPage.setPageText("Edited Text");

        //Assert
        assertEquals("Edited Text", pdfPage.getPageText());
    }

    @Test
    public void ClearPageArray_When2Added_ShouldReturn0(){
        //Arrange
        PdfPage pdfPage = new PdfPage();
        PdfPage pdfPage1 = new PdfPage();

        pdfPage.setPageType("Text");
        pdfPage.setPageText("Test Text 1");

        pdfPage1.setPageType("Image");
        pdfPage1.setPageImage(R.drawable.a_blues_scale_android);
        pdfPage1.setImageType("Scale");

        testList.add(pdfPage);
        testList.add(pdfPage1);

        //Act
        testList.clear();
        int count = testList.size();

        //Assert
        assertEquals(0, count);

    }


}