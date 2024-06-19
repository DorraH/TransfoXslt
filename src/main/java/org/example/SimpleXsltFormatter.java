package org.example;

import fr.aerow.opentext.oscriptdata.*;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        Element xslStylesheet = destDoc.createElement("xsl:stylesheet");
        xslStylesheet.setAttribute("version", "1.0");
        xslStylesheet.setAttribute("xmlns:xsl", "http://www.w3.org/1999/XSL/Transform");

        Element xslOutput = destDoc.createElement("xsl:output");
        xslOutput.setAttribute("method", "xml");
        xslOutput.setAttribute("encoding", "UTF-8");
        xslOutput.setAttribute("indent", "yes");
        xslStylesheet.appendChild(xslOutput);

        Element xslTemplateNode = destDoc.createElement("xsl:template");
        xslTemplateNode.setAttribute("match", "@*|node()");
        Element xslCopy = destDoc.createElement("xsl:copy");
        Element xslApply = destDoc.createElement("xsl:apply-templates");
        xslApply.setAttribute("select", "@*|node()");
        xslCopy.appendChild(xslApply);
        xslTemplateNode.appendChild(xslCopy);
        xslStylesheet.appendChild(xslTemplateNode);

        Element xslTemplateCategory = destDoc.createElement("xsl:template");
        xslTemplateCategory.setAttribute("match", "category[contains(@name, '".concat(catPathName).concat("')]"));

        Element contentNode = destDoc.createElement("category");
        contentNode.setAttribute("name", catPathName.replace("Livelink Categories", "Sphere Categories"));


        // Initiate OScriptParser
        OScriptParser parser = new OScriptParser(decodedContent);
        // Parse string and get result
        OScriptAssoc data = null;
        try {
            data = (OScriptAssoc) parser.parse();
        } catch (OScriptDataEmptyStringException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (data != null) {
            System.out.println("data: "+data.get("Children").getType().toString());
        }
        OScriptList childList = (OScriptList) data.get("Children");
        String children = data.get("Children").toOScript();

//        List<String> categoryContent = Arrays.stream(decodedContent.split("(?='DisplayName')(.*?)")).toList();
        List<String> categoryContent = Arrays.stream(decodedContent.split("(?='DisplayName')")).toList();
        childList.list.forEach(child -> {
            OScriptAssoc childObj = (OScriptAssoc) child;
            System.out.println("ID: "+childObj.get("ID"));
            if(! childObj.get("ID").equals("1")){
                String displayName = childObj.get("DisplayName").toString();

                Element attribute = destDoc.createElement("attribute");
                attribute.setAttribute("name", displayName);
                Element xslValueOf = destDoc.createElement("xsl:value-of");
                xslValueOf.setAttribute("select", "../category[@name='".concat(catPathName)
                        .concat("']/attribute[@name='").concat(displayName).concat("']"));
                attribute.appendChild(xslValueOf);
                contentNode.appendChild(attribute);

                System.out.println("Child Attr: "+ childObj.get("DisplayName"));
            }
        });
        /*categoryContent.forEach(content -> {
            if(content.contains("'DisplayName'") && !content.contains("'ID'=1,")){
                int startIndex = content.indexOf("'DisplayName''=");
                int endIndex = content.indexOf(',');
                String displayName = content.substring(startIndex + "'DisplayName''=".length(), endIndex);

                Element attribute = destDoc.createElement("attribute");
                attribute.setAttribute("name", displayName.replace("'",""));
                Element xslValueOf = destDoc.createElement("xsl:value-of");
                xslValueOf.setAttribute("select", "../category[@name='".concat(catPathName)
                        .concat("']/attribute[@name=").concat(displayName).concat("]"));
                attribute.appendChild(xslValueOf);
                contentNode.appendChild(attribute);

                System.out.println("Content Attr: "+ displayName);

            }
        });*/
         /*NamedNodeMap contentNodeAttrs = contentNode.getAttributes();
        Attr id = destDoc.createAttribute("id");
        id.setValue(catId);
        contentNodeAttrs.setNamedItem(id);*/
//        contentNode.setTextContent(decodedContent.replace("DisplayName", "\n 'DisplayName'"));
        xslTemplateCategory.appendChild(contentNode);

        xslStylesheet.appendChild(xslTemplateCategory);



        destDoc.appendChild(xslStylesheet);

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
