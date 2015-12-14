package edu.usc.weatherapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.share.Sharer;

import com.facebook.CallbackManager;
import com.facebook.FacebookDialog;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONException;

import org.json.JSONObject;

import java.util.TimeZone;

import com.facebook.share.widget.ShareDialog;
import com.facebook.FacebookCallback;
import android.net.Uri;

public class ResultActivity extends AppCompatActivity {

    Button btnDetails;
    Button btnMaps;
    String city;
    String state;
    String degree;
    JSONObject json;
    String deg;
    int tempint;
    String imageu;
    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)   {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent b = getIntent();
        city=b.getStringExtra("CityVal");
        state=b.getStringExtra("StateVal");
        degree=b.getStringExtra("DegVal");
        if(degree.matches("Fahrenheit"))
        {
            deg="°F";
        }
        else
        {
            deg="°C";
        }

        String jsonVal=b.getStringExtra("JsonVal");
        try {
            json = new JSONObject(jsonVal);
            String lat=json.getString("latitude");
            final String summary=json.getJSONObject("currently").getString("summary");

            String imgSrc=json.getJSONObject("currently").getString("icon");
            imageu=json.getJSONObject("currently").getString("icon");


            setImage(imgSrc);

            TextView summaryTextView=(TextView)findViewById(R.id.summary);
            summaryTextView.setText(""+json.getJSONObject("currently").getString("summary")+" in "+b.getStringExtra("CityVal")+", "+ b.getStringExtra("StateVal"));


            TextView tempTextView=(TextView)findViewById(R.id.temperature);
            Double temp=json.getJSONObject("currently").getDouble("temperature");
            tempint=(int) Math.round(temp);
            String val=String.valueOf(tempint) + (b.getStringExtra("DegVal").equals("Fahrenheit")? "\u00b0F":"\u00b0C");

            SpannableStringBuilder cs = new SpannableStringBuilder(val);
            if(tempint<10 && tempint>=0) {
                cs.setSpan(new SuperscriptSpan(), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                cs.setSpan(new RelativeSizeSpan(0.35f), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(tempint>10 || tempint<0 && tempint>-10)
            {
                cs.setSpan(new SuperscriptSpan(), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                cs.setSpan(new RelativeSizeSpan(0.35f), 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(tempint<=-10)
            {
                cs.setSpan(new SuperscriptSpan(), 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                cs.setSpan(new RelativeSizeSpan(0.35f), 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tempTextView.setText(cs);

            String min=String.valueOf(Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMin")));
            String max=String.valueOf(Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMax")));
            TextView minmaxTextView=(TextView)findViewById(R.id.minmaxtemp);
            minmaxTextView.setText("L:"+min+"° | H:"+max+"°");

            TextView precip=(TextView) findViewById(R.id.precip);
            String precipitation="N/A";
            Double precipInt=json.getJSONObject("currently").getDouble("precipIntensity");
            if(precipInt >= 0 && precipInt < 0.002)
                precipitation="None";
            else if(precipInt >= 0.002 && precipInt < 0.017)
                precipitation="Very Light";
            else if(precipInt >= 0.017 && precipInt < 0.1)
                precipitation="Light";
            else if(precipInt >= 0.1 && precipInt < 0.4)
                precipitation="Moderate";
            else if(precipInt >= 0.4)
                precipitation="Heavy";

            precip.setText(precipitation);

            TextView cor=(TextView) findViewById(R.id.cor);
            int corInt=(int) Math.round(json.getJSONObject("currently").getDouble("precipProbability") * 100);
            String chanceOfRain=String.valueOf(corInt)+" %";
            cor.setText(chanceOfRain);

            TextView windSp=(TextView) findViewById(R.id.windspeed);
            String windspeed;
            if(b.getStringExtra("DegVal").equals("Fahrenheit"))
            {
                windspeed=Math.round(json.getJSONObject("currently").getDouble("windSpeed")*100)/100+" mph";
            }
            else
            {
                windspeed=Math.round(json.getJSONObject("currently").getDouble("windSpeed")*100)/100+" m/s";
            }
            windSp.setText(windspeed);

            TextView dewP=(TextView) findViewById(R.id.dewpoint);
            String dewPoint;
            if(b.getStringExtra("DegVal").equals("Fahrenheit"))
            {
                dewPoint=Math.round(json.getJSONObject("currently").getDouble("dewPoint"))+" °F";
            }
            else
            {
                dewPoint=Math.round(json.getJSONObject("currently").getDouble("dewPoint"))+" °C";
            }
            dewP.setText(dewPoint);

            TextView hum=(TextView) findViewById(R.id.humidity);
            String humidity;
            humidity=String.valueOf((int)Math.round((json.getJSONObject("currently").getDouble("humidity") * 100)))+" %";
            hum.setText(humidity);

            TextView vis=(TextView) findViewById(R.id.visibility);
            String visibility;
            if(b.getStringExtra("DegVal").equals("Fahrenheit"))
            {
                if(json.getJSONObject("currently").getString("visibility")=="undefined")
                {
                    visibility="N/A";
                }
                else
                {
                    visibility=(json.getJSONObject("currently").getString("visibility"))+" mi";
                }

            }
            else
            {
                if(json.getJSONObject("currently").getString("visibility")=="undefined")
                {
                    visibility="N/A";
                }
                else
                {
                    visibility=(json.getJSONObject("currently").getString("visibility"))+" km";;
                }
            }
            vis.setText(visibility);

            long sunrisetimestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("sunriseTime")*1000);

            Date date = new Date(sunrisetimestamp);
            DateFormat formatter = new SimpleDateFormat("hh:mm a");
            formatter.setTimeZone(TimeZone.getTimeZone(json.getString("timezone")));
            String risedateFormatted = formatter.format(date).replace("am","AM").replace("pm", "PM");
            TextView sunr=(TextView) findViewById(R.id.sunrise);
            sunr.setText(risedateFormatted);

            long sunsettimestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("sunsetTime")*1000);

            Date sunsetDate = new Date(sunsettimestamp);
            DateFormat sunsetformatter = new SimpleDateFormat("hh:mm a");
            sunsetformatter.setTimeZone(TimeZone.getTimeZone(json.getString("timezone")));
            String setdateFormatted = sunsetformatter.format(sunsetDate).replace("am","AM").replace("pm","PM");

            TextView suns=(TextView) findViewById(R.id.sunset);
            suns.setText(setdateFormatted);

            btnDetails = (Button)findViewById(R.id.detailsButton);
            btnDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ResultActivity.this, DetailsActivity.class);
                    intent.putExtra("CityVal", city);
                    intent.putExtra("StateVal", state);
                    intent.putExtra("DegVal", degree);
                    intent.putExtra("JsonVal", json.toString());
                    ResultActivity.this.startActivity(intent);
                }
            });

            btnMaps=(Button)findViewById(R.id.mapButton);
            btnMaps.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ResultActivity.this, MapViewActivity.class);
                    try {
                        intent.putExtra("latVal",json.getString("latitude"));
                        intent.putExtra("longVal", json.getString("longitude"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //intent.putExtra("StateVal", state);
                    //intent.putExtra("DegVal", degree);
                    //intent.putExtra("JsonVal",json.toString());
                    ResultActivity.this.startActivity(intent);
                }
            });
            FacebookSdk.sdkInitialize(getApplicationContext());
            ImageView btnfb=(ImageView)findViewById(R.id.facebookLogo);
            final FacebookDialog shareDialog = new ShareDialog(this);
            btnfb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //Intent intent = new Intent(ResultActivity.this, FacebookActivity.class);
                    //intent.putExtra("CityVal",city);
                    //intent.putExtra("StateVal", state);
                    //intent.putExtra("DegVal", degree);
                    //intent.putExtra("JsonVal",json.toString());
                    // ResultActivity.this.startActivity(intent);

                    //setContentView(R.layout.activity_facebook);



                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = null;
                            linkContent = new ShareLinkContent.Builder()
                                    .setContentTitle("Current Weather in " + city + ", " + state)
                                    .setContentDescription(summary + ", " + String.valueOf(tempint)+" "+deg)
                                    .setContentUrl(Uri.parse("http://forecast.io"))
                                    .setImageUrl(getimage(imageu))
                                    .build();
                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

                            @Override
                            public void onSuccess(Sharer.Result result)
                            {
                                Log.d("Testtttt",result.toString());
                                Toast.makeText(getBaseContext(), "Facebook Post Successful",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel()
                            {
                                Toast.makeText(getApplicationContext(), "Post Cancelled",
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(FacebookException error)
                            {
                                Toast.makeText(getApplicationContext(), "Post Failed",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                            shareDialog.canShow(linkContent);
                            shareDialog.show(linkContent);

                    }
                }
            });
        }

        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setImage(String icon)
    {
        ImageView i=(ImageView)findViewById(R.id.weatherIcon);
        if(icon.equals("clear-day")) {
            i.setImageResource(R.drawable.clear);
        }else if(icon.equals("clear-night")){
            i.setImageResource(R.drawable.clear_night);}
        else if(icon.equals("rain")){
            i.setImageResource(R.drawable.rain);}
        else if(icon.equals("snow")){
            i.setImageResource(R.drawable.snow);}
        else if(icon.equals("sleet")){
            i.setImageResource(R.drawable.sleet);}
        else if(icon.equals("wind")){
            i.setImageResource(R.drawable.wind);}
        else if(icon.equals("fog")){
            i.setImageResource(R.drawable.fog);}
        else if(icon.equals("cloudy")){
            i.setImageResource(R.drawable.cloudy);}
        else if(icon.equals("partly-cloudy-day")){
            i.setImageResource(R.drawable.cloud_day);}
        else if(icon.equals("partly-cloudy-night")){
            i.setImageResource(R.drawable.cloud_night);}
    }
    public Uri getimage(String icon)
    {
        if(icon.equals("clear-day"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/clear.png");
        else if(icon.equals("clear-night"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/clear_night.png");
        else if(icon.equals("rain"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/rain.png");
        else if(icon.equals("snow"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/snow.png");
        else if(icon.equals("sleet"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/sleet.png");
        else if(icon.equals("wind"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/wind.png");
        else if(icon.equals("fog"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/fog.png");
        else if(icon.equals("cloudy"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/cloudy.png");
        else if(icon.equals("partly-cloudy-day"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/cloud_day.png");
        else if(icon.equals("partly-cloudy-night"))
            return Uri.parse("http://cs-server.usc.edu:45678/hw/hw8/images/cloud_night.png");
        else
            return null;
    }

}
