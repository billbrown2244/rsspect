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
 * The &lt;copyright> element.
 * </p>
 * <p>
 * From the <a href="http://cyber.law.harvard.edu/rss/rss.html">RSS 2.0
 * specification</a>...
 * </p>
 * <p>
 * Copyright notice for content in the channel.
 * </p>
 * 
 * @author Bill Brown
 * 
 */
public class Copyright implements Serializable {

	private static final long serialVersionUID = 26097518613338635L;

	private final String copyright;

	Copyright(String copyright) throws RSSpectException {
		if (copyright == null || copyright.equals("")) {
			throw new RSSpectException("copyright SHOULD NOT be blank.");
		}
		this.copyright = copyright;
	}

	Copyright(Copyright copyright) {
		this.copyright = copyright.copyright;
	}

	/**
	 * @return the copyright information.
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * Shows the contents of the &lt;copyright> element.
	 */
	@Override
	public String toString() {
		return "<copyright>" + copyright + "</copyright>";
	}
}
