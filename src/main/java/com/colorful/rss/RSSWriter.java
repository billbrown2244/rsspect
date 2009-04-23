/**
 * Copyright (C) 2009 William R. Brown <info@colorfulsoftware.com>
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
package com.colorful.rss;

import java.util.List;

import javax.xml.stream.XMLStreamWriter;

/**
 * Used by the RSSDoc to write a RSS bean to a xml file or java String.
 * 
 * @author Bill Brown
 * 
 */
class RSSWriter {

	// used internally by FeedDoc to write feed to output streams.
	void writeRSS(XMLStreamWriter writer, RSS rss) throws Exception {

		// open the feed element
		writer.writeStartElement("rss");
		if (rss.getAttributes() != null) {
			for (Attribute attr : rss.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}

		// write the channel
		if (rss.getChannel() != null) {
			writeChannel(writer, rss.getChannel());
		}

		// write the extensions
		if (rss.getExtensions() != null) {
			writeExtensions(writer, rss.getExtensions());
		}

		writer.writeEndElement();
	}

	void writeChannel(XMLStreamWriter writer, Channel channel) throws Exception {

		writer.writeStartElement("channel");

		// write the required elements
		writeTitle(writer, channel.getTitle());

		writeLink(writer, channel.getLink());

		writeDescription(writer, channel.getDescription());

		// write the optional elements

		if (channel.getLanguage() != null) {
			writeLanguage(writer, channel.getLanguage());
		}

		if (channel.getCopyright() != null) {
			writeCopyright(writer, channel.getCopyright());
		}

		if (channel.getManagingEditor() != null) {
			writeManagingEditor(writer, channel.getManagingEditor());
		}

		if (channel.getWebMaster() != null) {
			writeWebMaster(writer, channel.getWebMaster());
		}

		if (channel.getPubDate() != null) {
			writePubDate(writer, channel.getPubDate());
		}

		if (channel.getLastBuildDate() != null) {
			writeLastBuildDate(writer, channel.getLastBuildDate());
		}

		if (channel.getCategories() != null) {
			writeCategories(writer, channel.getCategories());
		}

		if (channel.getGenerator() != null) {
			writeGenerator(writer, channel.getGenerator());
		}

		if (channel.getDocs() != null) {
			writeDocs(writer, channel.getDocs());
		}

		if (channel.getCloud() != null) {
			writeCloud(writer, channel.getCloud());
		}

		if (channel.getTtl() != null) {
			writeTTL(writer, channel.getTtl());
		}

		if (channel.getImage() != null) {
			writeImage(writer, channel.getImage());
		}

		if (channel.getRating() != null) {
			writeRating(writer, channel.getRating());
		}

		if (channel.getTextInput() != null) {
			writeTextInput(writer, channel.getTextInput());
		}

		if (channel.getSkipHours() != null) {
			writeSkipHours(writer, channel.getSkipHours());
		}

		if (channel.getSkipDays() != null) {
			writeSkipDays(writer, channel.getSkipDays());
		}

		if (channel.getItems() != null) {
			writeItems(writer, channel.getItems());
		}

		if (channel.getExtensions() != null) {
			writeExtensions(writer, channel.getExtensions());
		}

		writer.writeEndElement();
	}

	void writeItems(XMLStreamWriter writer, List<Item> items) throws Exception {

		for (Item item : items) {

			writer.writeStartElement("item");

			// one of these two is required
			if (item.getTitle() != null) {
				writeTitle(writer, item.getTitle());
			}

			if (item.getDescription() != null) {
				writeDescription(writer, item.getDescription());
			}

			// write the optional elements
			if (item.getLink() != null) {
				writeLink(writer, item.getLink());
			}

			if (item.getAuthor() != null) {
				writeAuthor(writer, item.getAuthor());
			}

			if (item.getCategories() != null) {
				writeCategories(writer, item.getCategories());
			}

			if (item.getComments() != null) {
				writeComments(writer, item.getComments());
			}

			if (item.getEnclosure() != null) {
				writeEnclosure(writer, item.getEnclosure());
			}

			if (item.getGuid() != null) {
				writeGUID(writer, item.getGuid());
			}

			if (item.getPubDate() != null) {
				writePubDate(writer, item.getPubDate());
			}

			if (item.getSource() != null) {
				writeSource(writer, item.getSource());
			}

			if (item.getExtensions() != null) {
				writeExtensions(writer, item.getExtensions());
			}

			writer.writeEndElement();
		}

	}

	void writeAuthor(XMLStreamWriter writer, Author author) throws Exception {
		writer.writeStartElement("author");
		writer.writeCharacters(author.getAuthor());
		writer.writeEndElement();
	}

	void writeCategories(XMLStreamWriter writer, List<Category> categories)
			throws Exception {

		for (Category category : categories) {

			writer.writeStartElement("category");

			if (category.getDomain() != null) {
				writer.writeAttribute(category.getDomain().getName(), category
						.getDomain().getValue());
			}
			writer.writeCharacters(category.getCategory());
			writer.writeEndElement();

		}
	}

	void writeCloud(XMLStreamWriter writer, Cloud cloud) throws Exception {
		writer.writeStartElement("cloud");
		writer.writeCharacters(cloud.getCloud());
		writer.writeEndElement();
	}

	void writeComments(XMLStreamWriter writer, Comments comments)
			throws Exception {
		writer.writeStartElement("comments");
		writer.writeCharacters(comments.getComments());
		writer.writeEndElement();
	}

	void writeCopyright(XMLStreamWriter writer, Copyright copyright)
			throws Exception {
		writer.writeStartElement("copyright");
		writer.writeCharacters(copyright.getCopyright());
		writer.writeEndElement();
	}

	void writeDescription(XMLStreamWriter writer, Description description)
			throws Exception {
		writer.writeStartElement("description");
		writer.writeCharacters(description.getDescription());
		writer.writeEndElement();
	}

	void writeDocs(XMLStreamWriter writer, Docs docs) throws Exception {
		writer.writeStartElement("docs");
		writer.writeCharacters(docs.getDocs());
		writer.writeEndElement();
	}

	void writeEnclosure(XMLStreamWriter writer, Enclosure enclosure)
			throws Exception {

		writer.writeStartElement("enclosure");

		if (enclosure.getAttributes() != null) {
			for (Attribute attr : enclosure.getAttributes()) {
				writer.writeAttribute(attr.getName(), attr.getValue());
			}
		}

		writer.writeCharacters(enclosure.getEnclosure());
		writer.writeEndElement();
	}

	void writeExtensions(XMLStreamWriter writer, List<Extension> extensions)
			throws Exception {

		for (Extension extension : extensions) {

			// if there is no content, then
			// write an empty extension element.
			if (extension.getContent() == null) {
				String elementName = extension.getElementName();
				if (elementName.indexOf(":") == -1) {
					writer.writeEmptyElement(elementName);
				} else {
					String prefix = elementName.substring(0, elementName
							.indexOf(":"));
					String localName = elementName.substring(elementName
							.indexOf(":") + 1);
					writer.writeEmptyElement(prefix, localName, "");
				}
				if (extension.getAttributes() != null) {
					for (Attribute attr : extension.getAttributes()) {
						writer.writeAttribute(attr.getName(), attr.getValue());
					}
				}
			} else {
				String elementName = extension.getElementName();
				if (elementName.indexOf(":") == -1) {
					writer.writeStartElement(elementName);
				} else {
					String prefix = elementName.substring(0, elementName
							.indexOf(":"));
					String localName = elementName.substring(elementName
							.indexOf(":") + 1);
					writer.writeStartElement(prefix, localName, "");
				}
				if (extension.getAttributes() != null) {
					for (Attribute attr : extension.getAttributes()) {
						writer.writeAttribute(attr.getName(), attr.getValue());
					}
				}
				// add the content.
				writer.writeCharacters(extension.getContent());

				// close the element.
				writer.writeEndElement();
			}
		}
	}

	void writeGenerator(XMLStreamWriter writer, Generator generator)
			throws Exception {
		writer.writeStartElement("generator");
		writer.writeCharacters(generator.getGenerator());
		writer.writeEndElement();
	}

	void writeGUID(XMLStreamWriter writer, GUID guid) throws Exception {
		writer.writeStartElement("guid");

		if (guid.getIsPermaLink() != null) {
			writer.writeAttribute(guid.getIsPermaLink().getName(), guid
					.getIsPermaLink().getValue());
		}
		writer.writeCharacters(guid.getGuid());
		writer.writeEndElement();
	}

	void writeHeight(XMLStreamWriter writer, Height height) throws Exception {
		writer.writeStartElement("height");
		writer.writeCharacters(height.getHeight());
		writer.writeEndElement();
	}

	void writeImage(XMLStreamWriter writer, Image image) throws Exception {

		writer.writeStartElement("height");

		// write requried elements
		writeURL(writer, image.getUrl());
		writeTitle(writer, image.getTitle());
		writeLink(writer, image.getLink());

		// write optional elements
		if (image.getWidth() != null) {
			writeWidth(writer, image.getWidth());
		}

		if (image.getHeight() != null) {
			writeHeight(writer, image.getHeight());
		}

		if (image.getDescription() != null) {
			writeDescription(writer, image.getDescription());
		}

		writer.writeEndElement();
	}

	void writeLanguage(XMLStreamWriter writer, Language language)
			throws Exception {
		writer.writeStartElement("language");
		writer.writeCharacters(language.getLanguage());
		writer.writeEndElement();
	}

	void writeLastBuildDate(XMLStreamWriter writer, LastBuildDate lastBuildDate)
			throws Exception {
		writer.writeStartElement("lastBuildDate");
		writer.writeCharacters(lastBuildDate.getText());
		writer.writeEndElement();
	}

	void writeLink(XMLStreamWriter writer, Link link) throws Exception {
		writer.writeStartElement("link");
		writer.writeCharacters(link.getLink());
		writer.writeEndElement();
	}

	void writeManagingEditor(XMLStreamWriter writer,
			ManagingEditor managingEditor) throws Exception {
		writer.writeStartElement("managingEditor");
		writer.writeCharacters(managingEditor.getManagingEditor());
		writer.writeEndElement();
	}

	void writeName(XMLStreamWriter writer, Name name) throws Exception {
		writer.writeStartElement("name");
		writer.writeCharacters(name.getName());
		writer.writeEndElement();
	}

	void writePubDate(XMLStreamWriter writer, PubDate pubDate) throws Exception {
		writer.writeStartElement("lastBuildDate");
		writer.writeCharacters(pubDate.getText());
		writer.writeEndElement();
	}

	void writeRating(XMLStreamWriter writer, Rating rating) throws Exception {
		writer.writeStartElement("rating");
		writer.writeCharacters(rating.getRating());
		writer.writeEndElement();
	}

	void writeSkipDays(XMLStreamWriter writer, SkipDays skipDays)
			throws Exception {
		writer.writeStartElement("skipDays");
		writer.writeCharacters(skipDays.getSkipDays());
		writer.writeEndElement();
	}

	void writeSkipHours(XMLStreamWriter writer, SkipHours skipHours)
			throws Exception {
		writer.writeStartElement("skipHours");
		writer.writeCharacters(skipHours.getSkipHours());
		writer.writeEndElement();
	}

	void writeSource(XMLStreamWriter writer, Source source) throws Exception {
		writer.writeStartElement("source");

		if (source.getUrl() != null) {
			writer.writeAttribute(source.getUrl().getName(), source.getUrl()
					.getValue());
		}
		writer.writeCharacters(source.getSource());
		writer.writeEndElement();
	}

	void writeTextInput(XMLStreamWriter writer, TextInput textInput)
			throws Exception {

		writer.writeStartElement("textInput");
		writeTitle(writer, textInput.getTitle());
		writeDescription(writer, textInput.getDescription());
		writeName(writer, textInput.getName());
		writeLink(writer, textInput.getLink());
		writer.writeEndElement();

	}

	void writeTitle(XMLStreamWriter writer, Title title) throws Exception {
		writer.writeStartElement("title");
		writer.writeCharacters(title.getTitle());
		writer.writeEndElement();
	}

	void writeTTL(XMLStreamWriter writer, TTL ttl) throws Exception {
		writer.writeStartElement("ttl");
		writer.writeCharacters(ttl.getTtl());
		writer.writeEndElement();
	}

	void writeURL(XMLStreamWriter writer, URL url) throws Exception {
		writer.writeStartElement("url");
		writer.writeCharacters(url.getUrl());
		writer.writeEndElement();
	}

	void writeWebMaster(XMLStreamWriter writer, WebMaster webMaster)
			throws Exception {
		writer.writeStartElement("webMaster");
		writer.writeCharacters(webMaster.getWebMaster());
		writer.writeEndElement();
	}

	void writeWidth(XMLStreamWriter writer, Width width) throws Exception {
		writer.writeStartElement("width");
		writer.writeCharacters(width.getWidth());
		writer.writeEndElement();
	}

	void writeXHTML(XMLStreamWriter writer, String text) throws Exception {
		try {

			if (text.indexOf('<') == -1) {
				writer.writeCharacters(text);
			} else {
				// write up until the
				// opening of the next element
				writer.writeCharacters(text.substring(0, text.indexOf('<')));
				text = text.substring(text.indexOf('<') + 1);

				// get the opening element content
				String startElement = text.substring(0, text.indexOf('>'))
						.trim();

				// check for empty element
				if (startElement.indexOf('/') == startElement.length() - 1) {
					// check for attributes
					String[] attributes = startElement.split(" ");
					if (attributes.length > 1) {
						// if the name has a prefix, just
						// write it as part of the local name.
						writer.writeEmptyElement(attributes[0]);
						for (int i = 1; i < attributes.length; i++) {
							if (attributes[i].indexOf("=") != -1) {
								// we nee to put everything
								// to the right of the first '=' sign
								// in the value part because we could have
								// a query string with multiple '=' signs.
								String attrName = attributes[i].substring(0,
										attributes[i].indexOf('='));
								String attrValue = attributes[i]
										.substring(attributes[i].indexOf('=') + 1);
								writer.writeAttribute(attrName, attrValue);
							}
						}
					} else {
						// if the name has a prefix, just
						// write it as part of the local name.
						writer.writeEmptyElement(startElement);
					}
					// search for the next element
					writeXHTML(writer, text.substring(text.indexOf('>') + 1));
				} else {// this is regular start element
					// check for attributes
					String[] attributes = startElement.split(" ");
					if (attributes.length > 1) {
						// if the name has a prefix,
						// just write it as part of the local name.
						writer.writeStartElement(attributes[0]);
						for (int i = 1; i < attributes.length; i++) {
							if (attributes[i].indexOf("=") != -1) {
								String attrName = attributes[i].substring(0,
										attributes[i].indexOf('='));
								String attrValue = attributes[i]
										.substring(attributes[i].indexOf('=') + 1);
								writer.writeAttribute(attrName, attrValue);
							}
						}
					} else {
						// if the name has a prefix,
						// just write it as part of the local name.
						writer.writeStartElement(startElement);
					}
					// write the characters up until
					// the beginning of the end element.
					text = text.substring(text.indexOf('>') + 1);

					if (attributes.length > 1) {
						writer.writeCharacters(text.substring(0, text
								.indexOf("</" + attributes[0])));
						text = text.substring(text
								.indexOf("</" + attributes[0])
								+ ("</" + attributes[0] + ">").length());
					} else {
						writer.writeCharacters(text.substring(0, text
								.indexOf("</" + startElement)));
						text = text.substring(text.indexOf("</" + startElement)
								+ ("</" + startElement + ">").length());
					}

					// write the end element
					writer.writeEndElement();

					writeXHTML(writer, text);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Content is not valid XHTML", e);
		}
	}
}