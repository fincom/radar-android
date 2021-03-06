package com.tabbie.android.radar.adapters;

/**
 *  EventDetailsPagerAdapter.java
 * 
 *  Created on: Aug 12, 2012
 *      @author Justin Knutson
 * 
 *  Custom adapter to scroll through multiple
 *  events from the EventDetailsActivity with
 *  unimplemented methods for Tab Bar integration
 */

import java.util.ArrayList;

import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.tabbie.android.radar.R;
import com.tabbie.android.radar.core.cache.ImageLoader;
import com.tabbie.android.radar.model.Event;

public class EventDetailsPagerAdapter
	extends android.support.v4.view.PagerAdapter {
	
	public static final String TAG = "EventDetailsPagerAdapter";
	
	private final ImageLoader imageLoader;
	private final Context context;
	private final int pageLayout;
	private final OnClickListener clickListener;
	private final ArrayList<Event> events;
	
	public EventDetailsPagerAdapter(final Context context,
	                                final ArrayList<Event> events,
	                                final int pageLayout,
	                                final OnClickListener listener) {
		
		this.context = context;
		this.events = events;
		this.clickListener = listener;
		imageLoader = new ImageLoader(context);
		this.pageLayout = pageLayout;
	}
	
	@Override
	public Object instantiateItem(android.view.ViewGroup container, int position) {
		Log.d(TAG, "Adding View at position " + position);
		final View v = bindEvent(position);
		container.addView(v);
		return v;
	};
	
	
	
	@Override
	public void destroyItem(android.view.ViewGroup container, int position, Object object) {
		Log.d("EventDetailsPagerAdapter", "Removing View at position " + position);
		container.removeView((View) object);
	};

	@Override
	public int getCount() {
		return events.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	private View bindEvent(final int position) {
		
	  final Event e = events.get(position);
	  
		// Acquire references to all of our views
		final View v = LayoutInflater.from(context).inflate(pageLayout, null);
		
		final TextView titleView = (TextView) v.findViewById(R.id.details_event_title);
		final TextView timeView = (TextView) v.findViewById(R.id.details_event_time);
		final TextView locationView = (TextView) v.findViewById(R.id.details_event_location);
    final TextView addressView = (TextView) v.findViewById(R.id.details_event_address);
    final TextView descriptionView = (TextView) v.findViewById(R.id.details_event_description);
    final TextView coverView = (TextView) v.findViewById(R.id.details_event_price);
    
		final ImageView loaderView = (ImageView) v.findViewById(R.id.element_loader);
    final ImageView radarButton = (ImageView) v.findViewById(R.id.add_to_radar_image);
    final ImageView locationLinkView = (ImageView) v.findViewById(R.id.location_image);
    final ImageView imageView = (ImageView) v.findViewById(R.id.details_event_img);

    // Begin loading image into display
    imageLoader.displayImage(e.imageUrl.toString(), imageView);
    loaderView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate));
    
    // Set string values
    titleView.setText(e.name);
    timeView.setText(e.time.makeYourTime()); // You have no chance to survive
    locationView.setText(e.venue);
    addressView.setText(e.address);
    descriptionView.setText(e.description);
    coverView.setText(e.cost);
    
    // Make sure hyper-links are in place
    Linkify.addLinks(descriptionView, Linkify.WEB_URLS);
    
    // MapView link listeners
    locationLinkView.setOnClickListener(clickListener);
    addressView.setOnClickListener(clickListener);
    
    // Set RadarButton and listener
    radarButton.setSelected(e.onLineup);
    radarButton.setOnClickListener(clickListener);
    
    // Make sure our main view has a reference
    // to the event that's populating it
    // TODO v.setTag(position);
    v.setTag(e);
    
    return v;
	}
}