package org.example;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class XmlParser {

    private String outputDirectoryPath = "C:\\Users\\dorra\\Workspace\\ThalesProject\\export_categories_prod\\Outputs\\"
            + LocalDate.now() + "\\" + System.currentTimeMillis();
    private String notUsedPath = outputDirectoryPath + "\\NOT_USED\\";
    private String simplePath = outputDirectoryPath + "\\SIMPLE\\";
    private String complexPath = outputDirectoryPath + "\\COMPLEX\\";
    private String missingPath = outputDirectoryPath + "\\MISSING\\";
    private String csvRefPath = "C:\\Users\\dorra\\Workspace\\ThalesProject\\cat_used_by_migrated_workspaces.xlsx";
    static private Map<String, String> catIds ;

    public void parseXmlFile(String xmlInpuPath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


            /*Document document = docBuilder.newDocument();
            Node testNode = document.createElement("llnode");
            testNode.appendChild(doc.getElementsByTagName("content").item(0));
            document.importNode(doc.getFirstChild(), true);*/

            catIds = extractIdsCsvRef(csvRefPath);



            Files.createDirectories(Paths.get(notUsedPath));
            Files.createDirectories(Paths.get(simplePath));
            Files.createDirectories(Paths.get(complexPath));
            Files.createDirectories(Paths.get(missingPath));

            for (File file : Paths.get(xmlInpuPath).toFile().listFiles()){
                System.out.println("CatIds Size: " + catIds.size());
                readAndClassifyLlNodeBlocs(docBuilder, file.getPath(), catIds);
            }

            /*System.out.println("CatIds Size: " + catIds.size());
            readAndClassifyLlNodeBlocs(docBuilder, xmlInpuPath, catIds);
            System.out.println("CatIds Size: " + catIds.size());
            readAndClassifyLlNodeBlocs(docBuilder,
                    "C:\\Users\\dorra\\Workspace\\ThalesProject\\export_categories_prod\\XmlInputs\\france_metadata.xml", catIds);
            System.out.println("CatIds Size: " + catIds.size());*/


            /*if(Files.exists(Paths.get(missingPath))){
                for (File file : Paths.get(missingPath).toFile().listFiles())
                    file.delete();
                Files.delete(Paths.get(missingPath));
            }
            Files.createDirectories(Paths.get(missingPath));*/

            File missingIds = new File(outputDirectoryPath.concat("\\missing.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter(missingIds));
            writer.write(catIds.keySet().toString());
            writer.close();

            for (String id : catIds.keySet()){
                writeXmlDoc(docBuilder.newDocument(), missingPath.concat(id).concat(".xslt"));
            }





            //Import content Nodes from source file
//            importContentNodes(document, doc);

            //Write the XML document to a string or file
//            writeXmlDoc(document, "C:\\Users\\dorra\\Workspace\\ThalesProject\\export_categories_prod\\export_categories_prod-content2.xslt");

        }catch (ParserConfigurationException builderException){
            System.out.print("Error creating DocumentBuilder: ".concat(builderException.getMessage()));
//            builderException.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map<String, String> extractIdsCsvRef (String idsRefFile) throws IOException {
        //Extract ids from csv Ref
        FileInputStream file = new FileInputStream(idsRefFile);

        //Create Workbook instance holding reference to .xlsx file
        Workbook wb = new XSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        Sheet ws = wb.getSheetAt(0);

        //Iterate through each rows one by one
        List<Categorie> categories = new ArrayList<>();
        ws.removeRow(ws.getRow(0));
        Map<String, String> catIds = new HashMap<>();
        for (Row row : ws){
            String catId = row.getCell(0).getCellType().equals(CellType.NUMERIC) ?
                    String.valueOf((long)row.getCell(0).getNumericCellValue()): row.getCell(0).getStringCellValue();

            System.out.println(catId);
            Categorie categorie = new Categorie(catId, row.getCell(1).getCellType().equals(CellType.NUMERIC) ?
                    String.valueOf(row.getCell(1).getNumericCellValue()) : row.getCell(1).getStringCellValue(),
                    row.getCell(2).getCellType().equals(CellType.NUMERIC) ?
                            String.valueOf(row.getCell(2).getNumericCellValue()) : row.getCell(2).getStringCellValue(),
                    row.getCell(3).getCellType().equals(CellType.NUMERIC) ?
                            String.valueOf(row.getCell(3).getNumericCellValue()) : row.getCell(3).getStringCellValue());
            catIds.put(catId, categorie.catPath);
            categories.add(categorie);
        }
        file.close();

        return catIds;
    }

    private void readAndClassifyLlNodeBlocs(DocumentBuilder docBuilder, String xmlInpuPath, Map<String, String> catIds){

        try {
            Document xmlInputDoc = docBuilder.parse(xmlInpuPath);

            //Read and classify llnodes
            NodeList llnodes = xmlInputDoc.getElementsByTagName("llnode");

            System.out.println("llnodes length: " + llnodes.getLength());

            for (int i = 0; i < llnodes.getLength(); i++){
                String catId = llnodes.item(i).getAttributes().getNamedItem("id").getTextContent();
                String objType = llnodes.item(i).getAttributes().getNamedItem("objtype").getTextContent();
                if(! objType.equals("132")) {
                    if(catIds.get(catId) == null){
                        Document docClassificationTest = docBuilder.newDocument();
                        docClassificationTest.appendChild(docClassificationTest.importNode(llnodes.item(i), true));
                        writeXmlDoc(docClassificationTest, notUsedPath.concat(catId).concat(".xslt"));
//                    catIds.remove(catId);
                    }else {

                        //Classify content
                        Document docClassificationTest = docBuilder.newDocument();
                        Element llnode = (Element)llnodes.item(i);
                        classifyUsedId(docClassificationTest, llnode.getElementsByTagName("content").item(0), catId, catIds.get(catId));
                        catIds.remove(catId);
                    }
                }
            }

        }catch (IOException | SAXException parsingException){
            System.out.print("Error parsing file: ".concat(parsingException.getMessage()));
//            ioException.printStackTrace();
        }catch (TransformerException transformerException){
            System.out.println("Error when writing xml Doc "+ transformerException.getMessage());
        }

    }

    private void classifyUsedId(Document destDoc, Node content, String catId, String catPath) throws TransformerException{
//        Node content = llNode.getLastChild();
        String nodeValue = content.getTextContent().replaceAll("\n", "").replaceAll(" ","");

        String nodeValueDecoded = new String(Base64.getDecoder().decode(nodeValue), StandardCharsets.UTF_8);


        if(nodeValueDecoded.contains("'Type'=-18&gt") | nodeValueDecoded.contains("'FixedRows'=false") | nodeValueDecoded.contains("'Required'=true")){
            Element xslNode = destDoc.createElement("xsl");
            Node contentNode = destDoc.createElement("content");
            NamedNodeMap contentNodeAttrs = contentNode.getAttributes();
            Attr id = destDoc.createAttribute("id");
            id.setValue(catId);
            contentNodeAttrs.setNamedItem(id);
            contentNode.setTextContent(nodeValueDecoded.replace("DisplayName", "\n 'DisplayName'"));
            xslNode.appendChild(contentNode);
            destDoc.appendChild(xslNode);

            writeXmlDoc(destDoc, complexPath.concat(catId).concat(".xslt"));
        }else {
            SimpleXsltFormatter simpleXsltFormatter = new SimpleXsltFormatter(nodeValueDecoded, catId, catPath);
            destDoc = simpleXsltFormatter.formatXslt(destDoc);
            System.out.println("SimpleContent: "+ destDoc.getFirstChild().getTextContent());

            writeXmlDoc(destDoc, simplePath.concat(catId).concat(".xslt"));
        }
    }

    private void importContentNodes(Document destDocument, Document sourceDocument){
        Element contentsBloc = destDocument.createElement("contents");
        NodeList contents = sourceDocument.getElementsByTagName("content");
        String nodeValue0 = contents.item(0).getTextContent().replaceAll("\n", "").replaceAll(" ","");
        /*byte[] bytes = Base64.getDecoder().decode("QTwxLD8sJ0NoaWxkcmVuJz17QTwxLD8sJ0Rpc3BsYXlMZW4nPTMyLCdEaXNwbGF5TmFtZSc9J091dHB1dCBmaWxlIG5hbWUnLCdGaXhlZFJvd3MnPXRydWUsJ0lEJz0yLCdMZW5ndGgnPTMyLCdNYXhSb3dzJz0xLCdOdW1Sb3dzJz0xLCdSZXF1aXJlZCc9" +
                "ZmFsc2UsJ1NlYXJjaCc9dHJ1ZSwnVHlwZSc9LTE+LEE8MSw/LCdEaXNwbGF5TGVuJz0zMiwnRGlzcGxheU5hbWUnPSdGb2xkZXJzIGxpc3QnLCdGaXhlZFJvd3MnPXRydWUsJ0lEJz0zLCdMZW5ndGgnPTMyLCdNYXhSb3dzJz0xLCdOdW1Sb3dzJz0xLCdS" +
                "ZXF1aXJlZCc9ZmFsc2UsJ1NlYXJjaCc9dHJ1ZSwnVHlwZSc9LTE+LEE8MSw/LCdEaXNwbGF5TmFtZSc9J0Rlc3RpbmF0aW9uIElEJywnRml4ZWRSb3dzJz10cnVlLCdJRCc9NCwnTWF4Um93cyc9MSwnTnVtUm93cyc9MSwnUmVxdWlyZWQnPWZhbHNlLCdT" +
                "ZWFyY2gnPXRydWUsJ1R5cGUnPTI+LEE8MSw/LCdEaXNwbGF5TmFtZSc9J1BhZ2luYXRpb24nLCdGaXhlZFJvd3MnPXRydWUsJ0lEJz01LCdNYXhSb3dzJz0xLCdOdW1Sb3dzJz0xLCdSZXF1aXJlZCc9ZmFsc2UsJ1NlYXJjaCc9dHJ1ZSwnVHlwZSc9Mj4s" +
                "QTwxLD8sJ0Rpc3BsYXlOYW1lJz0nVGVtcGxhdGUgSUQnLCdGaXhlZFJvd3MnPXRydWUsJ0lEJz02LCdNYXhSb3dzJz0xLCdOdW1Sb3dzJz0xLCdSZXF1aXJlZCc9ZmFsc2UsJ1NlYXJjaCc9dHJ1ZSwnVHlwZSc9Mj4sQTwxLD8sJ0Rpc3BsYXlMZW4nPTMy" +
                "LCdEaXNwbGF5TmFtZSc9J1NlbmQgbWFpbCcsJ0ZpeGVkUm93cyc9dHJ1ZSwnSUQnPTcsJ0xlbmd0aCc9MzIsJ01heFJvd3MnPTEsJ051bVJvd3MnPTEsJ1JlcXVpcmVkJz1mYWxzZSwnU2VhcmNoJz10cnVlLCdUeXBlJz0tMT4sQTwxLD8sJ0Rpc3BsYXlM" +
                "ZW4nPTMyLCdEaXNwbGF5TmFtZSc9J0V4ZWN1dGlvbiBtb2RlJywnRml4ZWRSb3dzJz10cnVlLCdJRCc9OCwnTGVuZ3RoJz0zMiwnTWF4Um93cyc9MSwnTnVtUm93cyc9MSwnUmVxdWlyZWQnPWZhbHNlLCdTZWFyY2gnPXRydWUsJ1R5cGUnPS0xPixBPDEs" +
                "PywnRGlzcGxheUxlbic9MzIsJ0Rpc3BsYXlOYW1lJz0nT3V0cHV0IGZpbGUgZm9ybWF0JywnRml4ZWRSb3dzJz10cnVlLCdJRCc9OSwnTGVuZ3RoJz0zMiwnTWF4Um93cyc9MSwnTnVtUm93cyc9MSwnUmVxdWlyZWQnPWZhbHNlLCdTZWFyY2gnPXRydWUs" +
                "J1R5cGUnPS0xPixBPDEsPywnRGlzcGxheU5hbWUnPSdDb21tdW5pdHkgSUQnLCdGaXhlZFJvd3MnPXRydWUsJ0lEJz0xMCwnTWF4Um93cyc9MSwnTnVtUm93cyc9MSwnUmVxdWlyZWQnPWZhbHNlLCdTZWFyY2gnPXRydWUsJ1R5cGUnPTI+fSwnRGlzcGxh" +
                "eU5hbWUnPSdEb2N1bWVudGFyeSBJbmRleCBDYXRlZ29yeScsJ0ZpeGVkUm93cyc9dHJ1ZSwnSUQnPTEsJ01heFJvd3MnPTEsJ05hbWUnPSdEb2N1bWVudGFyeV9JbmRleF9DYXRlZ29yeScsJ05leHRJRCc9MTEsJ051bVJvd3MnPTEsJ1JlcXVpcmVkJz1m" +
                "YWxzZSwnVHlwZSc9LTE4LCdWYWx1ZVRlbXBsYXRlJz1BPDEsPywnSUQnPTEsJ1ZhbHVlcyc9e0E8MSw/LDI9QTwxLD8sJ0lEJz0yLCdWYWx1ZXMnPXs/fT4sMz1BPDEsPywnSUQnPTMsJ1ZhbHVlcyc9ez99Piw0PUE8MSw/LCdJRCc9NCwnVmFsdWVzJz17" +
                "P30+LDU9QTwxLD8sJ0lEJz01LCdWYWx1ZXMnPXs/fT4sNj1BPDEsPywnSUQnPTYsJ1ZhbHVlcyc9ez99Piw3PUE8MSw/LCdJRCc9NywnVmFsdWVzJz17P30+LDg9QTwxLD8sJ0lEJz04LCdWYWx1ZXMnPXs/fT4sOT1BPDEsPywnSUQnPTksJ1ZhbHVlcyc9" +
                "ez99PiwxMD1BPDEsPywnSUQnPTEwLCdWYWx1ZXMnPXs/fT4+fT4+");*/
        String nodeValueDecoded0 = new String(Base64.getDecoder().decode(nodeValue0), StandardCharsets.UTF_8);

        for (int i = 0; i < contents.getLength(); i++) {
            Node node = destDocument.importNode(contents.item(i), true);
            String nodeValue = contents.item(i).getTextContent().replaceAll("\n", "").replaceAll(" ","");
            String nodeValueDecoded = new String(Base64.getDecoder().decode(nodeValue), StandardCharsets.UTF_8);

            /*Node earth = doc.getFirstChild();
            NamedNodeMap earthAttributes = earth.getAttributes();
            Attr galaxy = doc.createAttribute("galaxy");
            galaxy.setValue("milky way");
            earthAttributes.setNamedItem(galaxy);*/

            Node contentNode = destDocument.createElement(destDocument.importNode(contents.item(i), true).getNodeName());
            NamedNodeMap contentNodeAttrs = contentNode.getAttributes();
            Attr type = destDocument.createAttribute("type");
            type.setValue("base64");
            contentNodeAttrs.setNamedItem(type);
            contentNode.setTextContent(nodeValueDecoded.replace("DisplayName", "\n 'DisplayName'"));
            contentsBloc.appendChild(contentNode);
//            contentsBloc.appendChild(destDocument.importNode(contents.item(i), true));
        }
        destDocument.appendChild(contentsBloc);
    }

    private void writeXmlDoc(Document docToWrite, String destPath) throws TransformerException{
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(docToWrite);
        StreamResult result = new StreamResult(new File(destPath));
        transformer.transform(source, result);
        /*StreamResult consoleResult = new StreamResult(System.out);
        transformer.transform(source, consoleResult);*/
    }

    private void manipulate_xml(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("C:\\Users\\dorra\\Workspace\\ThalesProject\\export_categories_prod\\export_categories_prod.xml");

            //Adding an Attribute
            Node earth = doc.getFirstChild();
            NamedNodeMap earthAttributes = earth.getAttributes();
            Attr galaxy = doc.createAttribute("galaxy");
            galaxy.setValue("milky way");
            earthAttributes.setNamedItem(galaxy);

            //Adding a Child Tag
            Node canada = doc.createElement("country");
            canada.setTextContent("ca");
            earth.appendChild(canada);

            //Write the XML document to a string or file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
//            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\Users\\dorra\\Workspace\\ThalesProject\\export_categories_prod\\export_categories_prod-modified.xml"));
            transformer.transform(source, result);
//            transformer.transform(source, result);

            /*String xmlString = result.getWriter().toString();
            System.out.println(xmlString);*/
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);


            //Writing xml File Result
            /*XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            try (FileOutputStream output =
                         new FileOutputStream("C:\\Users\\dorra\\Workspace\\ThalesProject\\test-modified.xml")) {

                xmlOutput.output((org.jdom2.Document) doc, output);
//                writeXml(doc, output);
            }*/

        }catch (ParserConfigurationException builderException){
            System.out.print("Error creating DocumentBuilder: ".concat(builderException.getMessage()));
//            builderException.printStackTrace();
        }catch (IOException|SAXException parsingException){
            System.out.print("Error parsing file: ".concat(parsingException.getMessage()));
//            ioException.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /*private static void writeXml(Document doc,
                             OutputStream output)
        throws TransformerException, UnsupportedEncodingException {

    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    // The default add many empty new line, not sure why?
    // https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
    // Transformer transformer = transformerFactory.newTransformer();

    // add a xslt to remove the extra newlines
    Transformer transformer = transformerFactory.newTransformer(
            new StreamSource(new File(FORMAT_XSLT)));

    // pretty print
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(output);

    transformer.transform(source, result);

}*/
}
