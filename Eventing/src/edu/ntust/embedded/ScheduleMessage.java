package edu.ntust.embedded;

import java.security.acl.Group;
import java.util.Calendar;

import android.R.bool;
import android.app.*;
import android.content.*;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class ScheduleMessage extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messager);
        setupViewComponent();   
	}
	private void setupViewComponent() {
		// TODO Auto-generated method stub
		Button mButtonMesOK = (Button)findViewById(R.id.buttonSendMes);
		mButtonMesOK.setOnClickListener(setMesOKOnCli);
	}
	private Button.OnClickListener setMesOKOnCli = new Button.OnClickListener() { 
		public void onClick(View v) {
			//setContentView(R.layout.main);
			Intent intent = new Intent();
			intent.setClass(ScheduleMessage.this, ScheduleNewActivity.class);
			startActivity(intent);
		}
	};
}
