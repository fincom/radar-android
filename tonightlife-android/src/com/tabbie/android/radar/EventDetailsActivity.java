package com.tabbie.android.radar;

/**
 *  EventDetailsActivity.java
 *
 *  Created on: July 25, 2012
 *      Author: Valeri Karpov
 *      
 *  Super simple activity for displaying a more detailed view of an event.
 *  All we do is just set a bunch of layout views to match our event model
 */

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.tabbie.android.radar.EventDetailsPagerAdapter.LineupSelectedCallback;
import com.tabbie.android.radar.core.BundleChecker;
import com.tabbie.android.radar.http.ServerDeleteRequest;
import com.tabbie.android.radar.http.ServerPostRequest;
import com.tabbie.android.radar.http.ServerResponse;

public class EventDetailsActivity extends ServerThreadActivity
	implements LineupSelectedCallback, OnClickListener {
	
  private Event e;
  private RadarCommonController commonController;
  private String token;
  
  @SuppressWarnings("unchecked")
  private static final List<Pair<String, Class<?> > >
      REQUIRED_INTENT_PARAMS = Arrays.asList(
          new Pair<String, Class<?> >("eventId", String.class),
          new Pair<String, Class<?> >("controller", RadarCommonController.class),
          new Pair<String, Class<?> >("token", String.class)
      );

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.event_details_activity);

    Bundle starter = getIntent().getExtras();
    BundleChecker b = new BundleChecker(starter);
    if (!b.check(REQUIRED_INTENT_PARAMS)) {
      Log.e("BundleChecker", "Bundle check failed for EventDetailsActivity!");
      this.finish();
      return;
    }
    
    final String eventId = starter.getString("eventId");
    commonController = starter.getParcelable("controller");
    e = commonController.getEvent(eventId);
    token = starter.getString("token");
    
    final ViewPager pager = (ViewPager) findViewById(R.id.details_event_pager);
    new EventDetailsPagerAdapter(this, commonController, R.layout.event_details_element, pager, this);
    pager.setCurrentItem(commonController.eventsList.indexOf(e));

  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent();
    intent.putExtra("controller", commonController);
    setResult(RESULT_OK, intent);
    super.onBackPressed();
  }

  @Override
  protected boolean handleServerResponse(ServerResponse resp) {
    // Assume that ADD_TO_RADAR and REMOVE_FROM_RADAR always succeed
    return false;
  }
  
  
	@Override
	public void onRadarSelected(final View v, final Event e) {
	    final ImageView radarButton = (ImageView) v.findViewById(R.id.add_to_radar_image);
	
        if (e.isOnRadar() && commonController.removeFromRadar(e)) {
          Log.v("EventDetailsActivity", "Removing event from radar");
          radarButton.setSelected(false);

          ServerDeleteRequest req = new ServerDeleteRequest(
              ServerThread.TABBIE_SERVER + "/mobile/radar/" + e.id
                  + ".json?auth_token=" + token, MessageType.ADD_TO_RADAR);

          serverThread.sendRequest(req);
        } else if (!e.isOnRadar()) {
          Log.v("EventDetailsActivity", "Adding event to radar");
          if (commonController.addToRadar(e)) {
            radarButton.setSelected(true);

            ServerPostRequest req = new ServerPostRequest(
                ServerThread.TABBIE_SERVER + "/mobile/radar/" + e.id + ".json",
                MessageType.ADD_TO_RADAR);
            req.params.put("auth_token", token);
            serverThread.sendRequest(req);
          } else {
            Toast.makeText(EventDetailsActivity.this,
                "Failed to add event to your shortlist!", Toast.LENGTH_SHORT).show();
            return;
          }
        }
	}

@Override
public void onClick(View v) {
	switch(v.getId()) {
	case R.id.location_image:
		final Event e = (Event) v.getTag();
		final Intent intent = new Intent(this, RadarMapActivity.class);
		intent.putExtra("controller", commonController);
		intent.putExtra("event", e);
		intent.putExtra("token", token);
		startActivity(intent);
		break;
	}
}
}