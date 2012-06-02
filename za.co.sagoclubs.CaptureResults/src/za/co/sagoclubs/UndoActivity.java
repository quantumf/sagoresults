package za.co.sagoclubs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UndoActivity extends Activity {

	private TextView txtOutput;
	private Button btnNewResult;
	private Button btnReturnToStart;
	private ProgressDialog dialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.undo);
        
		dialog = new ProgressDialog(this);

		txtOutput = (TextView)findViewById(R.id.txtOutput);
        txtOutput.setEnabled(false);
        
        if (Result.resultState == ResultState.Confirm) {
    		dialog.setMessage("Sending undo to server...");
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
        	new UndoResultTask().execute();
        }
        
        btnNewResult = (Button)findViewById(R.id.btnNewResult);

        btnNewResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Result.setResultState(ResultState.Enter);
                Intent myIntent = new Intent(v.getContext(), SelectWhitePlayerActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
			}
		});

        btnReturnToStart = (Button)findViewById(R.id.btnReturnToStart);

        btnReturnToStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
			}
		});

        btnNewResult.setVisibility(View.INVISIBLE);
        btnReturnToStart.setVisibility(View.INVISIBLE);

        if(savedInstanceState!=null) {
            restoreProgress(savedInstanceState);
        }

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Toast.makeText(this, "BACK not supported at this point", Toast.LENGTH_SHORT).show();
	        return true;
	    } 
	    return super.onKeyDown(keyCode, event);
	}

	@Override
    protected void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        saveState.putString("output",txtOutput.getText().toString());
    }
	
	private void restoreProgress(Bundle savedInstanceState) {
        String output = savedInstanceState.getString("output");
        if (output!=null) {
	    	txtOutput.setText(output);
        }
	}

	private class UndoResultTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... v) {
			setProgressBarIndeterminateVisibility(true);

        	InternetActions.openPage(Result.constructUndoUri());
        	String result = InternetActions.getPreBlock("http://rank.sagoclubs.co.za/refresh.html");
        	return result;
	    }

	    protected void onPostExecute(String result) {
	    	setProgressBarIndeterminateVisibility(false);
	    	dialog.hide();
        	txtOutput.setText(result);
        	Result.setResultState(ResultState.Complete);
            btnNewResult.setVisibility(View.VISIBLE);
            btnReturnToStart.setVisibility(View.VISIBLE);
	    }
	}
	
}
