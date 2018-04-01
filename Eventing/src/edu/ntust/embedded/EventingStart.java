package edu.ntust.embedded;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EventingStart<EventStart> extends Activity {
    //private Button mBtnLoginDlg;
	private ImageButton mBtnLoginDlg;
    private TextView mTxtResult;
    private Dialog mLoginDlg;
    private int clo = 0;

	/** Called when the activity is first created. */
    @Override
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        setupViewComponent();
        spark();
    }
    
    private void setupViewComponent() {
    	mBtnLoginDlg = (ImageButton)findViewById(R.id.imageClock);
    	mTxtResult = (TextView)findViewById(R.id.txtResult);
    	
    	mBtnLoginDlg.setOnClickListener(btnLoginDlgOnClkLis);
    	
    	

    	
    }
    private Button.OnClickListener btnLoginDlgOnClkLis = new Button.OnClickListener(){
    	public void onClick(View v) {
    		Intent intent = new Intent();
			//intent.setClass(EventingStart.this, ScheduleNewActivity.class);
			intent.setClass(EventingStart.this, ScheduleExpandableList.class);
			startActivity(intent);
    		/***
    		mTxtResult.setText("");
    		
    		mLoginDlg = new Dialog(EventingStart.this);
    		mLoginDlg.setTitle("登入系統");
    		mLoginDlg.setCancelable(false);
    		mLoginDlg.setContentView(R.layout.login);
    		Button loginBtnOK = (Button)mLoginDlg.findViewById(R.id.btnLogin);
    		loginBtnOK.setOnClickListener(loginDlgBtnOKOnClkLis);
    		mLoginDlg.show();
    		****///
    	}
    };
    
    private Button.OnClickListener loginDlgBtnOKOnClkLis = new Button.OnClickListener() {
		public void onClick(View v) {
			
			EditText edtAccount = (EditText)mLoginDlg.findViewById(R.id.edtAccount);
			EditText edtPassword = (EditText)mLoginDlg.findViewById(R.id.edtPassword);
			String s = new String("你輸入的使用者名稱:" + edtAccount.getText().toString() + "密碼:" + edtPassword.getText().toString());
			//mTxtResult.setText("嚙璀嚙踝蕭J嚙踝蕭嚙誕用者名嚙踝蕭:" + edtAccount.getText().toString() + "嚙皺嚙碼:" + edtPassword.getText().toString());
			mLoginDlg.cancel();
			if(edtAccount.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty())
			Toast.makeText(EventingStart.this, R.string.login_failed, Toast.LENGTH_LONG).show();
			
			else {
				Toast.makeText(EventingStart.this, R.string.login_success, Toast.LENGTH_LONG).show();
				//Toast.makeText(EventingStart.this, s, Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				//intent.setClass(EventingStart.this, ScheduleNewActivity.class);
				intent.setClass(EventingStart.this, ScheduleList.class);
				startActivity(intent);
			}
		}
	};
	public void spark() {
		final TextView touchScreen = (TextView) findViewById(R.id.textHint);
		Timer timer = new Timer();
		TimerTask taskcc = new TimerTask(){

		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {

					if (clo == 0) {
						clo = 1;
						touchScreen.setTextColor(Color.TRANSPARENT); // 透明
					} else {
						if (clo == 1) {
							clo = 2;
							touchScreen.setTextColor(Color.LTGRAY);
						} else {
							clo = 0;
						}
					}
				}
			});
		}
		};

		timer.schedule(taskcc, 1, 700); 

		}

}