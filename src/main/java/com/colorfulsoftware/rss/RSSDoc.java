/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.colorfulsoftware.rss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * This class reads and writes RSS documents to and from xml files, objects or
 * Strings. It contains all of the factory methods for building immutable copies
 * of the object elements.
 * 
 * 
 * @author Bill Brown
 * 
 */
public final class RSSDoc {

	/**
	 * the default document encoding of "UTF-8"
	 */
	private String encoding = "UTF-8";

	/**
	 * the default XML version of "1.0"
	 */
	private String xmlVersion = "1.0";

	private String libUri;
	private String libVersion;

	public RSSDoc(){
		try {
			Properties props = new Properties();
			props.load(RSSDoc.class.getResourceAsStream("/rsspect.properties"));
			libUri = props.getProperty("uri");
			libVersion = props.getProperty("version");
		} catch (Exception e) {
			// should not happen.
			e.printStackTrace();
		}
	}
	
	public RSSDoc(String encoding, String xmlVersion){
		this();
		this.encoding = encoding;
		this.xmlVersion = xmlVersion;
	}

	/**
	 * @return the RSSpect library version in the form of a generator element.
	 *         This element is output for all feeds that are generated by
	 *         RSSpect.
	 */
	public Generator getRSSpectVersion() {
		return buildGenerator(libUri + " v" + libVersion);
	}

	/**
	 * 
	 * @param output
	 *            the target output stream for the rss document.
	 * @param rss
	 *            the rss object containing the content of the feed
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the feed cannot be written to the output
	 */
	public void writeRSSDoc(OutputStream output, RSS rss, String encoding,
			String version) throws RSSpectException {
		try {
			writeRSSOutput(rss, XMLOutputFactory.newInstance()
					.createXMLStreamWriter(output, encoding), encoding, version);
		} catch (Exception e) {
			throw new RSSpectException("error writing rss feed: "
					+ e.getMessage());
		}
	}

	/**
	 * 
	 * @param file
	 *            the target output file for the document.
	 * @param rss
	 *            the rss object containing the content of the feed
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the feed cannot be written to the output
	 */
	public void writeRSSDoc(File file, RSS rss, String encoding, String version)
			throws RSSpectException {
		try {
			writeRSSOutput(rss, XMLOutputFactory.newInstance()
					.createXMLStreamWriter(new FileWriter(file)), encoding,
					version);
		} catch (Exception e) {
			throw new RSSpectException("error writing rss feed: "
					+ e.getMessage());
		}
	}

	/**
	 * For example: to pass the TXW
	 * com.sun.xml.txw2.output.IndentingXMLStreamWriter or the stax-utils
	 * javanet.staxutils.IndentingXMLStreamWriter for indented printing do this:
	 * 
	 * <pre>
	 * XmlStreamWriter writer = new IndentingXMLStreamWriter(XMLOutputFactory
	 * 		.newInstance().createXMLStreamWriter(
	 * 				new FileOutputStream(outputFilePath), encoding));
	 * RSSDoc.writeFeedDoc(writer, myFeed, null, null);
	 * </pre>
	 * 
	 * @param output
	 *            the target output for the feed.
	 * @param rss
	 *            the rss object containing the content of the feed
	 * @param encoding
	 *            the file encoding (default is UTF-8)
	 * @param version
	 *            the xml version (default is 1.0)
	 * @throws Exception
	 *             thrown if the feed cannot be written to the output
	 */
	public void writeRSSDoc(XMLStreamWriter output, RSS rss, String encoding,
			String version) throws RSSpectException {
		try {
			writeRSSOutput(rss, output, encoding, version);
		} catch (Exception e) {
			throw new RSSpectException("error writing rss feed: "
					+ e.getMessage());
		}
	}

	/**
	 * This method reads in a Feed element and returns the contents as an rss
	 * feed string with formatting specified by the fully qualified
	 * XMLStreamWriter class name (uses reflection internally). For example you
	 * can pass the TXW com.sun.xml.txw2.output.IndentingXMLStreamWriter or the
	 * stax-utils javanet.staxutils.IndentingXMLStreamWriter for indented
	 * printing. It will fall back to
	 * 
	 * <pre>
	 * readFeedToString(Feed)
	 * </pre>
	 * 
	 * if the XMLStreamWriter class cannot be found in the classpath.
	 * 
	 * @param rss
	 *            the rss object to be converted to an rss document string.
	 * @param xmlStreamWriter
	 *            the fully qualified XMLStreamWriter class name.
	 * @return an rss feed document string.
	 * @throws Exception
	 *             thrown if the feed cannot be returned as a String
	 */
	public String readRSSToString(RSS rss, String xmlStreamWriter)
			throws RSSpectException {
		try {
			StringWriter theString = new StringWriter();

			Class<?> cls = Class.forName(xmlStreamWriter);
			Constructor<?> ct = cls
					.getConstructor(new Class[] { XMLStreamWriter.class });
			Object arglist[] = new Object[] { XMLOutputFactory.newInstance()
					.createXMLStreamWriter(theString) };
			XMLStreamWriter writer = (XMLStreamWriter) ct.newInstance(arglist);

			writeRSSOutput(rss, writer, encoding, xmlVersion);

			return theString.toString();

		} catch (Exception e) {
			return readRSSToString(rss);
		}
	}

	/**
	 * This method reads in a Feed bean and returns the contents as an rss feed
	 * string.
	 * 
	 * @param rss
	 *            the rss object to be converted to an rss string.
	 * @return an rss feed document string.
	 * @throws Exception
	 *             thrown if the feed cannot be returned as a String
	 */
	public String readRSSToString(RSS rss) throws RSSpectException {
		try {
			StringWriter theString = new StringWriter();

			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = outputFactory
					.createXMLStreamWriter(theString);

			writeRSSOutput(rss, writer, encoding, xmlVersion);

			return theString.toString();
		} catch (Exception e) {
			throw new RSSpectException("error reading rss feed: "
					+ e.getMessage());
		}
	}

	/**
	 * This method reads an xml string into a Feed element.
	 * 
	 * @param xmlString
	 *            the xml string to be transformed into a RSS element.
	 * @return the RSS element
	 * @throws Exception
	 *             if the string cannot be parsed into a RSS element.
	 */
	public RSS readRSSToBean(String xmlString) throws RSSpectException {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = inputFactory
					.createXMLStreamReader(new java.io.StringReader(xmlString));
			return new RSSReader().readRSS(reader);
		} catch (Exception e) {
			throw new RSSpectException("error reading rss feed: "
					+ e.getMessage());
		}
	}

	/**
	 * This method reads an xml File object into a Feed element.
	 * 
	 * @param file
	 *            the file object representing an rss feed.
	 * @return the RSS element.
	 * @throws Exception
	 *             if the file cannot be parsed into a RSS element.
	 */
	public RSS readRSSToBean(File file) throws RSSpectException {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = inputFactory
					.createXMLStreamReader(new FileInputStream(file));
			return new RSSReader().readRSS(reader);
		} catch (Exception e) {
			throw new RSSpectException("error reading rss feed: "
					+ e.getMessage());
		}
	}

	/**
	 * This method reads an rss file from a URL into a Feed element.
	 * 
	 * @param url
	 *            the Internet network location of an rss file.
	 * @return the RSS element.
	 * @throws Exception
	 *             if the URL cannot be parsed into a RSS element.
	 */
	public RSS readRSSToBean(java.net.URL url) throws RSSpectException {
		try {
			return readRSSToBean(url.openStream());
		} catch (RSSpectException e) {
			throw e;
		} catch (Exception e) {
			throw new RSSpectException("error reading rss feed: "
					+ e.toString() + ": " + e.getMessage());
		}

	}

	/**
	 * This method reads an rss file from an input stream into a RSS element.
	 * 
	 * @param inputStream
	 *            the input stream containing an rss file.
	 * @return the RSS element.
	 * @throws Exception
	 *             if the URL cannot be parsed into a RSS element.
	 */
	public RSS readRSSToBean(InputStream inputStream) throws RSSpectException {
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLStreamReader reader = inputFactory
					.createXMLStreamReader(inputStream);
			return new RSSReader().readRSS(reader);
		} catch (Exception e) {
			throw new RSSpectException("error reading rss feed: "
					+ e.getMessage());
		}
	}

	/**
	 * 
	 * @param channel
	 *            the unique channel element (required)
	 * @param attributes
	 *            additional attributes (optional)
	 * @param extensions
	 *            additional extensions (optional)
	 * @return an immutable RSS object.
	 * @throws RSSpectException.
	 */
	public RSS buildRSS(Channel channel, List<Attribute> attributes,
			List<Extension> extensions) throws RSSpectException {
		return new RSS(channel, attributes, extensions);
	}

	/**
	 * 
	 * @param name
	 *            the attribute name.
	 * @param value
	 *            the attribute value.
	 * @return an immutable Attribute object.
	 */
	public Attribute buildAttribute(String name, String value) {
		return new Attribute(name, value);
	}

	/**
	 * 
	 * @param author
	 *            the author element. (required)
	 * @return an immutable Author object.
	 */
	public Author buildAuthor(String author) {
		return new Author(author);
	}

	/**
	 * @param domain
	 *            the domain attribute
	 * @param category
	 *            the category text
	 * @return an immutable Category object.
	 */
	public Category buildCategory(Attribute domain, String category)
			throws RSSpectException {
		return new Category(domain, category);
	}

	/**
	 * 
	 * @param title
	 * @param link
	 * @param description
	 * @param language
	 * @param copyright
	 * @param managingEditor
	 * @param webMaster
	 * @param pubDate
	 * @param lastBuildDate
	 * @param categories
	 * @param generator
	 * @param docs
	 * @param cloud
	 * @param ttl
	 * @param image
	 * @param rating
	 * @param textInput
	 * @param skipHours
	 * @param skipDays
	 * @param items
	 * @param extensions
	 * @return an immutable Channel object.
	 * @throws RSSpectException
	 */
	public Channel buildChannel(Title title, Link link,
			Description description, Language language, Copyright copyright,
			ManagingEditor managingEditor, WebMaster webMaster,
			PubDate pubDate, LastBuildDate lastBuildDate,
			List<Category> categories, Generator generator, Docs docs,
			Cloud cloud, TTL ttl, Image image, Rating rating,
			TextInput textInput, SkipHours skipHours, SkipDays skipDays,
			List<Extension> extensions, List<Item> items)
			throws RSSpectException {
		return new Channel(title, link, description, language, copyright,
				managingEditor, webMaster, pubDate, lastBuildDate, categories,
				generator, docs, cloud, ttl, image, rating, textInput,
				skipHours, skipDays, extensions, items);
	}

	/**
	 * 
	 * @param cloud
	 * @return an immutable Cloud object.
	 */
	public Cloud buildCloud(List<Attribute> attributes) throws RSSpectException {
		return new Cloud(attributes);
	}

	/**
	 * 
	 * @param comments
	 * @return an immutable Comments object.
	 */
	public Comments buildComments(String comments) {
		return new Comments(comments);
	}

	/**
	 * 
	 * @param copyright
	 * @return an immutable Copyright object.
	 */
	public Copyright buildCopyright(String copyright) {
		return new Copyright(copyright);
	}

	/**
	 * 
	 * @param description
	 * @return an immutable Description object.
	 */
	public Description buildDescription(String description) {
		return new Description(description);
	}

	/**
	 * 
	 * @param docs
	 * @return an immutable Docs object.
	 */
	public Docs buildDocs(String docs) {
		return new Docs(docs);
	}

	/**
	 * 
	 * @param attributes
	 *            should contain url, length and type
	 * @param enclosure
	 * @return an immutable Enclosure object.
	 * @throws RSSpectException
	 */
	public Enclosure buildEnclosure(List<Attribute> attributes)
			throws RSSpectException {
		return new Enclosure(attributes);
	}

	/**
	 * 
	 * @param elementName
	 *            the name of the extension element.
	 * @param attributes
	 *            additional attributes.
	 * @param content
	 *            the content of the extension element.
	 * @return an immutable Extension object.
	 * @throws RSSpectException
	 */
	public Extension buildExtension(String elementName,
			List<Attribute> attributes, String content) throws RSSpectException {
		return new Extension(elementName, attributes, content);
	}

	/**
	 * @param text
	 *            the text content.
	 * @return an immutable Generator object.
	 */
	public Generator buildGenerator(String text) {
		return new Generator(text);
	}

	/**
	 * 
	 * @param isPermaLink
	 * @param guid
	 * @return an immutable GUID object.
	 */
	public GUID buildGUID(Attribute isPermaLink, String guid) {
		return new GUID(isPermaLink, guid);
	}

	/**
	 * 
	 * @param height
	 *            should be a number 400 or less
	 * @return an immutable Height object.
	 * @throws RSSpectException
	 */
	public Height buildHeight(String height) throws RSSpectException {
		return new Height(height);
	}

	/**
	 * 
	 * @param url
	 * @param title
	 * @param link
	 * @param width
	 * @param height
	 * @param description
	 * @return an immutable Image object.
	 * @throws RSSpectException
	 */
	public Image buildImage(URL url, Title title, Link link, Width width,
			Height height, Description description) throws RSSpectException {
		return new Image(url, title, link, width, height, description);
	}

	/**
	 * 
	 * @param title
	 * @param link
	 * @param description
	 * @param author
	 * @param categories
	 * @param comments
	 * @param enclosure
	 * @param guid
	 * @param pubDate
	 * @param source
	 * @param extensions
	 * @return an immutable Item object.
	 * @throws RSSpectException
	 */
	public Item buildItem(Title title, Link link, Description description,
			Author author, List<Category> categories, Comments comments,
			Enclosure enclosure, GUID guid, PubDate pubDate, Source source,
			List<Extension> extensions) throws RSSpectException {
		return new Item(title, link, description, author, categories, comments,
				enclosure, guid, pubDate, source, extensions);
	}

	/**
	 * 
	 * @param language
	 * @return an immutable Language object.
	 */
	public Language buildLanguage(String language) {
		return new Language(language);
	}

	/**
	 * 
	 * @param lastBuildDate
	 * @return an immutable LastBuildDate object.
	 */
	public LastBuildDate buildLastBuildDate(Date lastBuildDate) {
		return new LastBuildDate(lastBuildDate);
	}

	/**
	 * 
	 * @param link
	 * @return an immutable Link object.
	 * @throws RSSpectException
	 */
	public Link buildLink(String link) throws RSSpectException {
		return new Link(link);
	}

	/**
	 * 
	 * @param managingEditor
	 * @return an immutable ManagingEditor object.
	 */
	public ManagingEditor buildManagingEditor(String managingEditor) {
		return new ManagingEditor(managingEditor);
	}

	/**
	 * 
	 * @param name
	 * @return an immutable Name object.
	 */
	public Name buildName(String name) {
		return new Name(name);
	}

	/**
	 * 
	 * @param pubDate
	 * @return an immutable PubDate object.
	 */
	public PubDate buildPubDate(Date pubDate) {
		return new PubDate(pubDate);
	}

	/**
	 * 
	 * @param rating
	 * @return an immutable Rating object.
	 */
	public Rating buildRating(String rating) {
		return new Rating(rating);
	}

	/**
	 * 
	 * @param skipDays
	 * @throws RSSpectException
	 * @return an immutable SkipDays object.
	 */
	public SkipDays buildSkipDays(List<Day> skipDays) throws RSSpectException {
		return new SkipDays(skipDays);
	}

	/**
	 * 
	 * @param skipHours
	 * @throws RSSpectException
	 * @return an immutable SkipHours object.
	 */
	public SkipHours buildSkipHours(List<Hour> skipHours)
			throws RSSpectException {
		return new SkipHours(skipHours);
	}

	public Day buildDay(String day) throws RSSpectException {
		return new Day(day);
	}

	public Hour buildHour(String hour) throws RSSpectException {
		return new Hour(hour);
	}

	/**
	 * 
	 * @param url
	 * @param source
	 * @return an immutable Source object.
	 * @throws RSSpectException
	 */
	public Source buildSource(Attribute url, String source)
			throws RSSpectException {
		return new Source(url, source);
	}

	/**
	 * 
	 * @param title
	 * @param description
	 * @param name
	 * @param link
	 * @return an immutable TextInput object.
	 * @throws RSSpectException
	 */
	public TextInput buildTextInput(Title title, Description description,
			Name name, Link link) throws RSSpectException {
		return new TextInput(title, description, name, link);
	}

	/**
	 * 
	 * @param title
	 * @return an immutable Title object.
	 */
	public Title buildTitle(String title) {
		return new Title(title);
	}

	/**
	 * 
	 * @param ttl
	 * @return an immutable TTL object.
	 */
	public TTL buildTTL(String ttl) {
		return new TTL(ttl);
	}

	/**
	 * 
	 * @param url
	 * @return an immutable URL object.
	 * @throws RSSpectException
	 */
	public URL buildURL(String url) throws RSSpectException {
		return new URL(url);
	}

	/**
	 * 
	 * @param webMaster
	 * @return an immutable WebMaster object.
	 */
	public WebMaster buildWebMaster(String webMaster) {
		return new WebMaster(webMaster);
	}

	/**
	 * 
	 * @param width
	 * @return an immutable Width object.
	 * @throws RSSpectException
	 */
	public Width buildWidth(String width) throws RSSpectException {
		return new Width(width);
	}

	// used to write feed output for several feed writing methods.
	private void writeRSSOutput(RSS rss, XMLStreamWriter writer,
			String encoding, String version) throws Exception {

		Channel channel = rss.getChannel();

		rss = buildRSS(buildChannel(channel.getTitle(), channel.getLink(),
				channel.getDescription(), channel.getLanguage(), channel
						.getCopyright(), channel.getManagingEditor(), channel
						.getWebMaster(), channel.getPubDate(), channel
						.getLastBuildDate(), channel.getCategories(),
				getRSSpectVersion(), channel.getDocs(), channel.getCloud(),
				channel.getTtl(), channel.getImage(), channel.getRating(),
				channel.getTextInput(), channel.getSkipHours(), channel
						.getSkipDays(), channel.getExtensions(), channel
						.getItems()), rss.getAttributes(), rss.getExtensions());

		// write the xml header.
		writer.writeStartDocument(encoding, version);
		new RSSWriter().writeRSS(writer, rss);
		writer.flush();
		writer.close();
	}

	public String getEncoding() {
		return encoding;
	}

	public String getXmlVersion() {
		return xmlVersion;
	}
	
	// checks for and returns the Attribute from the String attribute (argument)
	// in the list of attributes (argument)
	Attribute getAttributeFromGroup(List<Attribute> attributes,
			String attributeName) {
		if (attributes != null) {
			for (Attribute current : attributes) {
				if (current.getName().equalsIgnoreCase(attributeName)) {
					return buildAttribute(current.getName(), current.getValue());
				}
			}
		}
		return null;
	}
}
