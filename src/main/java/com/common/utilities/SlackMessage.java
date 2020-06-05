package com.common.utilities;

import java.io.Serializable;


public class SlackMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
    private String text;
    private String icon_emoji;
    private String channel;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIcon_emoji() {
		return icon_emoji;
	}
	public void setIcon_emoji(String icon_emoji) {
		this.icon_emoji = icon_emoji;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
