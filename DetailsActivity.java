package edu.usc.weatherapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.Intent;

import android.util.Log;
import android.view.MenuItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {

    TextView detailsheading;
    String jsonVal;
    JSONObject json=null;
    String deg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent det=getIntent();
        detailsheading=(TextView) findViewById(R.id.detailssummary);
        detailsheading.setTextColor(Color.BLACK);
        detailsheading.setText("More Details for " + det.getStringExtra("CityVal") + ", " + det.getStringExtra("StateVal"));
        final Button exp = new Button(this);

        jsonVal=det.getStringExtra("JsonVal");
        if(det.getStringExtra("DegVal").matches("Fahrenheit"))
        {
            deg="°F";
        }
        else
        {
            deg="°C";
        }
        try {
            json=new JSONObject(jsonVal);
            final TableLayout tableHours = (TableLayout) findViewById(R.id.table_hours);
            TableRow tbrow0 = new TableRow(this);
            tbrow0.setBackgroundColor(Color.parseColor("#cce7ff"));
            TextView tv0 = new TextView(this);
            tv0.setText(" Time ");
            tv0.setGravity(Gravity.TOP);
            tv0.setTextSize(20);
            tv0.setWidth(320);
            tv0.setHeight(120);
            tv0.setTextColor(Color.BLACK);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setText(" Summary ");
            tv1.setTextSize(20);
            tv1.setGravity(Gravity.TOP);
            tv1.setWidth(320);
            tv1.setHeight(120);
            tv1.setTextColor(Color.BLACK);
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText(" Temp ("+deg+")");
            tv2.setTextSize(20);
            tv2.setGravity(Gravity.TOP|Gravity.RIGHT);
            tv2.setWidth(320);
            tv2.setHeight(120);
            tv2.setTextColor(Color.BLACK);
            tbrow0.addView(tv2);
            tableHours.addView(tbrow0);

            for (int i = 1; i < 26; i++) {
                if(i==25)
                {
                    TableRow bl1=new TableRow(this);
                    TextView v1=new TextView(this);
                    v1.setText(" ");
                    v1.setWidth(320);
                    v1.setHeight(120);
                    exp.setText("+");
                    exp.setHeight(100);
                    exp.setWidth(40);
                    Log.d("test", String.valueOf(Math.round(exp.getTextSize())));
                    exp.setGravity(Gravity.CENTER);
                    exp.setBackgroundColor(Color.parseColor("#99cfff"));

                    TextView v2=new TextView(this);
                    v2.setText(" ");
                    v2.setWidth(320);
                    v2.setHeight(120);
                    bl1.addView(v1);
                    bl1.addView(exp);
                    bl1.addView(v2);
                    bl1.setBackgroundColor(Color.parseColor("#cccccc"));
                    tableHours.addView(bl1);
                }
                if (i < 25) {
                    TableRow blank = new TableRow(this);
                    TextView b1 = new TextView(this);
                    b1.setText("");
                    b1.setHeight(30);
                    TextView b2 = new TextView(this);
                    b2.setText("");
                    b2.setHeight(30);
                    TextView b3 = new TextView(this);
                    b3.setText("");
                    b3.setHeight(30);
                    blank.addView(b1);
                    blank.addView(b2);
                    blank.addView(b3);

                    tableHours.addView(blank);
                    blank.getLayoutParams().height = 50;
                    blank.getLayoutParams().width = 960;
                    TableRow tbrow = new TableRow(this);
                    if (i % 2 == 1) {
                        tbrow.setBackgroundColor(Color.parseColor("#cccccc"));
                    }
                    TextView t1v = new TextView(this);
                    t1v.setText(getTime(json.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getDouble("time")));
                    t1v.setTextColor(Color.BLACK);
                    t1v.setHeight(150);
                    t1v.setTextSize(20);
                    t1v.setPadding(20, 0, 0, 0);
                    tbrow.addView(t1v);
                    ImageView t2v = setImage(json.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getString("icon"));
                    t2v.setPadding(0, 0, 60, 0);
                    tbrow.addView(t2v);
                    t2v.getLayoutParams().width = 200;
                    t2v.getLayoutParams().height = 130;
                    TextView t3v = new TextView(this);
                    t3v.setText(Math.round(json.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getDouble("temperature"))+"");
                    t3v.setHeight(150);
                    t3v.setTextColor(Color.BLACK);
                    t3v.setTextSize(20);
                    t3v.setGravity(Gravity.RIGHT);
                    t3v.setPadding(0,0,30,0);
                    tbrow.addView(t3v);
                    tableHours.addView(tbrow);
                }
            }
            exp.setOnClickListener(new View.OnClickListener() {
               public void onClick(View view) {
                    try {
                        View x=(View)exp.getParent();
                        tableHours.removeView(x);
                        for(int i=25;i<json.getJSONObject("hourly").getJSONArray("data").length();i++) {
                            TableRow blank = new TableRow(DetailsActivity.this);
                            TextView b1 = new TextView(DetailsActivity.this);
                            b1.setText("");
                            b1.setHeight(30);
                            TextView b2 = new TextView(DetailsActivity.this);
                            b2.setText("");
                            b2.setHeight(30);
                            TextView b3 = new TextView(DetailsActivity.this);
                            b3.setText("");
                            b3.setHeight(30);
                            blank.addView(b1);
                            blank.addView(b2);
                            blank.addView(b3);

                            tableHours.addView(blank);
                            blank.getLayoutParams().height = 50;
                            blank.getLayoutParams().width = 960;
                            TableRow tbrow = new TableRow(DetailsActivity.this);
                            if (i % 2 == 1) {
                                tbrow.setBackgroundColor(Color.parseColor("#cccccc"));
                            }
                            TextView t1v = new TextView(DetailsActivity.this);
                            t1v.setText(getTime(json.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getDouble("time")));
                            t1v.setTextColor(Color.BLACK);
                            t1v.setHeight(150);
                            t1v.setTextSize(20);
                            t1v.setPadding(20, 0, 0, 0);
                            tbrow.addView(t1v);
                            ImageView t2v = setImage(json.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getString("icon"));
                            t2v.setPadding(0, 0, 60, 0);
                            tbrow.addView(t2v);
                            t2v.getLayoutParams().width = 200;
                            t2v.getLayoutParams().height = 130;
                            TextView t3v = new TextView(DetailsActivity.this);
                            t3v.setText(Math.round(json.getJSONObject("hourly").getJSONArray("data").getJSONObject(i).getDouble("temperature"))+"");
                            t3v.setHeight(150);
                            t3v.setTextColor(Color.BLACK);
                            t3v.setTextSize(20);
                            t3v.setGravity(Gravity.RIGHT);
                            t3v.setPadding(0,0,30,0);
                            tbrow.addView(t3v);
                            tableHours.addView(tbrow);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                   }
               }
            });

            TableLayout tableDays = (TableLayout) findViewById(R.id.daysTable);


            TableRow trow0 = new TableRow(this);
            trow0.setBackgroundColor(Color.parseColor("#ffe5cc"));
            long timestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getDouble("time")*1000);
            Date ts=new Date(timestamp);

            String day = (new SimpleDateFormat("EEEE")).format(ts); // "Tuesday"
            String monthday=(new SimpleDateFormat("MMM dd")).format(ts);
            TextView t0 = new TextView(this);
            String day1first=day+", "+monthday+System.getProperty("line.separator")+System.getProperty("line.separator")+"Min: "+ Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getDouble("temperatureMin"))+deg+" | Max: "+Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getDouble("temperatureMax"))+deg;
            SpannableString spannablecontent1=new SpannableString(day1first);
            spannablecontent1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    0, 16, 0);

            t0.setText(spannablecontent1);
            t0.setTextSize(20);
            t0.setHeight(300);
            t0.setWidth(800);
            t0.setTextColor(Color.BLACK);
            trow0.addView(t0);

            ImageView iv0=setImage(json.getJSONObject("daily").getJSONArray("data").getJSONObject(1).getString("icon"));
            trow0.addView(iv0);
            iv0.getLayoutParams().width = 200;
            iv0.getLayoutParams().height = 130;
            //iv0.setPadding(100,0,0,0);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv0.getLayoutParams();
            params.gravity=Gravity.RIGHT;
            iv0.setLayoutParams(params);

            tableDays.addView(trow0);
            TableRow blank1 = new TableRow(this);
            TextView b1 = new TextView(this);
            b1.setText("");
            b1.setHeight(30);
            blank1.addView(b1);
            tableDays.addView(blank1);


            TableRow tbrow1 = new TableRow(this);
            tbrow1.setBackgroundColor(Color.parseColor("#e5ffff"));
            timestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getDouble("time")*1000);
            ts=new Date(timestamp);

            day = (new SimpleDateFormat("EEEE")).format(ts); // "Tuesday"
            monthday=(new SimpleDateFormat("MMM dd")).format(ts);
            TextView t1 = new TextView(this);
            String day2first=day+", "+monthday+System.getProperty("line.separator")+System.getProperty("line.separator")+"Min: "+ Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getDouble("temperatureMin"))+deg+" | Max: "+Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getDouble("temperatureMax"))+deg;
            SpannableString spannablecontent2=new SpannableString(day2first);
            spannablecontent2.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    0, 16, 0);

            t1.setText(spannablecontent2);
            t1.setTextSize(20);
            t1.setHeight(300);
            t1.setWidth(600);
            t1.setTextColor(Color.BLACK);
            tbrow1.addView(t1);

            iv0=setImage(json.getJSONObject("daily").getJSONArray("data").getJSONObject(2).getString("icon"));
            tbrow1.addView(iv0);
            iv0.getLayoutParams().width = 200;
            iv0.getLayoutParams().height = 130;
            iv0.setPadding(0,0,30,0);

            tableDays.addView(tbrow1);

            TableRow blank2 = new TableRow(this);
            TextView b2 = new TextView(this);
            b2.setText("");
            b2.setHeight(30);
            blank2.addView(b2);
            tableDays.addView(blank2);


            TableRow tbrow2 = new TableRow(this);
            tbrow2.setBackgroundColor(Color.parseColor("#ffe5ff"));
            timestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getDouble("time")*1000);
            ts=new Date(timestamp);

            day = (new SimpleDateFormat("EEEE")).format(ts); // "Tuesday"
            monthday=(new SimpleDateFormat("MMM dd")).format(ts);
            TextView t2 = new TextView(this);
            String day3first=day+", "+monthday+System.getProperty("line.separator")+System.getProperty("line.separator")+"Min: "+ Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getDouble("temperatureMin"))+deg+" | Max: "+Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getDouble("temperatureMax"))+deg;
            SpannableString spannablecontent3=new SpannableString(day3first);
            spannablecontent3.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    0, 16, 0);

            t2.setText(spannablecontent3);
            t2.setTextSize(20);
            t2.setHeight(300);
            t2.setWidth(600);
            t2.setTextColor(Color.BLACK);
            tbrow2.addView(t2);

            iv0=setImage(json.getJSONObject("daily").getJSONArray("data").getJSONObject(3).getString("icon"));
            tbrow2.addView(iv0);
            iv0.getLayoutParams().width = 200;
            iv0.getLayoutParams().height = 130;
            iv0.setPadding(0,0,30,0);

            tableDays.addView(tbrow2);

            TableRow blank3 = new TableRow(this);
            TextView b3 = new TextView(this);
            b3.setText("");
            b3.setHeight(30);
            blank3.addView(b3);
            tableDays.addView(blank3);


            TableRow tbrow3 = new TableRow(this);
            tbrow3.setBackgroundColor(Color.parseColor("#eeffe5"));
            timestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getDouble("time")*1000);
            ts=new Date(timestamp);

            day = (new SimpleDateFormat("EEEE")).format(ts); // "Tuesday"
            monthday=(new SimpleDateFormat("MMM dd")).format(ts);
            TextView tv3 = new TextView(this);
            String day4first=day+", "+monthday+System.getProperty("line.separator")+System.getProperty("line.separator")+"Min: "+ Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getDouble("temperatureMin"))+deg+" | Max: "+Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getDouble("temperatureMax"))+deg;
            SpannableString spannablecontent4=new SpannableString(day4first);
            spannablecontent4.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    0, 16, 0);

            tv3.setText(spannablecontent4);
            tv3.setTextSize(20);
            tv3.setHeight(300);
            tv3.setWidth(600);
            tv3.setTextColor(Color.BLACK);
            tbrow3.addView(tv3);

            iv0=setImage(json.getJSONObject("daily").getJSONArray("data").getJSONObject(4).getString("icon"));
            tbrow3.addView(iv0);
            iv0.getLayoutParams().width = 200;
            iv0.getLayoutParams().height = 130;
            iv0.setPadding(0,0,30,0);

            tableDays.addView(tbrow3);

            TableRow blank4 = new TableRow(this);
            TextView b4 = new TextView(this);
            b4.setText("");
            b4.setHeight(30);
            blank4.addView(b4);
            tableDays.addView(blank4);


            TableRow tbrow4 = new TableRow(this);
            tbrow4.setBackgroundColor(Color.parseColor("#ffe5ee"));
            timestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getDouble("time")*1000);
            ts=new Date(timestamp);

            day = (new SimpleDateFormat("EEEE")).format(ts); // "Tuesday"
            monthday=(new SimpleDateFormat("MMM dd")).format(ts);
            TextView tv4 = new TextView(this);
            String day5first=day+", "+monthday+System.getProperty("line.separator")+System.getProperty("line.separator")+"Min: "+ Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getDouble("temperatureMin"))+deg+" | Max: "+Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getDouble("temperatureMax"))+deg;
            SpannableString spannablecontent5=new SpannableString(day5first);
            spannablecontent5.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    0, 16, 0);

            tv4.setText(spannablecontent5);
            tv4.setTextSize(20);
            tv4.setHeight(300);
            tv4.setWidth(600);
            tv4.setTextColor(Color.BLACK);
            tbrow4.addView(tv4);

            iv0=setImage(json.getJSONObject("daily").getJSONArray("data").getJSONObject(5).getString("icon"));
            tbrow4.addView(iv0);
            iv0.getLayoutParams().width = 200;
            iv0.getLayoutParams().height = 130;
            iv0.setPadding(0,0,30,0);

            tableDays.addView(tbrow4);

            TableRow blank5 = new TableRow(this);
            TextView b5 = new TextView(this);
            b5.setText("");
            b5.setHeight(30);
            blank5.addView(b5);
            tableDays.addView(blank5);


            TableRow tbrow5 = new TableRow(this);
            tbrow5.setBackgroundColor(Color.parseColor("#ffffe5"));
            timestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(6).getDouble("time")*1000);
            ts=new Date(timestamp);

            day = (new SimpleDateFormat("EEEE")).format(ts); // "Tuesday"
            monthday=(new SimpleDateFormat("MMM dd")).format(ts);
            TextView tv5 = new TextView(this);
            String day6first=day+", "+monthday+System.getProperty("line.separator")+System.getProperty("line.separator")+"Min: "+ Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getDouble("temperatureMin"))+deg+" | Max: "+Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getDouble("temperatureMax"))+deg;
            SpannableString spannablecontent6=new SpannableString(day6first);
            spannablecontent6.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    0, 17, 0);

            tv5.setText(spannablecontent6);
            tv5.setTextSize(20);
            tv5.setHeight(300);
            tv5.setWidth(600);
            tv5.setTextColor(Color.BLACK);
            tbrow5.addView(tv5);

            iv0=setImage(json.getJSONObject("daily").getJSONArray("data").getJSONObject(6).getString("icon"));
            tbrow5.addView(iv0);
            iv0.getLayoutParams().width = 200;
            iv0.getLayoutParams().height = 130;
            iv0.setPadding(0,0,30,0);

            tableDays.addView(tbrow5);

            TableRow blank6 = new TableRow(this);
            TextView b6 = new TextView(this);
            b6.setText("");
            b6.setHeight(30);
            blank6.addView(b6);
            tableDays.addView(blank6);


            TableRow tbrow6 = new TableRow(this);
            tbrow6.setBackgroundColor(Color.parseColor("#e5e5ff"));
            timestamp=Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getDouble("time")*1000);
            ts=new Date(timestamp);

            day = (new SimpleDateFormat("EEEE")).format(ts); // "Tuesday"
            monthday=(new SimpleDateFormat("MMM dd")).format(ts);
            TextView tv6 = new TextView(this);
            String day7first=day+", "+monthday+System.getProperty("line.separator")+System.getProperty("line.separator")+"Min: "+ Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getDouble("temperatureMin"))+deg+" | Max: "+Math.round(json.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getDouble("temperatureMax"))+deg;
            SpannableString spannablecontent7=new SpannableString(day7first);
            spannablecontent7.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    0, 16, 0);

            tv6.setText(spannablecontent7);
            tv6.setTextSize(20);
            tv6.setHeight(300);
            tv6.setWidth(600);
            tv6.setTextColor(Color.BLACK);
            tbrow6.addView(tv6);

            iv0=setImage(json.getJSONObject("daily").getJSONArray("data").getJSONObject(7).getString("icon"));
            tbrow6.addView(iv0);
            iv0.getLayoutParams().width = 200;
            iv0.getLayoutParams().height = 130;
            iv0.setPadding(0,0,30,0);

            tableDays.addView(tbrow6);

            LinearLayout firstLayout = (LinearLayout) findViewById(R.id.LinearLayoutFirst);
            firstLayout.setVisibility(LinearLayout.VISIBLE);
            LinearLayout secondLayout = (LinearLayout) findViewById(R.id.LinearLayoutSecond);
            secondLayout.setVisibility(LinearLayout.GONE);

            Button btnhours = (Button)findViewById(R.id.hoursButton);
            LinearLayout first = (LinearLayout) findViewById(R.id.LinearLayoutFirst);
            first.setVisibility(LinearLayout.VISIBLE);
            Button hours=(Button) findViewById(R.id.hoursButton);
            hours.setBackground(getResources().getDrawable(R.drawable.buttons));
            hours.setHeight(120);
            btnhours.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LinearLayout firstLayout = (LinearLayout) findViewById(R.id.LinearLayoutFirst);
                    firstLayout.setVisibility(LinearLayout.VISIBLE);
                    Button hours=(Button) findViewById(R.id.hoursButton);
                    hours.setBackground(getResources().getDrawable(R.drawable.buttons));
                    hours.setHeight(120);

                    LinearLayout secondLayout = (LinearLayout) findViewById(R.id.LinearLayoutSecond);
                    secondLayout.setVisibility(LinearLayout.GONE);
                    Button days=(Button) findViewById(R.id.daysButton);
                    days.setBackground(getResources().getDrawable(R.drawable.normalbuttons));
                    days.setHeight(120);

                }
            });

            Button btnDays = (Button)findViewById(R.id.daysButton);
            btnDays.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LinearLayout firstLayout = (LinearLayout) findViewById(R.id.LinearLayoutFirst);
                    firstLayout.setVisibility(LinearLayout.GONE);
                    Button hours=(Button) findViewById(R.id.hoursButton);
                    hours.setBackground(getResources().getDrawable(R.drawable.normalbuttons));
                    hours.setHeight(120);

                    LinearLayout secondLayout=(LinearLayout) findViewById(R.id.LinearLayoutSecond);
                    secondLayout.setVisibility(LinearLayout.VISIBLE);
                    Button days=(Button) findViewById(R.id.daysButton);
                    days.setBackground(getResources().getDrawable( R.drawable.buttons));
                    days.setHeight(120);



                }
            });




    } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String getTime(Double timeStamp)
    {
        long time=Math.round(timeStamp*1000);
        Date timeSt = new Date(time);
        DateFormat timeFor = new SimpleDateFormat("hh:mm a");
        try {
            timeFor.setTimeZone(TimeZone.getTimeZone(json.getString("timezone")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timeFor.format(timeSt).replace("am","AM").replace("pm", "PM");
    }

    public ImageView setImage(String icon)
    {
        ImageView i=new ImageView(this);
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
        return i;
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu., menu);
       // return true;
    //}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
