package org.example;

import org.w3c.dom.*;

public class SimpleXsltFormatter implements XsltFormatter{
    private String decodedContent;
    private String catId;
    private String catPathName;

    public SimpleXsltFormatter(String decodedContent, String catId, String catPathName) {
        this.decodedContent = decodedContent;
        this.catId = catId;
        this.catPathName = catPathName;
    }

    public Document formatXslt(Document destDoc){
        Element xslNode = destDoc.createElement("xsl");
        Node contentNode = destDoc.createElement("content");
        NamedNodeMap contentNodeAttrs = contentNode.getAttributes();
        Attr id = destDoc.createAttribute("id");
        id.setValue(catId);
        contentNodeAttrs.setNamedItem(id);
        contentNode.setTextContent(decodedContent.replace("DisplayName", "\n 'DisplayName'"));
        xslNode.appendChild(contentNode);
        destDoc.appendChild(xslNode);

        return destDoc;
    }

    public String getDecodedContent() {
        return decodedContent;
    }

    public void setDecodedContent(String decodedContent) {
        this.decodedContent = decodedContent;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatPathName() {
        return catPathName;
    }

    public void setCatPathName(String catPathName) {
        this.catPathName = catPathName;
    }
}
