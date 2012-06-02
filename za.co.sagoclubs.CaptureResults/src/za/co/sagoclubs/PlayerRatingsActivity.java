package za.co.sagoclubs;

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
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PlayerRatingsActivity extends Activity {

	private ScrollView scrollView;
	private ProgressDialog dialog;
	private TableLayout table;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_ratings);

        scrollView = (ScrollView)findViewById(R.id.SCROLLER_ID);

        table = new TableLayout(this);  
        
        table.setStretchAllColumns(true);  
        table.setShrinkAllColumns(true);
        table.setBackgroundColor(Color.WHITE);
      
		dialog = new ProgressDialog(this);

		dialog.setMessage("Retrieving player ratings...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
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
	    	Player[] players = InternetActions.getPlayerRatingsArray();
        	return players;
	    }

	    protected void onPostExecute(Player[] players) {
	    	setProgressBarIndeterminateVisibility(false);
	    	dialog.hide();
	        for (Player player: players) {
	            TableRow row = new TableRow(context);  
	            final Button nameButton = new Button(context);
	            nameButton.setTextColor(Color.BLACK);
	            nameButton.setBackgroundColor(Color.WHITE);
	            nameButton.setText(player.getName());
	            nameButton.setGravity(Gravity.LEFT);
	            nameButton.setHint(player.getId());
	            nameButton.setOnClickListener(new OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				String id = nameButton.getHint().toString();
        				String name = nameButton.getText().toString();
        				name = name.substring(0, name.length());
        				Result.setLogFile(new Player(id, name));
        	            Intent myIntent = new Intent(v.getContext(), LogFileActivity.class);
        	            startActivityForResult(myIntent, 0);
        			}
        		});
	            row.addView(nameButton);
	            TextView rank = new TextView(context);
	            rank.setTextColor(Color.BLACK);
	            rank.setBackgroundColor(Color.WHITE);
	            rank.setText(player.getRank());
	            rank.setGravity(Gravity.RIGHT);
	            row.addView(rank);
	            TextView index = new TextView(context);
	            index.setTextColor(Color.BLACK);
	            index.setBackgroundColor(Color.WHITE);
	            index.setText(player.getIndex()+"   ");
	            index.setGravity(Gravity.RIGHT);
	            row.addView(index);
	            table.addView(row);
	        }
	        scrollView.removeAllViews();
	        scrollView.addView(table);
//	        setContentView(table);        
	    }
	}
	
}
