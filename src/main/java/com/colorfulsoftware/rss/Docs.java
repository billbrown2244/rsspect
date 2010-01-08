/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.colorfulsoftware.rss;

import java.io.Serializable;

/**
 * <p>
 * The &lt;author> element.
 * </p>
 * <p>
 * From the <a href="http://cyber.law.harvard.edu/rss/rss.html">RSS 2.0
 * specification</a>...
 * </p>
 * <p>
 * A URL that points to the documentation for the format used in the RSS file.
 * It's probably a pointer to this page. It's for people who might stumble
 * across an RSS file on a Web server 25 years from now and wonder what it is.
 * </p>
 * 
 * @author Bill Brown
 * 
 */
public class Docs implements Serializable {

	private static final long serialVersionUID = 1840987541596737383L;

	private final String docs;

	Docs(String docs) throws RSSpectException {
		if (docs == null || docs.equals("")) {
			throw new RSSpectException("docs SHOULD NOT be blank.");
		}
		this.docs = docs;
	}

	Docs(Docs docs) {
		this.docs = docs.docs;
	}

	/**
	 * @return the documentation information for the rss format in url form.
	 */
	public String getDocs() {
		return docs;
	}

	/**
	 * Shows the contents of the &lt;docs> element.
	 */
	@Override
	public String toString() {
		return "<docs>" + docs + "</docs>";
	}

}
