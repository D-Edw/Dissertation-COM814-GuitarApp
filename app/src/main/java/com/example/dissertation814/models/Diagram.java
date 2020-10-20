package com.example.dissertation814.models;

public class Diagram {
    private String diagramName;
    private String diagramType;
    private int diagramRes;

    public Diagram() {
    }

    public Diagram(String diagramName, String diagramType, int diagramRes) {
        this.diagramName = diagramName;
        this.diagramType = diagramType;
        this.diagramRes = diagramRes;
    }

    public String getDiagramName() {
        return diagramName;
    }

    public void setDiagramName(String diagramName) {
        this.diagramName = diagramName;
    }

    public String getDiagramType() {
        return diagramType;
    }

    public void setDiagramType(String diagramType) {
        this.diagramType = diagramType;
    }

    public int getDiagramRes() {
        return diagramRes;
    }

    public void setDiagramRes(int diagramRes) {
        this.diagramRes = diagramRes;
    }
}
