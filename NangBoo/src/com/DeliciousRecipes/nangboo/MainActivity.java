package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends Activity {

	ListView mListView = null;
	BaseAdapter_main mAdapter = null;
	ArrayList<Ingredient> mData = null;

	Button 	chooseIngredient, multiple, addIngredient, 
			searchingRecipe, bookmark, setting;

	boolean isClicked_chooseButton = false;
	
	CustomDialog mDialog = null;
	
	
	/* 팝업 윈도우 관련 변수*/
	PopupWindow popupWindow = null;
	View 		popupLayout = null;
	
	Button modifyBtn, closeBtn;
	EditText name, expiration, memo;
	
	boolean isModifyBtnSelected;
	int position;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		mData = new ArrayList<Ingredient>();

		for (int i = 0; i < 50; i++) {
			Ingredient ingredient = new Ingredient();
			ingredient.name = "재료" + i;
			ingredient.expirationDate = "15-08-1" + i;

			mData.add(ingredient);
		}
		
		
		// 리스트뷰 생성 및 아이템 선택 리스너 설정
		mAdapter = new BaseAdapter_main(this, mData);

		mListView = (ListView) findViewById(R.id.listview_main);
		mListView.setDivider(new ColorDrawable(Color.rgb(240, 240, 210)));
		mListView.setDividerHeight(2);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (isClicked_chooseButton) {
					mAdapter.itemChoosed(position);
				}
				else{
					createItemSelectedPopup(position);
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


		
		// 버튼리스너 설정
		chooseIngredient.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isClicked_chooseButton = !isClicked_chooseButton;

				if (isClicked_chooseButton){
					multiple.setText("삭제");
					chooseIngredient.setBackgroundResource(R.drawable.btn_default_focused_holo_light);
				}
				else{
					multiple.setText("정렬");
					mAdapter.clear();
					chooseIngredient.setBackgroundResource(R.drawable.btn_default_normal_holo_light);
				}
			}
		});
		
		multiple.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isClicked_chooseButton){	//삭제모드
					createDeletePopup();
				}
				else{	// 정렬모드
					
				}
			}
		});

		/* 여기부터 경원이가 인텐트 해본다고 추가한거 ^-^! 된당!!!!!!!!!
		 * 다른 버튼에도 리스너~>인텐트 넣어주기^ㅇ^ㅇ!!!!!!!!!
		 * */
		addIngredient.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				startActivity(intent);
			}
		});
		
		/* 요기까지 */
	}
	
	
	private void createDeletePopup(){
		
		mDialog = new CustomDialog(this);
		
		mDialog.setTitle("삭제");
		mDialog.setContentText("선택하신 항목을 삭제하시겠습니까?");
		
		mDialog.setOKClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int count = mAdapter.delete();
				
				if(count>0)
					Toast.makeText(MainActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
				else 
					Toast.makeText(MainActivity.this, "선택한 항목이 없습니다.", Toast.LENGTH_SHORT).show();
				
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
	
	private void createItemSelectedPopup(int a){
		
		isModifyBtnSelected = false;
		position = a;
		
		popupLayout = getLayoutInflater().inflate(R.layout.layout_modify_item, null);
		
		modifyBtn = (Button)popupLayout.findViewById(R.id.modifyButton_modifyItem);
		closeBtn = (Button)popupLayout.findViewById(R.id.closeButton_modifyItem);
		
		name = (EditText)popupLayout.findViewById(R.id.name_modifyItem);
		expiration = (EditText)popupLayout.findViewById(R.id.expiration_modifyItem);
		memo = (EditText)popupLayout.findViewById(R.id.memo_modifyItem);
		
		Ingredient tmp = (Ingredient)mAdapter.getItem(position);
		
		name.setText(tmp.name);
		expiration.setText(tmp.expirationDate);
		memo.setText(tmp.memo);
		
		modifyBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isModifyBtnSelected = !isModifyBtnSelected;
				
				if(isModifyBtnSelected){
					modifyBtn.setText("확인");
					modifyBtn.setBackgroundResource(R.drawable.btn_default_focused_holo_light);
					name.setEnabled(true);
					expiration.setEnabled(true);
					memo.setEnabled(true);
				}
				else{
					modifyBtn.setText("수정");
					modifyBtn.setBackgroundResource(R.drawable.btn_default_normal_holo_light);
					name.setEnabled(false);
					expiration.setEnabled(false);
					memo.setEnabled(false);
					
					String[] info = new String[3];
					info[0] = name.getText().toString();
					info[1] = expiration.getText().toString();
					info[2] = memo.getText().toString();
					mAdapter.modify(position, info);
				}
			}
		});
		
		closeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		
		popupWindow = new PopupWindow(popupLayout, 
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		
		//popupWindow.showAsDropDown(chooseIngredient);
		popupWindow.showAsDropDown(chooseIngredient, -10, 50);
		
	}
}
























