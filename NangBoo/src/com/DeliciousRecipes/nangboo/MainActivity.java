package com.DeliciousRecipes.nangboo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	final static int MODIFY_ACTIVITY = 0;
	final static int ADD_ACTIVITY = 1;
	final static int SETTING_ACTIVITY = 2;
	
	public static final int SORT_NAME = 0;
	public static final int SORT_DATE = 1;
	public static final int SORT_REGISTER = 2;
	
	final static int RESULT_THEME = 0;
	final static int RESULT_FONTSIZE = 1;

	
	/* 데이터베이스 구축 */
	public IngredientDBManager mDBmanager = null;


	ListView 				mListView = null;
	BaseAdapter_main 		mAdapter = null;

	Button  chooseIngredient, multiple, addIngredient, 
			searchingRecipe, bookmark, setting;

	boolean isClicked_chooseButton = false;
	boolean isModifyBtnSelected = false;
	
	int position, selected;
	CustomDialog mDialog = null;
	AlertDialog.Builder builder = null;
	
	
	/* SharedPreference */
	static SharedPreferences settingPref;
	static Editor editor;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/* SharedPreference 설정 */
		settingPref = getSharedPreferences("settingPref", 0);
		editor = settingPref.edit();
		
		// noti
		if (settingPref.contains("notiIsOn"));
		else editor.putBoolean("notiIsOn", true);
		
		// THEME
		if (settingPref.contains("THEME"));
		else editor.putInt("THEME", R.color.yellow);
		
		editor.commit();

		mDBmanager = IngredientDBManager.getInstance(this);

		// 리스트뷰 생성 및 아이템 선택 리스너 설정
		mAdapter = new BaseAdapter_main(this);


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
					multiple.setBackgroundResource(R.drawable.remove_gray);
					
					switch(MainActivity.settingPref.getInt("THEME", R.color.yellow))
					{
						case R.color.green :
							chooseIngredient.setBackgroundResource(R.drawable.select_green);
							break;
						default :
							chooseIngredient.setBackgroundResource(R.drawable.select_orange);
					}
					
				} else {
					multiple.setBackgroundResource(R.drawable.sort_gray);
					mAdapter.clear();
					chooseIngredient.setBackgroundResource(R.drawable.select_gray);
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
					createSortDialog();
				}
			}
		});

		//재료추가 버튼리스너 설정
		addIngredient.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createAddDialog();
			}
		});//재료추가 끝
		
		//설정 버튼리스너 설정
		setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isClicked_chooseButton = false;
				multiple.setBackgroundResource(R.drawable.sort_gray);
				chooseIngredient.setBackgroundResource(R.drawable.select_gray);
				mAdapter.clear();
				
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivityForResult(intent, SETTING_ACTIVITY);
			}
		});//옵션 끝
		
		//레시피 검색 
	      searchingRecipe.setOnClickListener(new View.OnClickListener() {
	         
	         @Override
			public void onClick(View v) {
	        	 
				isClicked_chooseButton = false;
	     		multiple.setBackgroundResource(R.drawable.sort_gray);
	     		chooseIngredient.setBackgroundResource(R.drawable.select_gray);

				Bundle bundleData = new Bundle();
				bundleData.putString("INGREDIENT", mAdapter.ingredientURL());
				

	     		mAdapter.clear();

				Intent intent = new Intent(MainActivity.this, SearchingActivity.class);
				intent.putExtra("SEARCHING_INGREDIENT", bundleData);
				startActivity(intent);
	         }
		});
	      
		// 설정 버튼리스너 설정
		bookmark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isClicked_chooseButton = false;
				multiple.setBackgroundResource(R.drawable.sort_gray);
				chooseIngredient.setBackgroundResource(R.drawable.select_gray);
				mAdapter.clear();
				
				Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
				startActivity(intent);
			}
		});// 옵션 끝
		
	}//onCreate() 끝

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
					
					break;
				}
			case ADD_ACTIVITY :
				if (resultCode == RESULT_OK) {
					Bundle inputData = data.getBundleExtra("RESULT_FROM_ADD_ACTIVITY");

					Ingredient modifyValue = new Ingredient();

					modifyValue.name = inputData.getString("NAME");
					modifyValue.expirationDate = inputData.getString("EXPIRATION");
					modifyValue.memo = inputData.getString("MEMO");
					
					mAdapter.add(modifyValue);
					
					break;
				}
			case SETTING_ACTIVITY :
				if(resultCode == RESULT_THEME){
					mAdapter.notifyDataSetChanged();
				}
				else if(resultCode == RESULT_FONTSIZE){
					
				}
		
		}		
		
		
	}
	
	

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		
		isClicked_chooseButton = false;
		chooseIngredient.setBackgroundResource(R.drawable.select_gray);
		mAdapter.clear();
		
		super.onDestroy();
	}
	
	private void createAddDialog(){
		
		//재료 선택 방식 선택하는 팝업창
		builder = new AlertDialog.Builder(MainActivity.this);
		
		builder.setTitle("추가 방법 선택");
		String items[] = {"QR코드 인식", "직접 입력"};
		
		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selected = which;
					}
				})
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						isClicked_chooseButton = false;
						multiple.setBackgroundResource(R.drawable.sort_gray);
						chooseIngredient.setBackgroundResource(R.drawable.select_gray);
						mAdapter.clear();
						
						// 확인 버튼 터치
						// 여기서 선택한 값을 넘기면됨
						if (selected == 1) // 직접 입력
						{
							dialog.dismiss();
							Intent intent = new Intent(MainActivity.this, AddActivity.class);
							intent.putExtra("qrcode", false);
							startActivityForResult(intent, ADD_ACTIVITY);
						}
						else{
							Intent intent = new Intent(MainActivity.this, AddActivity.class);
							intent.putExtra("qrcode", true);
							startActivityForResult(intent, ADD_ACTIVITY);
						}
						
						selected = -1;
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 취소 버튼 터치
						dialog.dismiss();
					}
				});
		
		builder.show();
	}

	private void createSortDialog() {

		// 재료 선택 방식 선택하는 팝업창
		builder = new AlertDialog.Builder(MainActivity.this);

		builder.setTitle("정렬 방법 선택");
		String items[] = { "이름순", "유통기한 임박순", "등록순" };

		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selected = which;
					}
				})
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 확인 버튼 터치
						// 여기서 선택한 값을 넘기면됨

						if (selected == 0) mAdapter.sort(SORT_NAME); 			// 이름순
						else if (selected == 1) mAdapter.sort(SORT_DATE);		// 유통기한 임박순
						else if (selected == 2)mAdapter.sort(SORT_REGISTER);	// 등록순
						else mAdapter.sort(SORT_NAME);							// 아무것도 터치하지 않은 경우 이름순

						selected = -1;
						dialog.dismiss();

					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 취소 버튼 터치
						dialog.dismiss();
					}
				});

		builder.show();
	}

	private void createDeleteDialog() {

		mDialog = new CustomDialog(this);
		
		int count = mAdapter.getSelectedItemCount();
		
		if(count==0){
			mDialog.setTitle("유통기한 만료 식재료 삭제");
			mDialog.setContentText("유통기한이 지난 식재료를 모두 삭제하시겠습니까?");
		}
		else{		
			mDialog.setTitle("삭제");
			mDialog.setContentText("선택하신 항목을 삭제하시겠습니까?");
		}
		
		mDialog.setOKClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int tmp;
				
				if(mAdapter.getSelectedItemCount()==0){ 
					tmp = mAdapter.deleteExpireItem();

					if (tmp > 0)
						Toast.makeText(MainActivity.this, "삭제되었습니다.",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(MainActivity.this, "유통기한이 지난 항목이 없습니다.",
								Toast.LENGTH_SHORT).show();

				}
				else{ 
					tmp = mAdapter.delete();

					if (tmp > 0)
						Toast.makeText(MainActivity.this, "삭제되었습니다.",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(MainActivity.this, "선택한 항목이 없습니다.",
								Toast.LENGTH_SHORT).show();

				}
				
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
