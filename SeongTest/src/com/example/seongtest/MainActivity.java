package com.example.seongtest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	myDBHelper myHelper;
	EditText edtName, edtNumber,edtNameResult, edtNumberResult;
	Button btnInit, btnInsert, btnSelect;
	SQLiteDatabase sqlDB;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//java&xml
		edtName = (EditText) findViewById(R.id.edtName);
		edtNumber = (EditText) findViewById(R.id.edtNumber);
		btnInit = (Button) findViewById(R.id.btnInit);
		btnInsert = (Button) findViewById(R.id.btnInsert);
		btnSelect = (Button) findViewById(R.id.btnSelect);
		edtNameResult = (EditText)findViewById(R.id.editText1);
		edtNumberResult = (EditText)findViewById(R.id.editText2);

		// DB생성
		myHelper = new myDBHelper(this);

		// 버튼생성
		btnInit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sqlDB = myHelper.getWritableDatabase();
				myHelper.onUpgrade(sqlDB, 1, 2);
				sqlDB.close();
			}
		});
		btnInsert.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sqlDB = myHelper.getWritableDatabase();
				sqlDB.execSQL("insert into groupTBL values('"
						+ edtName.getText().toString() +"',"
						+ edtNumber.getText().toString() + ");");
				sqlDB.close();
				Toast.makeText(getApplicationContext(), "입력됨", 0).show();
			}
		});
		btnSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sqlDB = myHelper.getReadableDatabase();

				Cursor cursor;
				cursor = sqlDB.rawQuery("select * from groupTBL;", null);

				String strNames = "재료 이름" + "\r\n" + "-------" + "\r\n";
				String strNumbers = "개수" + "\r\n" + "-------" + "\r\n";

				while (cursor.moveToNext()) {
					strNames += cursor.getString(0) + "\r\n";
					strNumbers += cursor.getString(1) + "\r\n";
				}

				edtNameResult.setText(strNames);
				edtNumberResult.setText(strNumbers);

				cursor.close();
				sqlDB.close();
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
