package com.tabbie.android.radar;

/**
 * ServerDeleteRequest.java
 * 
 * Created on: July 30, 2012 Author: vkarpov
 * 
 * A specialized HTTP DELETE ServerRequest.
 */

public class ServerDeleteRequest extends ServerRequest {
  public ServerDeleteRequest(String url, MessageType type) {
    super(url, "GET", type);
  }

  @Override
  public String getOutput() {
    return null;
  }

  @Override
  public boolean hasOutput() {
    return false;
  }

}