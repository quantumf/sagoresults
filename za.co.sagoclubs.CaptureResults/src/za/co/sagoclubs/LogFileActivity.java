package za.co.sagoclubs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import static za.co.sagoclubs.Constants.TAG;

public class LogFileActivity extends Activity {

	private TextView txtOutput;
	private ScrollView scrollView;
	
	//private Spinner spnPlayer;
	private TextView txtPlayer;
	private ProgressDialog dialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logfile);
        
		dialog = new ProgressDialog(this);

		txtOutput = (TextView)findViewById(R.id.txtOutput);
        txtOutput.setEnabled(false);
        txtPlayer = (TextView)findViewById(R.id.txtPlayer);
        
        scrollView = (ScrollView)findViewById(R.id.SCROLLER_ID);
        
    	if(savedInstanceState!=null) {
            restoreProgress(savedInstanceState);
        } else {
            Log.d(TAG, "Calling server to get player logfile");
            txtPlayer.setText(Result.logfile.getName());
    		dialog.setMessage("Fetching log file...");
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
        	new LogFileTask().execute();
        }

	}

	@Override
    protected void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        saveState.putString("output", txtOutput.getText().toString());
    }
	
	private void restoreProgress(Bundle savedInstanceState) {
        String output = savedInstanceState.getString("output");
        if (output!=null) {
        	txtOutput.setMovementMethod(new ScrollingMovementMethod());
        	txtOutput.setText("");
        	txtOutput.append(output);
        	scrollView.post(new Runnable()
            {
                public void run()
                {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
	}

	@Override
	public void onPause() {
		super.onPause();
		if (dialog!=null) {
			dialog.dismiss();
		}
	}
	
	private class LogFileTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... v) {
			setProgressBarIndeterminateVisibility(true);

        	String result = InternetActions.getPreBlock("http://rank.sagoclubs.co.za/showlog.cgi?name="+Result.logfile.getId());
        	//String result = InternetActions.getPreBlock(Constants.DATAFILES + Result.logfile.getId()+".recordsheet");
        	return result;
	    }

	    protected void onPostExecute(String result) {
	    	setProgressBarIndeterminateVisibility(false);
	    	dialog.hide();
        	txtOutput.setMovementMethod(new ScrollingMovementMethod());
        	txtOutput.setText("");
        	txtOutput.append(result);
        	scrollView.post(new Runnable()
            {
                public void run()
                {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
	    }
	}
	
}
