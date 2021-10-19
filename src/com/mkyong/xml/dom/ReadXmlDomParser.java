package com.mkyong.xml.dom;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXmlDomParser {

  private static final String FILENAME = "/Users/rahugupta/Rahul_Data/UST_Work/staff.xml";

  public static void main(String[] args) {

      // Instantiate the Factory
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      try {

          // optional, but recommended
          // process XML securely, avoid attacks like XML External Entities (XXE)
          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

          // parse XML file
          DocumentBuilder db = dbf.newDocumentBuilder();

          Document doc = db.parse(new File(FILENAME));

          // optional, but recommended
          // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
          doc.getDocumentElement().normalize();

          System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
          System.out.println("------");

          // get <staff>
          NodeList list = doc.getElementsByTagName("ManagedElement");
          String attributeArr[]= {"inventoryUnitType","vendorUnitFamilyType","vendorUnitTypeNumber","vendorName","serialNumber","dateOfManufacture","unitPosition","manufacturerData"};
          Map<String, Map<String,String>> ManagedElementAttributeMap =new HashMap<String, Map<String,String>>();
          for (int temp = 0; temp < list.getLength(); temp++) {

              Node node = list.item(temp);
              
              if (node.getNodeType() == Node.ELEMENT_NODE) {

                  Element element = (Element) node;

                  // get staff's attribute
                  String userLabel = element.getAttribute("userLabel");
 
                  
                  System.out.println("userLabel "+userLabel);
                  
                 NodeList inventoryUnitNodeList= element.getElementsByTagName("inventoryUnit");
                 
                 Map<String,String> inventoryUnitTagAttributeMap =new HashMap<String,String>();
                 
                  for(int i=0;i<inventoryUnitNodeList.getLength();i++) {
                	  Node inventoryUnitNode=inventoryUnitNodeList.item(i);
                	  String attributeVal="";
                	  if(inventoryUnitNode.getNodeType()==Node.ELEMENT_NODE) {
                		  Element inventoryUnitElement=(Element)inventoryUnitNode;
                		  for(int k=0;k<attributeArr.length;k++) {
                			  attributeVal= getSpecificTagAttributeData(inventoryUnitElement,attributeArr[k]);
                			  
                			  if(attributeVal !=null && !"".equals(attributeVal)) {
                    			  System.out.println("attributeName => "+attributeArr[k]+" ****** "+"attributeVal "+attributeVal);
                			  inventoryUnitTagAttributeMap.put(attributeArr[k], attributeVal);
                			  }
                		  }
                		  
                		  
                		
                		  
                		  
                	  }
                	  System.out.println("inventoryUnitNodeList is processed for inventoryUnit Number ="+i);
                	  System.out.println("****************************************************");
                  }
                  ManagedElementAttributeMap.put(userLabel,  inventoryUnitTagAttributeMap);
                  System.out.println("ManagedElementList is processed for ManagedElement Number ="+temp);
                  System.out.println("****************************************************");

              }
              
          }
          
         /* Iterator<Map.Entry<String, Map<String, String>>> parent = ManagedElementAttributeMap.entrySet().iterator();
          while (parent.hasNext()) {
              Map.Entry<String, Map<String, String>> parentPair = parent.next();
              System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());

              Iterator<Map.Entry<String, String>> child = (parentPair.getValue()).entrySet().iterator();
              while (child.hasNext()) {
                  Map.Entry childPair = child.next();
                  System.out.println("childPair.getKey() :   " + childPair.getKey() + " childPair.getValue()  :  " + childPair.getValue());

                 // child.remove(); // avoids a ConcurrentModificationException
              }

          }*/
          
          //csv writer
          writeExcel(ManagedElementAttributeMap);

      } catch (ParserConfigurationException | SAXException | IOException e) {
          e.printStackTrace();
      }

  }
  
  public static String getSpecificTagAttributeData(Element nodeElement,String attributeName){
	  String attributeVal=nodeElement.getAttribute(attributeName);
	  
	  return attributeVal;
  }
  
  public static void writeExcel(Map<String, Map<String,String>> ManagedElementAttributeMap) 
  {
      //Blank workbook
      XSSFWorkbook workbook = new XSSFWorkbook(); 
       
      //Create a blank sheet
      XSSFSheet sheet = workbook.createSheet("TestData");
        
     
          Iterator<Map.Entry<String, Map<String, String>>> parent = ManagedElementAttributeMap.entrySet().iterator();
         
          String headerArr[]= {"ManagedElment-UserLabel","InventoryUnit-inventoryUnitType","InventoryUnit-vendorUnitFamilyType","InventoryUnit-vendorUnitTypeNumber","InventoryUnit-vendorName","InventoryUnit-serialNumber","InventoryUnit-dateOfManufacture","InventoryUnit-unitPosition","InventoryUnit-manufacturerData"};
          
          Row row = sheet.createRow(0);
          int cellnum = 0;
          for(String header:headerArr) {
          Cell cell = row.createCell(cellnum++);
          cell.setCellValue(header);
          }
          
          int rownum = 1;
          while (parent.hasNext()) {
              Map.Entry<String, Map<String, String>> parentPair = parent.next();
              System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());
              
              row = sheet.createRow(rownum++);
              cellnum = 0;
              Cell cell = row.createCell(cellnum++);
              cell.setCellValue((String)parentPair.getKey());
              
              Iterator<Map.Entry<String, String>> child = (parentPair.getValue()).entrySet().iterator();
              while (child.hasNext()) {
                  Map.Entry childPair = child.next();
                  System.out.println("childPair.getKey() :   " + childPair.getKey() + " childPair.getValue()  :  " + childPair.getValue());
                  Cell cell1 = row.createCell(cellnum++);
                  cell1.setCellValue((String)childPair.getValue());
                 // child.remove(); // avoids a ConcurrentModificationException
              }

          }
       
          
      try
      {
          //Write the workbook in file system
          FileOutputStream out = new FileOutputStream(new File("/Users/rahugupta/Rahul_Data/UST_Work/howtodoinjava_demo.xlsx"));
          workbook.write(out);
          out.close();
          System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
      } 
      catch (Exception e) 
      {
          e.printStackTrace();
      }
  }
  

}