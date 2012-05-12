package za.co.sagoclubs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btnSettings;
	private Button btnCaptureResult;
	private Button btnDisplayLogFile;
	private boolean onCreateCalled = false;

	@Override
	public void onResume() {
		super.onResume();
		if (onCreateCalled) {
			SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
			btnCaptureResult.setEnabled(!(preferences.getString("username", "").equals("")));
			btnDisplayLogFile.setEnabled(!(preferences.getString("username", "").equals("")));
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Result.setResultState(ResultState.Complete);
        
		SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
		InternetActions.setUsername(preferences.getString("username", ""));
		InternetActions.setPassword(preferences.getString("password", ""));
		
        btnSettings = (Button) findViewById(R.id.btnSettings);
		btnSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
			}
		});

        btnDisplayLogFile = (Button) findViewById(R.id.btnDisplayLogFile);
		btnDisplayLogFile.setEnabled(!(preferences.getString("username", "").equals("")));
		btnDisplayLogFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SelectPlayerActivity.class);
                startActivityForResult(myIntent, 0);
			}
		});

		btnCaptureResult = (Button) findViewById(R.id.btnCaptureResult);
		btnCaptureResult.setEnabled(!(preferences.getString("username", "").equals("")));
		btnCaptureResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Result.setResultState(ResultState.Enter);
                Intent myIntent = new Intent(v.getContext(), SelectWhitePlayerActivity.class);
                startActivityForResult(myIntent, 0);
			}
		});
		
		int width = btnCaptureResult.getWidth();
		if (btnDisplayLogFile.getWidth()>width) {
			width = btnDisplayLogFile.getWidth();
		}
		btnCaptureResult.setWidth(width);
		btnDisplayLogFile.setWidth(width);
		btnSettings.setWidth(width);
		
		onCreateCalled = true;
    }
    
}
