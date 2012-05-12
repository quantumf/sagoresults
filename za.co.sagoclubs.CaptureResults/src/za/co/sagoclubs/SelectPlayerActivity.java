package za.co.sagoclubs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SelectPlayerActivity extends Activity {

	private ListView lsvSelectPlayer;
	private CheckBox chkFavourites;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_player);
        
        lsvSelectPlayer = (ListView)findViewById(R.id.lsvSelectPlayer);
        chkFavourites = (CheckBox)findViewById(R.id.chkFavourites);
        
		SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
		boolean showFavourites = preferences.getBoolean("show_favourites", false);
		chkFavourites.setChecked(showFavourites);
		chooseWhatToShow(showFavourites);
        
        lsvSelectPlayer.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Player player = (Player)lsvSelectPlayer.getItemAtPosition(position);
				Result.setLogFile(player);
				Intent myIntent = new Intent(view.getContext(), LogFileActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});

        chkFavourites.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				chooseWhatToShow(isChecked);
				SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
				Editor editor = preferences.edit();
				editor.putBoolean("show_favourites", isChecked);
				editor.commit();
			}
        });
        
    }

    private void chooseWhatToShow(boolean showFavourites) {
		if (showFavourites) {
			showFavourites();
		} else {
			showAll();
		}
    }

    private void showAll() {
        lsvSelectPlayer = (ListView)findViewById(R.id.lsvSelectPlayer);
        PlayerArrayAdapter adapter = new PlayerArrayAdapter(this, R.layout.list_item, InternetActions.getPlayerArray());        
        lsvSelectPlayer.setAdapter(adapter);
    }
    
    private void showFavourites() {
        lsvSelectPlayer = (ListView)findViewById(R.id.lsvSelectPlayer);
		SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
        PlayerArrayAdapter adapter = new PlayerArrayAdapter(this, R.layout.list_item, InternetActions.getFavouritePlayers(preferences));        
        lsvSelectPlayer.setAdapter(adapter);
    }
    
}
