package ru.gkomega.api.openparser.xstream.utils;

import lombok.experimental.UtilityClass;
import org.xml.sax.InputSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@UtilityClass
public class FormatUtils {

    public static String formatXml(final String xml) {
        try {
            final Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            final Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            final StreamResult streamResult = new StreamResult(new ByteArrayOutputStream());

            serializer.transform(xmlSource, streamResult);

            return new String(((ByteArrayOutputStream) streamResult.getOutputStream()).toByteArray());
        } catch (Exception e) {
            return xml;
        }
    }
}
