package com.eelengine.engine;

import com.eelengine.engine.sprite.Sprite;
import com.eelengine.engine.sprite.SpriteInstance;
import com.eelengine.engine.sprite.SpriteInstanceManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TestLoadLevel {
    Sprite masterSprite;
    SpriteInstanceManager manager;

    public TestLoadLevel(Sprite masterSprite, SpriteInstanceManager manager) {
        this.masterSprite = masterSprite;
        this.manager = manager;
    }

    public void doIt() {
        try {
            File fXmlFile = new File("nongit/level1.tmx");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            parseWholeXML(doc.getDocumentElement());


//            NodeList nList = doc.getElementsByTagName("layer");
//            System.out.println("----------------------------");
//
//            for (int temp = 0; temp < nList.getLength(); temp++) {
//                Node nNode = nList.item(temp);
//                System.out.println("\nCurrent Element :" + nNode.getNodeName());
//                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element eElement = (Element) nNode;
//                    System.out.println(eElement.getAttribute("id"));
//                    System.out.println(eElement.getAttribute("width"));
//                    System.out.println(eElement.getAttribute("height"));
//                    System.out.println(eElement.getAttribute("name"));
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private static int[][] parseCSVIntGrid(String data, int width, int height){
        int[][] out=new int[width][height];
        String [] lines=data.trim().split("\n");
        for(int y=0;y<width;y++){
            String[] terms=lines[y].split(",");
                for(int x=0;x<width;x++){
                out[x][y]=Integer.parseInt(terms[x]);
            }
        }
        return out;
    }
    private static Node getChildNodeOfType(Node parent, String type){
        if(parent==null)return null;
        NodeList childNodes = parent.getChildNodes();
        for(int i=0; i<childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                if (childNode.getNodeName().equals(type))
                    return childNode;
            }
        }
        return null;
    }
    private void parseLayer(Node layerNode){
        if(!layerNode.getNodeName().equals("layer"))throw new RuntimeException("b");
        Element e = (Element)layerNode;
        int id=Integer.parseInt(e.getAttribute("id"));
        System.out.println("layer id: "+id);
        System.out.println("layer name: "+e.getAttribute("name"));
        int width=Integer.parseInt(e.getAttribute("width"));
        int height=Integer.parseInt(e.getAttribute("height"));
        System.out.println("layer width: "+width);
        System.out.println("layer height: "+height);
        Node datas = getChildNodeOfType(layerNode,"data");
        int[][] data =parseCSVIntGrid(datas.getTextContent(),width,height);
        System.out.println(data);
        if(true){
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if(data[x][y]!=0)
                        manager.addStaticSpriteInstance(new SpriteInstance(masterSprite,x,-y,1,data[x][y]-1));
                }
            }
        }
    }
    private void parseWholeXML(Node startingNode)
    {
        NodeList childNodes = startingNode.getChildNodes();
        for(int i=0; i<childNodes.getLength(); i++)
        {
            Node childNode = childNodes.item(i);
            if(childNode.getNodeType() == Node.ELEMENT_NODE)
            {
                if(childNode.getNodeName().equals("layer"))parseLayer(childNode);
                else parseWholeXML(childNode);
            }
            else
            {

                // trim() is used to ignore new lines and spaces elements.
                if(!childNode.getTextContent().trim().isEmpty())
                {
                    System.out.println(childNode.getTextContent());
                }

            }
        }
    }
}
