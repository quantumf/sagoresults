package za.co.sagoclubs;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SectionIndexer;

public class SelectWhitePlayerActivity extends Activity {

	private ListView lsvSelectWhitePlayer;
	private CheckBox chkFavourites;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_white_player);

        lsvSelectWhitePlayer = (ListView)findViewById(R.id.lsvSelectWhitePlayer);
        chkFavourites = (CheckBox)findViewById(R.id.chkFavourites);
        
		SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
		boolean showFavourites = preferences.getBoolean("show_favourites", false);
		chkFavourites.setChecked(showFavourites);
		chooseWhatToShow(showFavourites);
        
        lsvSelectWhitePlayer.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Player player = (Player)lsvSelectWhitePlayer.getItemAtPosition(position);
				Result.setWhite(player);
                Intent myIntent = new Intent(view.getContext(), SelectBlackPlayerActivity.class);
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
        lsvSelectWhitePlayer = (ListView)findViewById(R.id.lsvSelectWhitePlayer);
        PlayerArrayAdapter adapter = new PlayerArrayAdapter(this, R.layout.list_item, InternetActions.getPlayerArray());        
        lsvSelectWhitePlayer.setAdapter(adapter);
        lsvSelectWhitePlayer.setFastScrollEnabled(true);
    }
    
    private void showFavourites() {
        lsvSelectWhitePlayer = (ListView)findViewById(R.id.lsvSelectWhitePlayer);
		SharedPreferences preferences = getSharedPreferences("SETTINGS", 0);
        PlayerArrayAdapter adapter = new PlayerArrayAdapter(this, R.layout.list_item, InternetActions.getFavouritePlayers(preferences));        
        lsvSelectWhitePlayer.setAdapter(adapter);
        lsvSelectWhitePlayer.setFastScrollEnabled(true);
    }
    
}

