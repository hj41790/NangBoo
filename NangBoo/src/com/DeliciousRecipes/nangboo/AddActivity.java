package com.DeliciousRecipes.nangboo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//import java.sql.Date;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class AddActivity extends Activity  {

	SimpleDateFormat format;
	Button date_button;
	/*
	 * �����ؾߵ�
	 * 2. �޸� �Է��ϴ� �� �߰�
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		//���糯¥ date_button�� ���� �����ϱ�
		final Calendar cal = Calendar.getInstance();
		format = new SimpleDateFormat("yyyy.MM.dd");
		date_button = (Button)findViewById(R.id.expiration_date_button);
		Date now = new Date();
		date_button.setText(format.format(now));
		
		/* ������ �߰�*/
		//��ư ������
		Button okButton = (Button)findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(AddActivity.this, "OK button is clicked.", Toast.LENGTH_LONG).show();
			}
		});

		Button backButton = (Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//������� ��¥ ���� ������
		date_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/* DatePickerDialog ���� */
				// dialog ������
				OnDateSetListener datePickerDialogListener = new OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						
						String dateString = year+"."+(monthOfYear+1)+"."+dayOfMonth;	
						try {
							Date d = format.parse(dateString);
							date_button.setText(format.format(d));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
				};
				//���� show
				DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, datePickerDialogListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});

	
	}//onCreate ��!

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
