package org.kodejava.android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "jdbc:mysql://192.168.0.107:3306/kodejava";
    private static final String USER = "kodejava";
    private static final String PASSWORD = "kodejava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InfoAsyncTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            Map<String, String> info = new HashMap<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT name, address, phone_number FROM school_info LIMIT 1";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    info.put("name", resultSet.getString("name"));
                    info.put("address", resultSet.getString("address"));
                    info.put("phone_number", resultSet.getString("phone_number"));
                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }

            return info;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (!result.isEmpty()) {
                TextView textViewName = findViewById(R.id.textViewName);
                TextView textViewAddress = findViewById(R.id.textViewAddress);
                TextView textViewPhoneNumber = findViewById(R.id.textViewPhone);

                textViewName.setText(result.get("name"));
                textViewAddress.setText(result.get("address"));
                textViewPhoneNumber.setText(result.get("phone_number"));
            }
        }
    }
}
