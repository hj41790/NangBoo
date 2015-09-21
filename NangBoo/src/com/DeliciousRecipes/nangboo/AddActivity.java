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
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddActivity extends Activity {

   // use for qr code
   // 확인하고싶은 package의 이름
   private static final String CHECK_PACKAGE_NAME = "com.google.zxing.client.android";
   // Done

   SimpleDateFormat format;
   Calendar cal;

   EditText name;
   Button date_button;
   EditText memo;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_add);

      //테마적용
      LinearLayout layout = (LinearLayout)findViewById(R.id.action_bar_add);
      layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
      EditText editText = (EditText) findViewById(R.id.product_name_edittext);
      editText.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
      Button button = (Button)findViewById(R.id.expiration_date_button);
      button.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
      editText = (EditText) findViewById(R.id.memo_add);
      editText.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
		
      // QRCode 인식
      Intent intent = getIntent();
      Boolean qr = intent.getExtras().getBoolean("qrcode");
      if (qr == true) {
         
         PackageManager pm = getPackageManager();
         try {
            //barcode scanner 어플이 핸드폰에 있는지 확인 없으면 설치유도
            pm.getApplicationInfo(CHECK_PACKAGE_NAME.toLowerCase(),
                  PackageManager.GET_META_DATA);
            Intent QRintent = new Intent("com.google.zxing.client.android.SCAN");
            QRintent.putExtra("SCAN_MODE", "BAR_CODE_MODE");
            //complete using app 없이 바로 barcode scanner로 넘어가게 해주는 역할
            QRintent.setClassName("com.google.zxing.client.android", "com.google.zxing.client.android.CaptureActivity");
            startActivityForResult(QRintent, 0);
         } catch (NameNotFoundException e) {
            //어플리케이션 설치
            AlertDialog.Builder builder = null;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("바코드 스캐너 설치");
            builder.setMessage("QR코드 인식을 위해서는 바코드 스캐너 설치가 필요합니다. 설치하시겠습니까?");
            builder.setPositiveButton("예",
                  new DialogInterface.OnClickListener() {

                     @Override
                     public void onClick(DialogInterface dialog,
                           int whichButton) {
                        // TODO Auto-generated method stub
                        //마켓으로 이동하게 해주는 역할
                        Uri uri = Uri
                              .parse("market://details?id=com.google.zxing.client.android");
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                              uri);
                        startActivity(intent);
                     }
                  });
            builder.setNegativeButton("아니오",
                  new DialogInterface.OnClickListener() {

                     @Override
                     public void onClick(DialogInterface dialog,
                           int whichButton) {
                     }
                  });
            builder.show();
         }
      }

      // 현재날짜 date_button에 띄우고 시작하기
      cal = Calendar.getInstance();
      Date now = new Date();
      format = new SimpleDateFormat("yyyy.MM.dd");

      // 뷰 불러오기
      name = (EditText) findViewById(R.id.product_name_edittext);
      date_button = (Button) findViewById(R.id.expiration_date_button);
      memo = (EditText) findViewById(R.id.memo_add);
      
      date_button.setText(format.format(now));

      /* 리스너 추가 */
      // 추가 버튼 리스너
      Button okButton = (Button) findViewById(R.id.ok_button);
      okButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {

            if (name.getText().toString().equals("")) {
               Toast.makeText(AddActivity.this, "제품명을 입력하세요",
                     Toast.LENGTH_SHORT).show();
            } else {

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

      // 백, 취소버튼 리스너
      Button backButton = (Button) findViewById(R.id.back_button);

      backButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {

            Intent intent = new Intent();

            setResult(RESULT_CANCELED, intent);
            finish();
         }
      });

      // 유통기한 날짜 선택 리스너
      date_button.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {

            OnDateSetListener datePickerDialogListener = new OnDateSetListener() {
               @Override
               public void onDateSet(DatePicker view, int year,
                     int monthOfYear, int dayOfMonth) {

                  String dateString = year + "." + (monthOfYear + 1)
                        + "." + dayOfMonth;
                  try {
                     Date d = format.parse(dateString);
                     date_button.setText(format.format(d));
                  } catch (ParseException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
            };
            // 만들어서 show
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                  AddActivity.this, datePickerDialogListener, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                  cal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
         }
      });

   }// onCreate 끝!

   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      if (requestCode == 0) {
         if (resultCode == RESULT_OK) {
            String barcode = intent.getStringExtra("SCAN_RESULT");
            // result[0] = name || result[1] = dataString
            String[] result = barcode.split("#");

            // 이름 바꾸기
            name.setText(result[0]);
            // 날짜 바꾸기
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