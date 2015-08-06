package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	
	/* �����ͺ��̽� ���� */
	public IngredientDBManager mDBmanager = null;

	ListView 				mListView = null;
	BaseAdapter_main 		mAdapter = null;
	ArrayList<Ingredient> 	mData = null;

	Button  chooseIngredient, multiple, addIngredient, 
			searchingRecipe, bookmark, setting;

	boolean isClicked_chooseButton = false;
	boolean isModifyBtnSelected = false;
	
	int position, selected;
	CustomDialog mDialog = null;
	AlertDialog.Builder builder = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDBmanager = IngredientDBManager.getInstance(this);

		// ����Ʈ�� ���� �� ������ ���� ������ ����
		mAdapter = new BaseAdapter_main(this);

		
		/*
		mDBmanager.delete(null, null);

		for (int i = 0; i < 50; i++) {
			Ingredient ingredient = new Ingredient();
			ingredient.name = "���" + i;
			ingredient.expirationDate = "2015.08." + (int)((Math.random()*100)%20+1);
			ingredient.memo = i + "��°";

			mAdapter.add(ingredient);
		}
		*/
		
		
		
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
					multiple.setText("����");
					chooseIngredient
							.setBackgroundResource(R.drawable.btn_default_focused_holo_light);
				} else {
					multiple.setText("����");
					mAdapter.clear();
					chooseIngredient
							.setBackgroundResource(R.drawable.btn_default_normal_holo_light);
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
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});//�ɼ� ��
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
		
		}		
		
		
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
						// Ȯ�� ��ư ��ġ
						// ���⼭ ������ ���� �ѱ���
						if (selected == 0){ // QR�ڵ� �ν�
							;
						}
						else if (selected == 1) // ���� �Է�
						{
							dialog.dismiss();
							Intent intent = new Intent(MainActivity.this, AddActivity.class);
							startActivityForResult(intent, ADD_ACTIVITY);
						}
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

		mDialog.setTitle("����");
		mDialog.setContentText("�����Ͻ� �׸��� �����Ͻðڽ��ϱ�?");

		mDialog.setOKClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int count = mAdapter.delete();

				if (count > 0)
					Toast.makeText(MainActivity.this, "�����Ǿ����ϴ�.",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(MainActivity.this, "������ �׸��� �����ϴ�.",
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
