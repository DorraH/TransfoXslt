package org.example;

import fr.aerow.opentext.oscriptdata.OScriptAssoc;
import fr.aerow.opentext.oscriptdata.OScriptDataEmptyStringException;
import fr.aerow.opentext.oscriptdata.OScriptParser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        XmlParser xmlParser = new XmlParser();
        xmlParser.parseXmlFile("C:\\Users\\dorra\\Workspace\\ThalesProject\\export_categories_prod\\XmlInputs");


        // TODO Auto-generated method stub
        /*String strAssoc="A<1,?,'Children'={A<1,?,'DisplayLen'=8,'DisplayName'='EntityRootCode'," +
                "'FixedRows'=true,'ID'=2,'Length'=24,'MaxRows'=1,'Name'='EntityRootCode','NumRows'=1," +
                "'Required'=true,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=-1>,A<1,?," +
                "'DisplayLen'=32,'DisplayName'='EntityName','FixedRows'=true,'ID'=27,'Length'=254," +
                "'MaxRows'=1,'Name'='EntityName','NumRows'=1,'Required'=true,'Search'=true," +
                "'setName'='TH_ENTITY_MANAGEMENT','Type'=-1>,A<1,?,'DisplayLen'=8,'DisplayName'='EntitySubCode'," +
                "'FixedRows'=false,'ID'=3,'Length'=24,'MaxRows'=10,'Name'='EntitySubCode','NumRows'=1," +
                "'Required'=false,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=-1>,A<1,?," +
                "'DisplayName'='EntityManagers','FixedRows'=false,'ID'=4,'MaxRows'=10,'Name'='EntityManagers'," +
                "'NumRows'=1,'Required'=true,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT'," +
                "'Type'=14>,A<1,?,'DisplayName'='EntityMainManager','FixedRows'=true,'ID'=22,'MaxRows'=1," +
                "'Name'='EntityMainManager','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false," +
                "'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntityManagersEdit'," +
                "'FixedRows'=false,'ID'=17,'MaxRows'=50,'Name'='EntityManagersEdit','NumRows'=1,'Required'=false," +
                "'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?," +
                "'DisplayName'='EntitySecurityMgrs','FixedRows'=false,'ID'=15,'MaxRows'=50," +
                "'Name'='EntitySecurityMgrs','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false," +
                "'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntityMainSecurityMgr','FixedRows'=true,'ID'=23,'MaxRows'=1,'Name'='EntityMainSecurityMgr','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntityMainHolders','FixedRows'=false,'ID'=16,'MaxRows'=50,'Name'='EntityMainHolders','NumRows'=1,'Required'=true,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntityMainMainHolder','FixedRows'=true,'ID'=24,'MaxRows'=1,'Name'='EntityMainMainHolder','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntityRootFolder','FixedRows'=true,'ID'=5,'MaxRows'=1,'Name'='EntityRootFolder','NumRows'=1,'Required'=false,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=2>,A<1,?,'DisplayName'='EntityCategoryFolder','FixedRows'=true,'ID'=6,'MaxRows'=1,'Name'='EntityCategoryFolder','NumRows'=1,'Required'=false,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=2>,A<1,?,'DisplayName'='EntityTemplateFolder','FixedRows'=true,'ID'=7,'MaxRows'=1,'Name'='EntityTemplateFolder','NumRows'=1,'Required'=false,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=2>,A<1,?,'DisplayName'='EntityFacetFolder','FixedRows'=true,'ID'=8,'MaxRows'=1,'Name'='EntityFacetFolder','NumRows'=1,'Required'=false,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=2>,A<1,?,'DisplayName'='EntityPerspectiveFolder','FixedRows'=true,'ID'=9,'MaxRows'=1,'Name'='EntityPerspectiveFolder','NumRows'=1,'Required'=false,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=2>,A<1,?,'DisplayName'='EntityGroupEIM','FixedRows'=true,'ID'=10,'MaxRows'=1,'Name'='EntityGroupEIM','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntityGroupSecurityMgr','FixedRows'=true,'ID'=11,'MaxRows'=1,'Name'='EntityGroupSecurityMgr','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntityGroupMainHolder','FixedRows'=true,'ID'=12,'MaxRows'=1,'Name'='EntityGroupMainHolder','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntitySubGroup','FixedRows'=false,'ID'=13,'MaxRows'=50,'Name'='EntitySubGroup','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntitySubManagers','FixedRows'=false,'ID'=18,'MaxRows'=50,'Name'='EntitySubManagers','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntitySubManagersEdit','FixedRows'=false,'ID'=28,'MaxRows'=50,'Name'='EntitySubManagersEdit','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='EntitySubManagersReplace','FixedRows'=true,'ID'=29,'MaxRows'=1,'Name'='EntitySubManagersReplace','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='BwTeamLeaders','FixedRows'=false,'ID'=20,'MaxRows'=50,'Name'='BwTeamLeaders','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayName'='BwMainHolders','FixedRows'=false,'ID'=21,'MaxRows'=50,'Name'='BwMainHolders','NumRows'=1,'Required'=false,'Search'=true,'SelectGroup'=false,'setName'='TH_ENTITY_MANAGEMENT','Type'=14>,A<1,?,'DisplayLen'=32,'DisplayName'='SubEntityPerspective','FixedRows'=true,'ID'=19,'Length'=254,'MaxRows'=1,'Name'='SubEntityPerspective','NumRows'=1,'Required'=false,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=-1>,A<1,?,'Columns'=32,'DisplayName'='datatable','FixedRows'=true,'ID'=14,'MaxRows'=1,'Name'='datatable','NumRows'=1,'Required'=false,'Rows'=3,'Search'=true,'setName'='TH_ENTITY_MANAGEMENT','Type'=11>},'DisplayName'='TH_ENTITY_MANAGEMENT','FixedRows'=true,'ID'=1,'MaxRows'=1,'Name'='TH_ENTITY_MANAGEMENT','NextID'=30,'NumRows'=1,'Required'=true,'Type'=-18,'ValueTemplate'=A<1,?,'ID'=1,'Values'={A<1,?,2=A<1,?,'ID'=2,'Values'={?}>,3=A<1,?,'ID'=3,'Values'={?}>,4=A<1,?,'ID'=4,'Values'={?}>,5=A<1,?,'ID'=5,'Values'={?}>,6=A<1,?,'ID'=6,'Values'={?}>,7=A<1,?,'ID'=7,'Values'={?}>,8=A<1,?,'ID'=8,'Values'={?}>,9=A<1,?,'ID'=9,'Values'={?}>,10=A<1,?,'ID'=10,'Values'={?}>,11=A<1,?,'ID'=11,'Values'={?}>,12=A<1,?,'ID'=12,'Values'={?}>,13=A<1,?,'ID'=13,'Values'={?}>,14=A<1,?,'ID'=14,'Values'={?}>,15=A<1,?,'ID'=15,'Values'={?}>,16=A<1,?,'ID'=16,'Values'={?}>,17=A<1,?,'ID'=17,'Values'={?}>,18=A<1,?,'ID'=18,'Values'={?}>,19=A<1,?,'ID'=19,'Values'={?}>,20=A<1,?,'ID'=20,'Values'={?}>,21=A<1,?,'ID'=21,'Values'={?}>,22=A<1,?,'ID'=22,'Values'={?}>,23=A<1,?,'ID'=23,'Values'={?}>,24=A<1,?,'ID'=24,'Values'={?}>,27=A<1,?,'ID'=27,'Values'={?}>,28=A<1,?,'ID'=28,'Values'={?}>,29=A<1,?,'ID'=29,'Values'={?}>>}>>";

        // Initiate OScriptParser
        OScriptParser parser = new OScriptParser(strAssoc);


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
        }*/
    }

}






