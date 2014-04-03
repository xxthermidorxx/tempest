package tempest.test.spout;

import java.io.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.Util.Utils;

import tempest.spout.XmlReadoutSpout;

public class XmlReadoutSpoutTest extends XmlReadoutSpout {
    private SpoutOutputCollector _collector;
    private List<String> _tagTextList;
    private Random _rand;

    @Override
    protected Document _parseXml(String filename) {
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        return builder.parse(new File(filename));
    }

    @Override
    protected List<String> _getTagTextList(Document xmlDocumentContent, String tagPath) {
        XPathFactory xpathFactory= XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        NodeList entries = (NodeList) xpath.evaluate(tagPath, xmlDocumentContent, XPathConstants.NODESET);

        List<String> list = new ArrayList<String>();
        for(int i=0; i<entries.getLength(); i++) {
            list.add( entries.item(i).getNodeValue() );
        }
        System.out.println(list.size());
        return list;
    }

    /**
     * constructor
     * @param String the name of the xml file
     * @return void
     */
    public XmlReadoutSpoutTest() {
        _rand = new Random();

        Document xmlDocumentContent = _parseXml("../data/medline00015.xml");
        String tagPath = "/MedlineCitationSet/MedlineCitation/Article/ArticleTitle";
        _tagTextList = _getTagTextList(xmlDocumentContent, tagPath);
    }

    /**
     * constructor
     * @param String the name of the xml file
     * @return void
     */
    public void getTagTextList() {
        System.out.println(_tagTextList.size());
        for(String tagText : _tagTextList) {
            System.out.println(tagText);
        }
    }

    public static void main(String[] args) {
        XmlReadoutSpoutTest xrstest = new XmlReadoutSpoutTest();
        xrstest.getTagTextList();
    }
}
