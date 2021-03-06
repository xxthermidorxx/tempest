package tempest.spout;

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
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class XmlReadoutSpout extends BaseRichSpout {

    private SpoutOutputCollector _collector;
    private List<String> _tagTextList;
    private Random _rand;

    /**
     * parse xml file
     * @param String the name of the xml file
     * @return Document the content of xml
     */
    protected Document _parseXml(String filename) throws Exception {
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        return builder.parse(new File(filename));
    }

    /**
     * get text list at the tag the user designates
     * @param Document the content of xml
     * @param String the name of the xml file
     * @return List<String> the tag text list
     */
    protected List<String> _getTagTextList(Document xmlDocumentContent, String tagPath) throws Exception {
        XPathFactory xpathFactory= XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        NodeList entries = (NodeList) xpath.evaluate(tagPath, xmlDocumentContent, XPathConstants.NODESET);

        List<String> list = new ArrayList<String>();
        for(int i=0; i<entries.getLength(); i++) {
            list.add( entries.item(i).getNodeValue() );
        }
        return list;
    }

    /**
     * constructor
     * @param String the name of the xml file
     * @return void
     */
    public XmlReadoutSpout() throws Exception {
        _rand = new Random();

        Document xmlDocumentContent = _parseXml("tempest/data/medline00015.xml");
        String tagPath = "/MedlineCitationSet/MedlineCitation/Article/ArticleTitle/text()";
        _tagTextList = _getTagTextList(xmlDocumentContent, tagPath);
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void nextTuple() {
        Utils.sleep(100);
        String sentence = _tagTextList.get( _rand.nextInt( _tagTextList.size() ) );
        // TODO: このemitはISpoutOutputDeclarerのメソッド呼び出しか？？？
        _collector.emit( /*streamdId="xml", tuple=*/new Values(sentence) /*messageId="parsed_xml"*/ );
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}
