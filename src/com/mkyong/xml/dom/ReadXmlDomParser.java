package com.mkyong.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

          for (int temp = 0; temp < list.getLength(); temp++) {

              Node node = list.item(temp);

              if (node.getNodeType() == Node.ELEMENT_NODE) {

                  Element element = (Element) node;

                  // get staff's attribute
                  String userLabel = element.getAttribute("userLabel");

                  

                  /*NodeList salaryNodeList = element.getElementsByTagName("salary");
                  String salary = salaryNodeList.item(0).getTextContent();

                  // get salary's attribute
                  String currency = salaryNodeList.item(0).getAttributes().getNamedItem("currency").getTextContent();

                  System.out.println("Current Element :" + node.getNodeName());
                  System.out.println("Staff Id : " + id);
                
                  System.out.printf("Salary [Currency] : %,.2f [%s]%n%n", Float.parseFloat(salary), currency);*/
                  
                  System.out.println("userLabel "+userLabel);
                  
                 NodeList inventoryUnitNodeList= element.getElementsByTagName("inventoryUnit");
                  for(int i=0;i<inventoryUnitNodeList.getLength();i++) {
                	  Node inventoryUnitNode=inventoryUnitNodeList.item(i);
                	  
                	  if(inventoryUnitNode.getNodeType()==Node.ELEMENT_NODE) {
                		  Element inventoryUnitElement=(Element)inventoryUnitNode;
                		  String inventoryUnitType=inventoryUnitElement.getAttribute("inventoryUnitType");
                		  if(inventoryUnitType !=null && !"".equals(inventoryUnitType))
                		  System.out.println("inventoryUnitType "+inventoryUnitType);
                		  
                		  String vendorUnitFamilyType=inventoryUnitElement.getAttribute("vendorUnitFamilyType");
                		  if(vendorUnitFamilyType !=null && !"".equals(vendorUnitFamilyType))
                		  System.out.println("vendorUnitFamilyType "+vendorUnitFamilyType);
                		  
                		  
                	  }
                  }

              }
          }

      } catch (ParserConfigurationException | SAXException | IOException e) {
          e.printStackTrace();
      }

  }

}