package com.persistent.sourabh_mandal.indoormapping_2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class IndoorMapsActivity extends FragmentActivity {


    private static final float HUE_IABLUE = 200.0f;
    private static final String TAG = "Indoor Maps 2";

    /* used to decide when bitmap should be downscaled */
    private static final int MAX_DIMENSION = 2048;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mMarker;
    private IARegion mOverlayFloorPlan = null;
    private GroundOverlay mGroundOverlay = null;
    private IALocationManager mIALocationManager;
    private IAResourceManager mResourceManager;
    private IATask<IAFloorPlan> mFetchFloorPlanTask;
    private Target mLoadTarget;
    private boolean mCameraPositionNeedsUpdating = true; // update on first location

    //Map Objects
    static final LatLng Stall_1 = new LatLng(12.921, 77.681);
    static final LatLng Stall_2 = new LatLng(12.923, 77.683);
    static final LatLng Stall_3 = new LatLng(12.925, 77.685);
    static final LatLng Stall_4 = new LatLng(12.927, 77.687);

    //Remove these static variables later as the owner can add them manually

    List<LatLng> StallList = new ArrayList<LatLng>();




    /**
     * Listener that handles location change events.
     */
    private IALocationListener mListener = new IALocationListenerSupport() {

        /**
         * Location changed, move marker and camera position.
         */
        @Override
        public void onLocationChanged(IALocation location) {

            Log.d(TAG, "New location coordinates: " + location.getLatitude() + "," + location.getLongitude());

            if (mMap == null)
            {
                // location received before map is initialized, ignoring update here
                // it was throwing error
                return;
            }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMarker == null)
            {
                // first location, add marker
                // on map overlay
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(HUE_IABLUE)));
            } else {
                // move existing markers position to received location
                // TRACKING
                mMarker.setPosition(latLng);
                //Show The coordinates
                Toast.makeText(IndoorMapsActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();
            }

            // our camera position needs updating if location has significantly changed
            // TRACKING

            if (mCameraPositionNeedsUpdating) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f));
                mCameraPositionNeedsUpdating = false;
            }
        }
    };

    private IARegion.Listener mRegionListener = new IARegion.Listener() {

        @Override
        public void onEnterRegion(IARegion region) {
            if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                final String newId = "429a24e9-8a95-4cef-a436-073b1fab3201";//region.getId();

                //If there is a floor plan avaialable, display if camera zooms out, remove overlay

                if (mGroundOverlay == null || !region.equals(mOverlayFloorPlan)) {
                    mCameraPositionNeedsUpdating = true;

                    // removing floor plan, need to move camera

                    if (mGroundOverlay != null) {
                        mGroundOverlay.remove();
                        mGroundOverlay = null;
                    }
                    mOverlayFloorPlan = region;

                    // Setting up the overlay from IndoorAtlas API using floor plan id
                    fetchFloorPlan(newId);
                    //Remove this later and add functionality to input from user

//                    StallList.add(Stall_1);
//                    StallList.add(Stall_2);
//                    StallList.add(Stall_3);
//                    StallList.add(Stall_4);


                    if (mMap!=null)
                    {
//                        List<Marker> markerList = new ArrayList<Marker>();
//                        int counter = 0;
//                        for(LatLng l : StallList)
//                        {
//                            counter++;
//                            markerList.add(mMap.addMarker(new MarkerOptions().position(l).title("STALL "+ counter).icon(BitmapDescriptorFactory.fromResource(R.raw.stall))));
//                        }


                        Marker marker1 = mMap.addMarker(new MarkerOptions().position(Stall_1).title("STALL 1").icon(BitmapDescriptorFactory.fromResource(R.raw.stall)));
                        Marker marker2 = mMap.addMarker(new MarkerOptions().position(Stall_2).title("Stall 2").icon(BitmapDescriptorFactory.fromResource(R.raw.stall)));
                        Marker marker3 = mMap.addMarker(new MarkerOptions().position(Stall_3).title("STALL 3").icon(BitmapDescriptorFactory.fromResource(R.raw.stall)));
                        Marker marker4 = mMap.addMarker(new MarkerOptions().position(Stall_4).title("Stall 4").icon(BitmapDescriptorFactory.fromResource(R.raw.stall)));



                    }
                } else {
                    mGroundOverlay.setTransparency(0.0f);
                }
                Toast.makeText(IndoorMapsActivity.this, newId, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onExitRegion(IARegion region) {
            if (mGroundOverlay != null) {
                // Indicate we left this floor plan but leave it there for reference
                // If we enter another floor plan, this one will be removed and another one loaded
                mGroundOverlay.setTransparency(0.5f);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_maps);

        // stop screen going to sleep while app is on foreground

        findViewById(android.R.id.content).setKeepScreenOn(true);
        mIALocationManager = IALocationManager.create(this);
        mResourceManager = IAResourceManager.create(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIALocationManager.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap == null) {

            // SupportMapFragment as the map fragment provider.

            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    mMap = googleMap;
                }
            });
        }

        // requesting location updates & monitor region changes

        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister location & region changes
        mIALocationManager.removeLocationUpdates(mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    //^HouseKeeping

    private void setupGroundOverlay(IAFloorPlan floorPlan, Bitmap bitmap) {

        if (mGroundOverlay != null) {
            mGroundOverlay.remove();
        }

        if (mMap != null) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            IALatLng iaLatLng = floorPlan.getCenter();
            LatLng center = new LatLng(iaLatLng.latitude, iaLatLng.longitude);
            GroundOverlayOptions fpOverlay = new GroundOverlayOptions()
                    .image(bitmapDescriptor)
                    .position(center, floorPlan.getWidthMeters(), floorPlan.getHeightMeters())
                    .bearing(floorPlan.getBearing());

            mGroundOverlay = mMap.addGroundOverlay(fpOverlay);
        }
    }

    private void fetchFloorPlanBitmap(final IAFloorPlan floorPlan) {

        final String url = floorPlan.getUrl();

        if (mLoadTarget == null) {
            mLoadTarget = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d(TAG, "onBitmap loaded with dimensions: " + bitmap.getWidth() + "x"
                            + bitmap.getHeight());
                    setupGroundOverlay(floorPlan, bitmap);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    // N/A
                }

                @Override
                public void onBitmapFailed(Drawable placeHolderDraweble) {
                    Toast.makeText(IndoorMapsActivity.this, "Failed to load bitmap",Toast.LENGTH_SHORT).show();
                    mOverlayFloorPlan = null;
                }
            };
        }
        RequestCreator request = Picasso.with(this).load(url);

        final int bitmapWidth = floorPlan.getBitmapWidth();
        final int bitmapHeight = floorPlan.getBitmapHeight();

        if (bitmapHeight > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        } else if (bitmapWidth > MAX_DIMENSION) {
            request.resize(MAX_DIMENSION, 0);
        }

        request.into(mLoadTarget);
    }

    private void fetchFloorPlan(String id) {

        // if there is already running task, cancel it
        cancelPendingNetworkCalls();
        id = "429a24e9-8a95-4cef-a436-073b1fab3201";
        //Floor Plan ID
        final IATask<IAFloorPlan> task = mResourceManager.fetchFloorPlanWithId(id);

        task.setCallback(new IAResultCallback<IAFloorPlan>() {

            @Override
            public void onResult(IAResult<IAFloorPlan> result) {

                if (result.isSuccess() && result.getResult() != null) {

                    // bitmap for this floor plan

                    fetchFloorPlanBitmap(result.getResult());
                } else {
                    if (!task.isCancelled()) {
                        Toast.makeText(IndoorMapsActivity.this,"loading floor plan failed: " + result.getError(), Toast.LENGTH_LONG).show();
                        mOverlayFloorPlan = null;
                    }
                }
            }
        }, Looper.getMainLooper());
        // reference to task, if required can be canceled.
        mFetchFloorPlanTask = task;

    }

    private void cancelPendingNetworkCalls() {
        if (mFetchFloorPlanTask != null && !mFetchFloorPlanTask.isCancelled()) {
            mFetchFloorPlanTask.cancel();
        }
    }
}