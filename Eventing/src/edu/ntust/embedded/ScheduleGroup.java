package edu.ntust.embedded;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class ScheduleGroup extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        setupViewComponent();
        
    }

	private void setupViewComponent() {
		// TODO Auto-generated method stub
		Button mButtonSetGroupOK = (Button)findViewById(R.id.buttonSetGroupOK);
		mButtonSetGroupOK.setOnClickListener(setGroupOKOnCli);
	}
	private Button.OnClickListener setGroupOKOnCli = new Button.OnClickListener() { 
		public void onClick(View v) {
			//setContentView(R.layout.main);
			//onDestroy();
			Intent intent = new Intent();
			intent.setClass(ScheduleGroup.this, ScheduleNewActivity.class);
			startActivity(intent);
		}
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
