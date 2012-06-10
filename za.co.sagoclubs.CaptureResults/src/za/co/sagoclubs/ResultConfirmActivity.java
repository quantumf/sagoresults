package za.co.sagoclubs;

import static za.co.sagoclubs.Constants.TAG;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultConfirmActivity extends Activity {

	private TextView txtOutput;
	private Button btnUndo;
	private Button btnNewResult;
	private Button btnReturnToStart;
	private ProgressDialog dialog;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "ResultConfirmActivity.onCreate");
		
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		dialog = new ProgressDialog(this);

        setContentView(R.layout.result_confirm);
        
        txtOutput = (TextView)findViewById(R.id.txtOutput);
        txtOutput.setEnabled(false);
        btnUndo = (Button)findViewById(R.id.btnUndo);
        btnNewResult = (Button)findViewById(R.id.btnNewResult);
        btnReturnToStart = (Button)findViewById(R.id.btnReturnToStart);

        if (Result.resultState == ResultState.Enter) {
            btnUndo.setVisibility(View.INVISIBLE);
            btnNewResult.setVisibility(View.INVISIBLE);
            btnReturnToStart.setVisibility(View.INVISIBLE);
    		dialog.setMessage("Sending result to server...");
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
        	new SaveResultTask().execute();
        }

    	btnUndo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UndoActivity.class);
                startActivityForResult(myIntent, 0);
			}
		});

		btnNewResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        Result.setResultState(ResultState.Enter);
                Intent myIntent = new Intent(v.getContext(), SelectWhitePlayerActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
			}
		});
		

        btnReturnToStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
			}
		});

        if(savedInstanceState!=null) {
            restoreProgress(savedInstanceState);
        }
        

	}
	
//	@Override
//	public void onPause() {
//		super.onPause();
//		dialog.dismiss();
//	}
	
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
	
	private class SaveResultTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... v) {
			setProgressBarIndeterminateVisibility(true);
        	InternetActions.openPage(Result.constructResultUri());
        	String result = InternetActions.getPreBlock("http://rank.sagoclubs.co.za/refresh.html");
        	return result;
	    }

	    protected void onPostExecute(String result) {
	    	setProgressBarIndeterminateVisibility(false);
	    	dialog.dismiss();
        	txtOutput.setText(result);
        	Result.setResultState(ResultState.Confirm);
            btnUndo.setVisibility(View.VISIBLE);
            btnNewResult.setVisibility(View.VISIBLE);
            btnReturnToStart.setVisibility(View.VISIBLE);
	    }
	}
	
}
