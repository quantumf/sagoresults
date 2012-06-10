package za.co.sagoclubs;

import java.util.Arrays;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PlayerRatingsActivity extends Activity {

	private ScrollView scrollView;
	private ProgressDialog dialog;
	private ListView listView;
	private Button btnSortByRank;
	private Button btnSortByName;
	private PlayerSortOrder preferredOrder = PlayerSortOrder.SORT_BY_RANK;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_ratings);

        btnSortByRank = (Button)findViewById(R.id.btnSortByRank);
        btnSortByName = (Button)findViewById(R.id.btnSortByName);
        scrollView = (ScrollView)findViewById(R.id.SCROLLER_ID);
        listView = (ListView)findViewById(R.id.listView);

		updateList();

    	btnSortByName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (preferredOrder==PlayerSortOrder.SORT_BY_RANK) {
					preferredOrder=PlayerSortOrder.SORT_BY_NAME;
					updateList();
				}
			}
    	});

    	btnSortByRank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (preferredOrder==PlayerSortOrder.SORT_BY_NAME) {
					preferredOrder=PlayerSortOrder.SORT_BY_RANK;
					updateList();
				}
			}
    	});
	}

	private void updateList() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("Retrieving player ratings...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
    	new PlayerRatingsTask().execute(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (dialog!=null) {
			dialog.dismiss();
		}
	}
	
	private class PlayerRatingsTask extends AsyncTask<Context, Void, Player[]> {
		private Context context;
		
		protected Player[] doInBackground(Context... v) {
			context = v[0];
			setProgressBarIndeterminateVisibility(true);
	    	Player[] players = InternetActions.getPlayerRatingsArray(PlayerSortOrder.SORT_BY_NAME);
        	return players;
	    }

		public OnItemClickListener playerItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Player p = (Player)listView.getItemAtPosition(position);
				Result.setLogFile(p);
	            Intent myIntent = new Intent(view.getContext(), LogFileActivity.class);
	            startActivityForResult(myIntent, 0);
			}
		};
		
	    protected void onPostExecute(Player[] players) {
	    	setProgressBarIndeterminateVisibility(false);
	    	dialog.hide();
	        PlayerRatingArrayAdapter adapter = new PlayerRatingArrayAdapter(context,
	                R.layout.player_rating_list_item, players);
	        listView.setAdapter(adapter);
	        listView.setOnItemClickListener(playerItemClickListener);
	        
	    }
	}
	
}
