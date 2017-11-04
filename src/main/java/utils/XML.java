package utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * http://www.java2s.com/Code/Java/XML/NewDocumentFromInputStream.htm
 *
 * @author Grzegorz Olechwierowicz
 * @since 24.04.2017.
 */
public class XML {
  public static Document newDocumentFromInputStream(InputStream in) {
    DocumentBuilderFactory factory = null;
    DocumentBuilder builder = null;
    Document ret = null;

    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }

    try {
      assert builder != null;
      ret = builder.parse(new InputSource(in));
    } catch (SAXException | IOException e) {
      e.printStackTrace();
    }
    return ret;
  }
}
