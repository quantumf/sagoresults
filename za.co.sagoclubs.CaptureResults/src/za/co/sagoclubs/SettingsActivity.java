package za.co.sagoclubs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import static za.co.sagoclubs.Constants.TAG;

public class SettingsActivity extends Activity {
	
	private TextView txtUsername;
	private TextView txtPassword;
	private Button btnSelectFavouritePlayers;
	private boolean changed;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        txtUsername = (TextView)findViewById(R.id.txtUsername);
        txtPassword = (TextView)findViewById(R.id.txtPassword);

        loadSettings();
        
        btnSelectFavouritePlayers = (Button) findViewById(R.id.btnSelectFavouritePlayers);
		btnSelectFavouritePlayers.setVisibility(txtUsername.getText().toString().equals("guest")?View.INVISIBLE:View.VISIBLE);
		btnSelectFavouritePlayers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SelectFavouritePlayersActivity.class);
                startActivityForResult(myIntent, 0);
			}
		});
		
		txtUsername.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "SettingsActivity.txtUsername.OnClick");
				changed = true;
			}
		});

		changed = false;
		
	}

	@Override
	public void onPause() {
		saveSettings();
		super.onPause();
	}
	
	private void saveSettings() {
		if (changed) {
			SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
			Editor editor = preferences.edit();
			editor.putString("username", txtUsername.getText().toString().trim());
			editor.putString("password", txtPassword.getText().toString().trim());
			editor.commit();
			InternetActions.setUsername(preferences.getString("username", "guest"));
			InternetActions.setPassword(preferences.getString("password", "guest"));
		}
	}
	
	private void loadSettings() {
		SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
		txtUsername.setText(preferences.getString("username", "guest"));
		txtPassword.setText(preferences.getString("password", "guest"));
	}
	
}
