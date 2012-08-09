package com.tabbie.android.radar.http;

/**
 * ServerResponse.java
 * 
 * Created on: June 16, 2012
 * Author: vkarpov
 * 
 * Interface for what we care about from server responses.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tabbie.android.radar.MessageType;

public class ServerResponse {
	public final int code;
	public final String content;
	public final MessageType responseTo;
	
	public ServerResponse(int code, String content, MessageType responseTo) {
		this.code = code;
		this.content = content;
		this.responseTo = responseTo;
	}
	
	public JSONObject parseJsonContent() {
		try {
			return new JSONObject(this.content);
		} catch (JSONException e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
	
	public JSONArray parseJsonArray() {
    try {
      return new JSONArray(this.content);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }
	
}