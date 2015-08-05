package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final static int MODIFY_ACTIVITY = 0;
	final static int ADD_ACTIVITY = 1;

	ListView mListView = null;
	BaseAdapter_main mAdapter = null;
	ArrayList<Ingredient> mData = null;

	Button chooseIngredient, multiple, addIngredient, searchingRecipe,
			bookmark, setting;

	boolean isClicked_chooseButton = false;

	CustomDialog mDialog = null;

	/* 데이터베이스 구축 */
	public IngredientDBManager mDBmanager = null;

	/* 팝업 윈도우 관련 변수 */
	PopupWindow popupWindow = null;
	View popupLayout = null;

	Button modifyBtn, closeBtn;
	EditText name, expiration, memo;

	boolean isModifyBtnSelected;
	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDBmanager = IngredientDBManager.getInstance(this);

		// 리스트뷰 생성 및 아이템 선택 리스너 설정
		mAdapter = new BaseAdapter_main(this);

		
		/*
		mDBmanager.delete(null, null);

		for (int i = 0; i < 50; i++) {
			Ingredient ingredient = new Ingredient();
			ingredient.name = "재료" + i;
			ingredient.expirationDate = "2015.08." + (int)((Math.random()*100)%20+1);
			ingredient.memo = i + "번째";

			mAdapter.add(ingredient);
		}
		*/
		
		
		
		mListView = (ListView) findViewById(R.id.listview_main);
		mListView.setDivider(new ColorDrawable(Color.rgb(240, 240, 210)));
		mListView.setDividerHeight(2);
		mListView.setAdapter(mAdapter);

		// 리스트뷰 아이템 클릭 리스너
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int a,
					long id) {
				// TODO Auto-generated method stub

				if (isClicked_chooseButton) {
					mAdapter.itemChoosed(a);
				} else {
					
					Ingredient tmp = (Ingredient) mAdapter.getItem(a);
					
					Bundle bundleData = new Bundle();
					bundleData.putString("NAME", tmp.name);
					bundleData.putString("EXPIRATION", tmp.expirationDate);
					bundleData.putString("MEMO", tmp.memo);
					bundleData.putInt("POSITION", a);

					Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
					intent.putExtra("INGREDIENT", bundleData);
					startActivityForResult(intent, MODIFY_ACTIVITY);
				}
			}
		});

		// 버튼 불러오기
		chooseIngredient = (Button) findViewById(R.id.chooseIngredient_main);
		addIngredient = (Button) findViewById(R.id.addIngredient_main);
		bookmark = (Button) findViewById(R.id.bookmark_main);
		multiple = (Button) findViewById(R.id.multiple_main);
		searchingRecipe = (Button) findViewById(R.id.searchingRecipe_main);
		setting = (Button) findViewById(R.id.setting_main);

		// 재료선택 버튼리스너 설정
		chooseIngredient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isClicked_chooseButton = !isClicked_chooseButton;

				if (isClicked_chooseButton) {
					multiple.setText("삭제");
					chooseIngredient
							.setBackgroundResource(R.drawable.btn_default_focused_holo_light);
				} else {
					multiple.setText("정렬");
					mAdapter.clear();
					chooseIngredient
							.setBackgroundResource(R.drawable.btn_default_normal_holo_light);
				}
			}
		});

		// 정렬 버튼리스너 설정
		multiple.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isClicked_chooseButton) { // 삭제모드
					createDeleteDialog();
				} else { // 정렬모드

				}
			}
		});

		//재료추가 버튼리스너 설정
		addIngredient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				startActivityForResult(intent, ADD_ACTIVITY);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case MODIFY_ACTIVITY :
				if(resultCode==RESULT_OK){
					
					Bundle inputData = data.getBundleExtra("RESULT_FROM_MODIFY_ACTIVITY");
					
					Ingredient modifyValue = new Ingredient();

					modifyValue.name = inputData.getString("NAME");
					modifyValue.expirationDate = inputData.getString("EXPIRATION");
					modifyValue.memo = inputData.getString("MEMO");
					
					mAdapter.modify(inputData.getInt("POSITION"), modifyValue);
				}
			case ADD_ACTIVITY :
				if (resultCode == RESULT_OK) {
					Bundle inputData = data.getBundleExtra("RESULT_FROM_ADD_ACTIVITY");

					Ingredient modifyValue = new Ingredient();

					modifyValue.name = inputData.getString("NAME");
					modifyValue.expirationDate = inputData.getString("EXPIRATION");
					modifyValue.memo = inputData.getString("MEMO");
					
					mAdapter.add(modifyValue);
				}
		
		}		
		
		
	}

	private void createDeleteDialog() {

		mDialog = new CustomDialog(this);

		mDialog.setTitle("삭제");
		mDialog.setContentText("선택하신 항목을 삭제하시겠습니까?");

		mDialog.setOKClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int count = mAdapter.delete();

				if (count > 0)
					Toast.makeText(MainActivity.this, "삭제되었습니다.",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(MainActivity.this, "선택한 항목이 없습니다.",
							Toast.LENGTH_SHORT).show();

				mDialog.dismiss();
			}
		});

		mDialog.setCancelListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

		mDialog.show();

	}
}
