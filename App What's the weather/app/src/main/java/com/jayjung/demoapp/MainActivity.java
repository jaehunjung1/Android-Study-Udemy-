package com.jayjung.demoapp;

        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.EditText;
        import android.widget.TextView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;

        import android.view.View;


public class MainActivity extends AppCompatActivity {

    TextView resultText;
    EditText cityText;
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String apiText = "";
            URL url;

            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    apiText += (char) data;
                    data = reader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }

            return apiText;
        }

        @Override
        protected void onPostExecute(String apiText) {
            super.onPostExecute(apiText);
            JSONObject jsonObject;
            String resultString = "";

            try {
                jsonObject = new JSONObject(apiText);
                String weather = jsonObject.getString("weather");
                JSONArray weatherArr = new JSONArray(weather);

                for (int i = 0; i < weatherArr.length(); i++) {
                    resultString += weatherArr.getJSONObject(i).getString("main")+": "+
                            weatherArr.getJSONObject(i).getString("description")+"\n";
                }
                resultText.setText(resultString);
                resultText.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }

    public void printWeather(View view) {
        String cityName = cityText.getText().toString();

        DownloadTask task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=7d55fd38b559348eaa874f9102bdf981");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView)findViewById(R.id.resultText);
        cityText = (EditText)findViewById(R.id.editText);

    }
}