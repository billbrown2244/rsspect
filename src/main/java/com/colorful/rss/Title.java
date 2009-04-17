package com.colorful.rss;

import java.io.Serializable;

/**
 * The name of the channel. It's how people refer to your service. If you have
 * an HTML website that contains the same information as your RSS file, the
 * title of your channel should be the same as the title of your website.
 * 
 * @author Bill Brown
 * 
 */
public class Title implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8102075738201739128L;
	
	private final String text;
    
    /**
     * 
     * @param text the plain text.
     */
    public Title(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
