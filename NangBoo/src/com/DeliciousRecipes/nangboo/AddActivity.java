package com.DeliciousRecipes.nangboo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import java.sql.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity  {

	SimpleDateFormat format;
	Calendar cal;

	EditText name;
	Button date_button;
	EditText memo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		//QRCode �ν�
		Intent intent = getIntent();
		Boolean qr = intent.getExtras().getBoolean("qrcode");
		if(qr == true){
			Intent QRintent = new Intent("com.google.zxing.client.android.SCAN");
			QRintent.putExtra("SCAN_MODE", "BAR_CODE_MODE");
			try{
				startActivityForResult(QRintent, 0);
			}catch(ActivityNotFoundException e){
				new AlertDialog.Builder(AddActivity.this)
				.setTitle("QR Scanner not found.")
				.setMessage("Please install QR code scanner")
				.setPositiveButton("OK",null)
				.show();
			}
		}

		//���糯¥ date_button�� ���� �����ϱ�
		cal = Calendar.getInstance();
		Date now = new Date();
		format = new SimpleDateFormat("yyyy.MM.dd");
		
		// �� �ҷ�����
		name = (EditText)findViewById(R.id.product_name_edittext);
		date_button = (Button)findViewById(R.id.expiration_date_button);
		memo = (EditText)findViewById(R.id.memo_add);
		
		date_button.setText(format.format(now));
		
		/* ������ �߰�*/
		// �߰� ��ư ������
		Button okButton = (Button)findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				if(name.getText().toString().equals("")) {
					Toast.makeText(AddActivity.this, "��ǰ���� �Է��ϼ���", Toast.LENGTH_SHORT).show();
				}
				else {

					Bundle outputData = new Bundle();

					outputData.putString("NAME", name.getText().toString());
					outputData.putString("EXPIRATION", date_button.getText()
							.toString());
					outputData.putString("MEMO", memo.getText().toString());

					Intent intent = new Intent();
					intent.putExtra("RESULT_FROM_ADD_ACTIVITY", outputData);

					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});

		
		
		// ��, ��ҹ�ư ������
		Button backButton = (Button)findViewById(R.id.back_button);
			
		backButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
		
		//������� ��¥ ���� ������
		date_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				OnDateSetListener datePickerDialogListener = new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

						String dateString = year + "." + (monthOfYear + 1)+ "." + dayOfMonth;
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
				DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, 
																		datePickerDialogListener, cal.get(Calendar.YEAR), 
																		cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});

	
	}//onCreate ��!
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode == 0){
			if(resultCode == RESULT_OK){
				String barcode = intent.getStringExtra("SCAN_RESULT");
				//result[0] = name || result[1] = dataString
				String[] result = barcode.split("#");
				
				//�̸� �ٲٱ�
				name.setText(result[0]);
				//��¥ �ٲٱ�
				try {
					Date d = format.parse(result[1]);
					date_button.setText(format.format(d));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
