package edu.usc.weatherapplication;

import org.json.JSONObject;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;

import java.net.URLEncoder;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;

import android.graphics.Color;
import android.net.Uri;

public class SearchPage extends AppCompatActivity {
    EditText street;
    EditText city;
    Spinner state;
    TextView error;
    Button btnSearch;
    RadioGroup degree;
    String deg;
    String stateCode;
    private static String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_search_page);

        error = (TextView) findViewById(R.id.errorText);
        Spinner spinner = (Spinner) findViewById(R.id.state_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.USStates, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        street = (EditText) findViewById(R.id.street);

        btnSearch = (Button) findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                city = (EditText) findViewById(R.id.city);
                state = (Spinner) findViewById(R.id.state_spinner);
                stateCode = getResources().getStringArray(R.array.USStateCodes)[state.getSelectedItemPosition()];
                degree = (RadioGroup) findViewById(R.id.degree);

                if (street.getText().toString().trim().matches("")) {

                    error.setText("\n\nPlease enter a Street Address.");
                    error.setTextColor(Color.parseColor("#cc0000"));
                    error.setTextSize(20);
                    return;
                } else if (city.getText().toString().trim().matches("")) {
                    error.setText("\n\nPlease enter a City.");
                    error.setTextColor(Color.parseColor("#cc0000"));
                    error.setTextSize(20);
                    return;
                } else if (stateCode.matches("None")) {
                    error.setText("\n\nPlease select a State.");
                    error.setTextColor(Color.parseColor("#cc0000"));
                    error.setTextSize(20);
                    return;
                }
                    new JSONParse().execute();
                    error.setText("");
                    street.setVisibility(View.VISIBLE);
            }

        });
        Button btnAbout = (Button) findViewById(R.id.aboutButton);
        btnAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SearchPage.this, AboutActivity.class);
                SearchPage.this.startActivity(intent);

            }
        });
        Button btnClear = (Button) findViewById(R.id.clearButton);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                city = (EditText) findViewById(R.id.city);
                state = (Spinner) findViewById(R.id.state_spinner);
                stateCode = getResources().getStringArray(R.array.USStateCodes)[state.getSelectedItemPosition()];
                RadioButton r = (RadioButton) findViewById(R.id.radio_fahrenheit);
                RadioButton rc = (RadioButton) findViewById(R.id.radio_celsius);
                street.setText("");
                city.setText("");
                state.setSelection(0);
                r.setChecked(true);
                rc.setChecked(false);
                error=(TextView) findViewById(R.id.errorText);
                error.setText("");
            }
        });

        ImageView img = (ImageView)findViewById(R.id.forecastLogo);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://forecast.io"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_page, menu);
        return true;
    }

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


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RadioButton degree_fahrenheit=(RadioButton)findViewById(R.id.radio_fahrenheit);
            if (degree_fahrenheit.isChecked())
                deg = "Fahrenheit";
            else
                deg = "Celsius";
            url="http://cs-server.usc.edu:10904/index.php/?streetaddress="+ URLEncoder.encode(street.getText().toString())+"&city="+ URLEncoder.encode(city.getText().toString())+"&state="+getResources().getStringArray(R.array.USStateCodes)[state.getSelectedItemPosition()]+"&degree="+deg;
            Log.d("Harish",url);
            pDialog = new ProgressDialog(SearchPage.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.hide();
            Intent intent = new Intent(SearchPage.this, ResultActivity.class);
            intent.putExtra("JsonVal", json.toString());
            intent.putExtra("CityVal",city.getText().toString());
            intent.putExtra("StateVal",getResources().getStringArray(R.array.USStateCodes)[state.getSelectedItemPosition()]);
            intent.putExtra("DegVal",deg);
            SearchPage.this.startActivity(intent);

        }
    }
}
