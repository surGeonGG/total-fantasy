package Main;

import org.xml.sax.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;


public class EventHandler {


    public EventHandler(Main main) {

        Document xmlDOM = buildDOM("./res/events/Events.xml");

        xmlDOM.getDocumentElement();

        String root = xmlDOM.getDocumentElement().getNodeName();

    }

    public void handleEvent(String biome, String id) {

//        main.getGui().getTextArea().changeText(biome + " " + id);
    }

    private Document buildDOM(String source) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringElementContentWhitespace(true);

            factory.setIgnoringComments(true);

            factory.setValidating(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new InputSource(source));

        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
