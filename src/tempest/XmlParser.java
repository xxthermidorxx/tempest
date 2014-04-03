package tempest;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {

    public static class XmlParserByXpath {
        private String _filename;
        private String _location;
        private Document _document;

        /**
         * constructor
         * @param String xmlFile
         * @return void
         */
        public XmlParserByXpath(String xmlFile) {
            _filename = xmlFile;
            _location = "/MedlineCitationSet/MedlineCitation/Article/ArticleTitle/text()";
        }

        /**
         * set document
         * @return void
         */
        private void setDocument() throws Exception {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            _document = builder.parse(new File(_filename));
        }

        /**
         * parse xml by javax.xml.xpath
         * @return Arraylist<String> list
         */
        public List<String> parseXmlByJavaxXpath() throws Exception {
            setDocument();

            XPathFactory xpathFactory= XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            NodeList entries = (NodeList) xpath.evaluate(_location, _document, XPathConstants.NODESET);

            List<String> list = new ArrayList<String>();
            for(int i=0; i<entries.getLength(); i++) {
                list.add( entries.item(i).getNodeValue() );
            }
            System.out.println(list.size());
            return list;
        }

        /**
         * parse xml by org.apache.xpath;
         * @return Arraylist<String> list
         */
        public List<String> parseXmlByApacheXpath() throws Exception {
            setDocument();
            List<String> list = new ArrayList<String>();
            /*
            NodeIterator ni = XPathAPI.selectNodeIterator(_document, _location);
            for(Node node: ni) {
                list.add( node.getNodeValue() );
            }
            */
            return list;
        }
    }

    public static void main(String[] args) throws Exception{
        XmlParserByXpath xp = new XmlParserByXpath("tempest/data/medline00015.xml");
        System.out.println( xp.parseXmlByJavaxXpath() );
    }
}
