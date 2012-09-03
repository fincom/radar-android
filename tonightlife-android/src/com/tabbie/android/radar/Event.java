package com.tabbie.android.radar;

import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable, Handler.Callback {
  public final String tag;
  public final String name;
  public final String description;
  public final String venueName;
  public final String address;
  public final URL image;
  public final double lat;
  public final double lon;
  public final boolean featured;
  public final TonightlifeDatetime time;
  
  // TODO Add short minimumAge for sorting

  public int radarCount;
  private boolean onRadar;

  public Event( String id,
                String name,
                String description,
                String venueName,
                String address,
                URL image,
                double lat,
                double lon,
                int radarCount,
                boolean featured,
                String time,
                boolean onRadar) {
    this.tag = id;
    this.name = name;
    this.description = description;
    this.venueName = venueName;
    this.address = address;
    this.image = image;
    this.lat = lat;
    this.lon = lon;
    this.radarCount = radarCount;
    this.featured = featured;
    this.time = new TonightlifeDatetime(time);
    this.onRadar = onRadar;
  }

  public boolean isOnRadar() {
    return onRadar;
  }

  public void setOnRadar(boolean onRadar) {
    this.onRadar = onRadar;
  }

  public int describeContents() {
    return 0;
  }
  
  public String toString() {
    return "'" + this.name + "' radarCount=" + radarCount + " onRadar=" + onRadar;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(image.toString());
    dest.writeString(tag);
    dest.writeString(name);
    dest.writeString(description);
    dest.writeString(venueName);
    dest.writeString(address);
    dest.writeDouble(lat);
    dest.writeDouble(lon);
    dest.writeInt(radarCount);
    dest.writeInt(featured ? 1 : 0);
    dest.writeString(time.initializer);
    dest.writeInt(onRadar ? 1 : 0);
  }

  public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
    public Event createFromParcel(Parcel in) {
      String url = in.readString();
      try {
        return new Event( in.readString(),
                          in.readString(),
                          in.readString(),
                          in.readString(),
                          in.readString(),
                          new URL(url),
                          in.readDouble(),
                          in.readDouble(),
                          in.readInt(),
                          in.readInt() == 1,
                          in.readString(),
                          in.readInt() == 1);
      } catch (MalformedURLException e) {
        e.printStackTrace();
        return null;
      }
    }

    public Event[] newArray(int size) {
      return new Event[size];
    }
  };

  @Override
  public boolean handleMessage(final Message msg) {
	  // TODO Deal with Bitmap here
  	return false;
  }
}
