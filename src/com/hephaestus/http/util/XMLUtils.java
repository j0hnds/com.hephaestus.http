package com.hephaestus.http.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * This class provides a variety of methods to support XML manipulations.
 * 
 * @author Dave Sieh
 *
 */
public class XMLUtils {
	
	/**
	 * Formats the specified string as XML.
	 * 
	 * @param xmlDocument the string value of the unformatted XML.
	 * 
	 * @return the formatted XML.
	 */
	public static String formatXMLString(String xmlDocument) {
		String output = null;

		StringWriter sw;
		try {
			Document doc = getDocumentFromString(xmlDocument);
			Format format = Format.getPrettyFormat();
			XMLOutputter outputter = new XMLOutputter(format);
			sw = new StringWriter();
			outputter.output(doc, sw);
			output = sw.toString();
		} catch (Exception e) {
			output = xmlDocument;
		}

		return output;
	}

	/**
	 * Constructs a JDOM Document from the specified XML string.
	 * 
	 * @param s the XML string to parse.
	 * 
	 * @return reference to the JDom document constructed from the string.
	 */
	public static Document getDocumentFromString(String s) {
		Document document = null;
		if (s != null) {
			document = getDocumentFromReader(new StringReader(s));
		}
		return document;
	}

	/**
	 * Constructs a JDom Document from the specified reader.
	 * 
	 * @param r the reader
	 * 
	 * @return the JDom Document constructed from the reader.
	 */
	public static Document getDocumentFromReader(Reader r) {
		Document document = null;
		if (r != null) {
			SAXBuilder builder = new SAXBuilder();
			try {
				document = builder.build(r);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (JDOMException e) {
				throw new RuntimeException(
						"Content of field cannot be parsed.", e); //$NON-NLS-1$
			}
		}
		return document;
	}

}
