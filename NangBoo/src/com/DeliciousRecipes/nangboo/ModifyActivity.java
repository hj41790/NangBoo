package com.DeliciousRecipes.nangboo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ModifyActivity extends Activity{

	SimpleDateFormat format = Ingredient.dateFormat;
	Calendar cal = Calendar.getInstance();

	CustomDialog mDialog = null;
	
	Ingredient ingredient = null;
	int position;
	
	Button back, modify, expiration;
	EditText name, memo;
	
	boolean isModifyButtonClicked = false;

	/* SharedPreferences */
	SettingPref pref;
	SharedPreferences settingPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify);

		/* SharedPreferences 설정 */
		pref = new SettingPref(getApplicationContext());
		settingPref = pref.getPref();
		
		//테마적용
		LinearLayout layout = (LinearLayout)findViewById(R.id.action_bar_modify);
		layout.setBackgroundResource(settingPref.getInt("THEME", R.color.yellow));		
		EditText editText = (EditText) findViewById(R.id.product_name_edittext_modify);
	    editText.setBackgroundResource(settingPref.getInt("THEME", R.color.yellow));
	    Button button = (Button)findViewById(R.id.expiration_date_button_modify);
	    button.setBackgroundResource(settingPref.getInt("THEME", R.color.yellow));
	    editText = (EditText) findViewById(R.id.memo_modify);
	    editText.setBackgroundResource(settingPref.getInt("THEME", R.color.yellow));
	    
	    
		Intent intent = getIntent();
		Bundle inputData = intent.getBundleExtra("INGREDIENT");
		
		ingredient = new Ingredient();
		ingredient.name = inputData.getString("NAME");
		ingredient.expirationDate = inputData.getString("EXPIRATION");
		ingredient.memo = inputData.getString("MEMO");
		
		ingredient.stringToDate();
		cal.setTime(ingredient.date);
		
		position = inputData.getInt("POSITION");
		
		//버튼 불러오기
		back = (Button) findViewById(R.id.back_button_modify);
		modify = (Button) findViewById(R.id.modify_button_modify);
		expiration = (Button) findViewById(R.id.expiration_date_button_modify);
		
		//텍스트뷰 불러오기
		name = (EditText) findViewById(R.id.product_name_edittext_modify);
		memo = (EditText) findViewById(R.id.memo_modify);
		
		name.setText(ingredient.name);
		memo.setText(ingredient.memo);
		expiration.setText(format.format(ingredient.date));
		
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Bundle outputData = new Bundle();
				
				outputData.putString("NAME", ingredient.name);
				outputData.putString("EXPIRATION", ingredient.expirationDate);
				outputData.putString("MEMO", ingredient.memo);
				outputData.putInt("POSITION", position);
				
				Intent intent = new Intent();
				intent.putExtra("RESULT_FROM_MODIFY_ACTIVITY", outputData);
				setResult(RESULT_OK, intent);
				
				finish();
			}
			
		});
		
		modify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				isModifyButtonClicked = !isModifyButtonClicked;
				if(isModifyButtonClicked){
					modify.setText("확인");
					name.setEnabled(true);
					memo.setEnabled(true);
					expiration.setEnabled(true);
				}
				else{
					createModifyDialog();
					
					modify.setText("수정");
					name.setEnabled(false);
					memo.setEnabled(false);
					expiration.setEnabled(false);
					
				}
				
			}
			
		});
		
		expiration.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//* DatePickerDialog 띄우기
				// dialog 리스너
				OnDateSetListener datePickerDialogListener = new OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						
						String dateString = year+"."+(monthOfYear+1)+"."+dayOfMonth;
						
						try {
							Date d = format.parse(dateString);
							expiration.setText(format.format(d));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				//만들어서 show
				DatePickerDialog datePickerDialog = new DatePickerDialog(ModifyActivity.this, 
																		 datePickerDialogListener, cal.get(Calendar.YEAR), 
																		 cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});
		
	}
	
	private void createModifyDialog() {

		mDialog = new CustomDialog(this);

		mDialog.setTitle("수정");
		mDialog.setContentText("변경 사항은 되돌릴 수 없습니다. 수정하시겠습니까?");

		mDialog.setOKClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//변경사항 저장
				ingredient.name = name.getText().toString();
				ingredient.expirationDate = expiration.getText().toString();
				ingredient.memo = memo.getText().toString();
				
				Toast.makeText(ModifyActivity.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();

				mDialog.dismiss();
			}
		});

		mDialog.setCancelListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// 수정 전 처음 값으로 되돌리기!!!
				name.setText(ingredient.name);
				memo.setText(ingredient.memo);
				expiration.setText(format.format(ingredient.date));
				
				mDialog.dismiss();
			}
		});

		mDialog.show();

	}
	

}
