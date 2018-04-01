package edu.ntust.embedded;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddEventWorkerActivity extends Activity {

	private TextView phoneTxtView;
	private EditText jobEdtTxt;
	private TextView nameTxtView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neweventworker);
		
		String name = getIntent().getStringExtra("name");
		String number = getIntent().getStringExtra("number");
		
		nameTxtView = (TextView) findViewById(R.id.name);
		jobEdtTxt = (EditText) findViewById(R.id.job);
		phoneTxtView = (TextView) findViewById(R.id.phone);
		
		nameTxtView.setText(name);
		phoneTxtView.setText(number);
		
		Button btnOK = (Button) findViewById(R.id.OK);
		btnOK.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent result = new Intent();
				result.putExtra("job", jobEdtTxt.getText().toString());
				setResult(RESULT_OK, result);
				finish();
			}
			
		});
		
	}

	@Override
	protected void onDestroy() {
		
		
		super.onDestroy();
	}
	
	
	
}
