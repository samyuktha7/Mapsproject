package com.example.samyuktha.mapsprac;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BlankFragment extends android.app.Fragment implements OnMapReadyCallback{


    GoogleMap mapo;
    LocationManager lm;
    SearchView sh;
    String here;
    Double rating;
    private Target target;
    RecyclerView rvs;
    List<Datafromhere> doll,doll2;
    Boolean opennow;
    FloatingActionButton fab2;
    FragmentManager fm;
    android.app.FragmentTransaction ft;
    private ClusterManager<StringClusterItem> mClusterManager;
    Bitmap bitmaps;
    Datafromhere dlm;
    CustomClusterRenderer renderer;


    static class StringClusterItem implements ClusterItem {
        public String getTitle() {
            return title;
        }

        final String title;
        final LatLng latLng;
        final String vicin;
        String iconnss;
        public StringClusterItem(String name1, String title, LatLng latLng, String vicinity1) {

            this.iconnss = title;
            this.latLng = latLng;
            this.vicin=vicinity1;
            this.title=name1;
        }
        @Override public LatLng getPosition() {
            return latLng;
        }


        public String getVicin() {
            return vicin;
        }

        public String getIconnss() {
            return iconnss;
        }
    }

    public class CustomClusterRenderer extends DefaultClusterRenderer<StringClusterItem> {
        private final Context mContext;
        public CustomClusterRenderer(Context context, GoogleMap map,
                                     ClusterManager<BlankFragment.StringClusterItem> clusterManager) {
            super(context, map, clusterManager);
            mContext = context;
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<StringClusterItem> cluster) {
            return cluster.getSize()>1;
        }



        @Override protected void onBeforeClusterItemRendered(BlankFragment.StringClusterItem item,
                                                             MarkerOptions markerOptions) {

                markerOptions.title(item.getTitle());
              markerOptions.snippet(item.getVicin());
            markerOptions.position(item.getPosition());

            String sms = dlm.getIcon1();

            final Target mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    Log.d("DEBUG1", "onBitmapLoaded");
                    bitmaps = bitmap;

                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                    Log.d("DEBUG", "onBitmapFailed");
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {
                    Log.d("DEBUG", "onPrepareLoad");
                }
            };

            Picasso.with(getContext()).load(sms).into(mTarget);


            if(bitmaps==null) {


                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            else
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmaps));
//                mapo.addMarker(markerOptions);



        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        doll = new ArrayList<Datafromhere>();

        fab2=(FloatingActionButton)getActivity().findViewById(R.id.topfloatingActionButton);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putParcelableArrayList("samss", (ArrayList<? extends Parcelable>) doll);

                BlankFragment2 bf2= new BlankFragment2();
                bf2.setArguments(b);
                fm= getFragmentManager();
                ft=fm.beginTransaction();
                ft.replace(R.id.fmlay,bf2);

                ft.commit();



            }
        });
        sh= (SearchView)getActivity().findViewById(R.id.searchhere);
        rvs=(RecyclerView)getActivity().findViewById(R.id.rec);
        LinearLayoutManager lms = new LinearLayoutManager(getActivity().getBaseContext());
        lms.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvs.setLayoutManager(lms);

        sh.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                here=query;
                mapo.clear();

                String url = getUrl(here);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mapo;
                DataTransfer[1] = url;
                Getnearby getNearbyPlacesData = new Getnearby();
                getNearbyPlacesData.execute(DataTransfer);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }


    private String getUrl( String nearbyPlace) {
        StringBuilder googlePlacesUrl = new
                StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");

        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&query="+ nearbyPlace);
       // googlePlacesUrl.append("&key=" + "AIzaSyDnaTE-olVObMqhzNMJgqrp5rY7ZYpUNjU");
        googlePlacesUrl.append("&key=" + "AIzaSyCry_OxBfTOfawF3GojKc6J-gz5L6t9Ne4");
        Log.d("url1",""+googlePlacesUrl.toString());
        Log.d("url2"," "+nearbyPlace);
        return (googlePlacesUrl.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vm =  inflater.inflate(R.layout.fragment_blank, container, false);
       return vm;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mv = (MapView)view.findViewById(R.id.mapview);
        mv.onCreate(null);
        mv.onResume();
        mv.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mapo=googleMap;
        mapo.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mapo.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {


                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getActivity().getLayoutInflater().inflate(R.layout.infobubble, null);

                TextView name = (TextView) v.findViewById(R.id.text2);
                TextView lat = (TextView) v.findViewById(R.id.text4);
                TextView longs = (TextView) v.findViewById(R.id.text41);
                TextView  address = (TextView) v.findViewById(R.id.text6);

                LatLng ll =marker.getPosition();
                Double latso=ll.latitude;
                Double longso = ll.longitude;
                name.setText(marker.getTitle());
                lat.setText(latso.toString());
                longs.setText(longso.toString());
                address.setText(marker.getSnippet());

                return v;
            }
        });



        mapo.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {


                Toast.makeText(getActivity(),"checking..",Toast.LENGTH_SHORT).show();
                Log.d("jsdhegfe","hagefe");
//                Intent ins = new Intent(getActivity(), Main2Activity.class);
//                LatLng loss= marker.getPosition();
//                Double l1= loss.latitude;
//                Double l2= loss.longitude;
//                ins.putExtra("h1",marker.getTitle());
//                ins.putExtra("h2",l1.toString());
//                ins.putExtra("h3",l2.toString());
//                ins.putExtra("h4",marker.getSnippet());
//                startActivity(ins);

            }
        });



        mClusterManager = new ClusterManager<>(getActivity(), mapo);
        mapo.setOnCameraChangeListener(mClusterManager);
        mapo.setOnMarkerClickListener(mClusterManager);



        mapo.setOnMarkerClickListener(mClusterManager);
        mapo.setOnInfoWindowClickListener(mClusterManager);

         renderer = new CustomClusterRenderer(getActivity(), mapo, mClusterManager);
        mClusterManager.setRenderer(renderer);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<StringClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<StringClusterItem> cluster) {
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder.include(item.getPosition());
                }
                final LatLngBounds bounds = builder.build();
                mapo.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                return true;
            }
        });


        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<StringClusterItem>() {
            @Override
            public void onClusterItemInfoWindowClick(StringClusterItem stringClusterItem) {
                Log.d("jhbsdv"," "+stringClusterItem.getTitle());

                                Intent ins = new Intent(getActivity(), Main2Activity.class);
                LatLng loss= stringClusterItem.getPosition();
                Double l1= loss.latitude;
                Double l2= loss.longitude;
                ins.putExtra("h1",stringClusterItem.getTitle());
                ins.putExtra("h2",l1.toString());
                ins.putExtra("h3",l2.toString());
                ins.putExtra("h4",stringClusterItem.getVicin());
                ins.putExtra("h5",stringClusterItem.getIconnss());
                startActivity(ins);
            }
        });

    }



    public class Getnearby extends AsyncTask<Object,String ,String> {

        String googleplacesdata, url;
        GoogleMap mgooglemap;

        List<Address> la;
        MainActivity ma;
        @Override
        protected String doInBackground(Object... params) {


            try {
                mgooglemap = (GoogleMap) params[0];
                url = (String) params[1];
                Downloadurl downloadUrl = new Downloadurl();
                googleplacesdata = downloadUrl.readUrl(url);
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            Log.d("output", googleplacesdata);
            return googleplacesdata;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            doll2=new ArrayList<Datafromhere>();

            try {
                JSONObject reader = new JSONObject(s);
                JSONArray abc= reader.getJSONArray("results");
                Log.d("around1"," "+abc.length());
                for(int i=0;i<abc.length();i++)
                {
                    JSONObject objectinsidearray = abc.getJSONObject(i);

                    JSONObject geometryobject = objectinsidearray.getJSONObject("geometry");
                    JSONObject locationobj = geometryobject.getJSONObject("location");
                    Double latitudeval=locationobj.getDouble("lat");
                    Double Longitudeval = locationobj.getDouble("lng");
                    String icon = objectinsidearray.getString("icon");

                    String name = objectinsidearray.getString("name");

                    if(objectinsidearray.has("opening_hours")) {
                        JSONObject openobject = objectinsidearray.getJSONObject("opening_hours");
                        opennow=openobject.getBoolean("open_now");
                    }
                    else opennow=false;
                    if(objectinsidearray.has("rating")) {
                        rating = objectinsidearray.getDouble("rating");
                    }
                    else rating = 0.0;
                    String vicinity = objectinsidearray.getString("formatted_address");
                    Log.d("latitudes", " "+latitudeval);
                    doll.add(new Datafromhere(latitudeval,Longitudeval,icon,name,opennow,rating,vicinity));
                    doll2.add(new Datafromhere(latitudeval,Longitudeval,icon,name,opennow,rating,vicinity));
                    Log.d("around2"," added"+i);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



            Log.d("around"," "+doll2.size());
            for (int i = 0; i < doll2.size(); i++) {


                dlm =doll2.get(i);
                LatLng latLng = new LatLng(dlm.getLatsdata(),dlm.getLngsdata());

                mClusterManager.addItem(new StringClusterItem(dlm.getName1(), dlm.getIcon1(),latLng,dlm.getVicinity1()));

                // move map camera
                mapo.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(dlm.getLatsdata(),dlm.getLngsdata())));

            }

            mClusterManager.cluster();

            Myrecycls adap=new Myrecycls(doll,mapo);
            rvs.setAdapter(adap);

        }
    }





}
