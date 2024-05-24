package org.example;

public class Categorie {
    String catId;
    String catName;
    String usage;
    String catPath;

    public Categorie(String catId, String catName, String usage, String catPath) {
        this.catId = catId;
        this.catName = catName;
        this.usage = usage;
        this.catPath = catPath;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }
}
