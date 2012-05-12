package za.co.sagoclubs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResultConfirmActivity extends Activity {

	private EditText txtOutput;
	private Button btnUndo;
	private Button btnNewResult;
	private Button btnReturnToStart;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_confirm);
        
        txtOutput = (EditText)findViewById(R.id.txtOutput);
        txtOutput.setEnabled(false);
        btnUndo = (Button)findViewById(R.id.btnUndo);
        btnNewResult = (Button)findViewById(R.id.btnNewResult);

        if (Result.resultState == ResultState.Enter) {
        	InternetActions.openPage(Result.constructResultUri());
        	String result = InternetActions.getPreBlock("http://rank.sagoclubs.co.za/refresh.html");
        	txtOutput.setText(result);
        	Result.setResultState(ResultState.Confirm);
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
	
}
