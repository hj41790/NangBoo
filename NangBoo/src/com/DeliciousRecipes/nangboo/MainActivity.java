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

	
	/* �����ͺ��̽� ���� */
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
		
		/* SharedPreference ���� */
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

		// ����Ʈ�� ���� �� ������ ���� ������ ����
		mAdapter = new BaseAdapter_main(this);


		mListView = (ListView) findViewById(R.id.listview_main);
		mListView.setDivider(new ColorDrawable(Color.rgb(240, 240, 210)));
		mListView.setDividerHeight(2);
		mListView.setAdapter(mAdapter);

		// ����Ʈ�� ������ Ŭ�� ������
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

		// ��ư �ҷ�����
		chooseIngredient = (Button) findViewById(R.id.chooseIngredient_main);
		addIngredient = (Button) findViewById(R.id.addIngredient_main);
		bookmark = (Button) findViewById(R.id.bookmark_main);
		multiple = (Button) findViewById(R.id.multiple_main);
		searchingRecipe = (Button) findViewById(R.id.searchingRecipe_main);
		setting = (Button) findViewById(R.id.setting_main);

		// ��ἱ�� ��ư������ ����
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

		// ���� ��ư������ ����
		multiple.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isClicked_chooseButton) { // �������
					createDeleteDialog();
				} else { // ���ĸ��
					createSortDialog();
				}
			}
		});

		//����߰� ��ư������ ����
		addIngredient.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createAddDialog();
			}
		});//����߰� ��
		
		//���� ��ư������ ����
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
		});//�ɼ� ��
		
		//������ �˻� 
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
	      
		// ���� ��ư������ ����
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
		});// �ɼ� ��
		
	}//onCreate() ��

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
		
		//��� ���� ��� �����ϴ� �˾�â
		builder = new AlertDialog.Builder(MainActivity.this);
		
		builder.setTitle("�߰� ��� ����");
		String items[] = {"QR�ڵ� �ν�", "���� �Է�"};
		
		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selected = which;
					}
				})
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						isClicked_chooseButton = false;
						multiple.setBackgroundResource(R.drawable.sort_gray);
						chooseIngredient.setBackgroundResource(R.drawable.select_gray);
						mAdapter.clear();
						
						// Ȯ�� ��ư ��ġ
						// ���⼭ ������ ���� �ѱ���
						if (selected == 1) // ���� �Է�
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
				.setNegativeButton("���", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ��� ��ư ��ġ
						dialog.dismiss();
					}
				});
		
		builder.show();
	}

	private void createSortDialog() {

		// ��� ���� ��� �����ϴ� �˾�â
		builder = new AlertDialog.Builder(MainActivity.this);

		builder.setTitle("���� ��� ����");
		String items[] = { "�̸���", "������� �ӹڼ�", "��ϼ�" };

		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selected = which;
					}
				})
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Ȯ�� ��ư ��ġ
						// ���⼭ ������ ���� �ѱ���

						if (selected == 0) mAdapter.sort(SORT_NAME); 			// �̸���
						else if (selected == 1) mAdapter.sort(SORT_DATE);		// ������� �ӹڼ�
						else if (selected == 2)mAdapter.sort(SORT_REGISTER);	// ��ϼ�
						else mAdapter.sort(SORT_NAME);							// �ƹ��͵� ��ġ���� ���� ��� �̸���

						selected = -1;
						dialog.dismiss();

					}
				})
				.setNegativeButton("���", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ��� ��ư ��ġ
						dialog.dismiss();
					}
				});

		builder.show();
	}

	private void createDeleteDialog() {

		mDialog = new CustomDialog(this);
		
		int count = mAdapter.getSelectedItemCount();
		
		if(count==0){
			mDialog.setTitle("������� ���� ����� ����");
			mDialog.setContentText("��������� ���� ����Ḧ ��� �����Ͻðڽ��ϱ�?");
		}
		else{		
			mDialog.setTitle("����");
			mDialog.setContentText("�����Ͻ� �׸��� �����Ͻðڽ��ϱ�?");
		}
		
		mDialog.setOKClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int tmp;
				
				if(mAdapter.getSelectedItemCount()==0){ 
					tmp = mAdapter.deleteExpireItem();

					if (tmp > 0)
						Toast.makeText(MainActivity.this, "�����Ǿ����ϴ�.",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(MainActivity.this, "��������� ���� �׸��� �����ϴ�.",
								Toast.LENGTH_SHORT).show();

				}
				else{ 
					tmp = mAdapter.delete();

					if (tmp > 0)
						Toast.makeText(MainActivity.this, "�����Ǿ����ϴ�.",
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(MainActivity.this, "������ �׸��� �����ϴ�.",
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
