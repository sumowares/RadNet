/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radnet.conf;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 *
 * @author Shoshanna
 */
public class ConfigurationContainer {

    public static List<String> romfile;
    public static boolean recursive;
    public static boolean downloads;
    public static boolean skiphidden;

    public static boolean twitterEnabled;

    public static boolean LoadConfig() {

        try {
            // initialize the directory array list.
            romfile = new ArrayList<String>();
            // get startup path
            File f = new File(".");
            String startupFolder = f.getCanonicalPath();

            File confXml = new File(startupFolder + "/conf/conf.xml");
            if (confXml.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(confXml);
                doc.getDocumentElement().normalize();
                NodeList nListDirectory = doc.getElementsByTagName("directory_conf");
                NodeList nListDirectories = doc.getElementsByTagName("romfile");
                NodeList nListSocialTwitter = doc.getElementsByTagName("twitter_conf");

                // process the root directory information
                for (int temp = 0; temp < nListDirectory.getLength(); temp++) {
                    Node nNode = nListDirectory.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        recursive = Boolean.valueOf(eElement.getElementsByTagName("recursion").item(0).getTextContent());
                        downloads = Boolean.valueOf(eElement.getElementsByTagName("downloads").item(0).getTextContent());
                        skiphidden = Boolean.valueOf(eElement.getElementsByTagName("skiphid").item(0).getTextContent());
                    }
                }

                // process the rom folders (directories)
                for (int temp = 0; temp < nListDirectories.getLength(); temp++) {
                    Node nNode = nListDirectories.item(temp);
                    NodeList directories = nNode.getChildNodes();
                    
                    for (int i = 0; i < directories.getLength(); i++) {
                        Node currentDirectory = directories.item(i);
                        if (currentDirectory.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) currentDirectory;
                            romfile.add(eElement.getAttribute("location"));
                        }
                    }
                }
                
                
                for (int temp = 0; temp < nListSocialTwitter.getLength(); temp++) {
                    Node nNode = nListSocialTwitter.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (eElement.getElementsByTagName("pin").item(0).getTextContent() != "") {
                            twitterEnabled = true;
                            TwitterInformation.pin = eElement.getElementsByTagName("pin").item(0).getTextContent();
                            TwitterInformation.user_id = eElement.getElementsByTagName("user_id").item(0).getTextContent();
                            TwitterInformation.screen_name = eElement.getElementsByTagName("screen_name").item(0).getTextContent();
                            TwitterInformation.token_id = eElement.getElementsByTagName("token_id").item(0).getTextContent();
                            TwitterInformation.token_secret = eElement.getElementsByTagName("token_secret").item(0).getTextContent();
                            TwitterInformation.allowgames = Boolean.valueOf(eElement.getElementsByTagName("allowgames").item(0).getTextContent());
                            TwitterInformation.allowinfo = Boolean.valueOf(eElement.getElementsByTagName("allowinfo").item(0).getTextContent());
                        }
                        else {
                            twitterEnabled = false;
                            System.out.println("Not linked with Twitter.");
                        }
                    }
                }
                
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return false;
    }

}
