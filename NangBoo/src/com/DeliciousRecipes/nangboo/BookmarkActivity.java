package com.DeliciousRecipes.nangboo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BookmarkActivity extends Activity {
	
	BookmarkDBManager mDBmanager = null;
	
	GridView				mListView = null;
	BaseAdapter_bookmark 	mAdapter = null;
	
	Button back, modify;

	boolean isModifyBtnSelected = false;
	
	CustomDialog mDialog;
	AddBookmark_Dialog modifyDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_bookmark);
	    
	    //�׸�����
		LinearLayout layout = (LinearLayout)findViewById(R.id.action_bar_bookmark);
		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
		
		
	    mDBmanager = BookmarkDBManager.getInstance(this);
	    
		// ����Ʈ�� ���� �� ������ ���� ������ ����
		mAdapter = new BaseAdapter_bookmark(this);

		mListView = (GridView) findViewById(R.id.listview_bookmark_activity);
		mListView.setAdapter(mAdapter);
		
		registerForContextMenu(mListView);
		
		// ����Ʈ�� ������ Ŭ�� ������
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int a,
					long id) {
				// TODO Auto-generated method stub

				if (isModifyBtnSelected) {
					mAdapter.itemChoosed(a);
				} else {

					Bookmark tmp = (Bookmark) mAdapter.getItem(a);

					Bundle bundleData = new Bundle();
					bundleData.putString("URL", tmp.URL);
					
					Intent intent = new Intent(BookmarkActivity.this, WebviewActivity.class);
					intent.putExtra("SEARCHING_URL", bundleData);
					startActivity(intent);
				}
			}
		});
		
		back = (Button) findViewById(R.id.back_button_bookmark);
		modify = (Button) findViewById(R.id.modify_button_bookmark);
		
		modify.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isModifyBtnSelected = !isModifyBtnSelected;

				if (isModifyBtnSelected) {
					modify.setText("����");
				} else {
					createDeleteDialog();
					modify.setText("����");
					mAdapter.clear();
				}
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mAdapter.clear();
				finish();
			}
		});
		
		
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_bookmark, menu);
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = info.position; // AdapterView�ȿ��� ContextMenu�� �����ִ� �׸��� ��ġ
		
		switch(item.getItemId())
		{
			case R.id.context_menu_link_bookmark :
				Bookmark tmp = (Bookmark) mAdapter.getItem(index);

				Bundle bundleData = new Bundle();
				bundleData.putString("URL", tmp.URL);
				
				Intent intent = new Intent(BookmarkActivity.this, WebviewActivity.class);
				intent.putExtra("SEARCHING_URL", bundleData);
				startActivity(intent);
				
				break;
			case R.id.context_menu_modify_name_bookmark :
				createModifyBookmarkDialog(index);
				break;
			case R.id.context_menu_delete_bookmark :
				mDBmanager.delete("_id="+mAdapter.getItemId(index), null);
				mAdapter.notifyDataSetChanged();
				break;
			default :
				
		}
		
		return super.onContextItemSelected(item);
	}


	@Override
	public void onContextMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onContextMenuClosed(menu);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if((keyCode==KeyEvent.KEYCODE_BACK) && isModifyBtnSelected){
			isModifyBtnSelected = !isModifyBtnSelected;
			modify.setText("����");
			mAdapter.clear();
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void createModifyBookmarkDialog(final int position){
		modifyDialog = new AddBookmark_Dialog(this);
		modifyDialog.setTitle("����");
		
		Bookmark item = (Bookmark)mAdapter.getItem(position);
		modifyDialog.setText(item.name);
		
		modifyDialog.changeOKBtnText("����");
		
		modifyDialog.setOKClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(modifyDialog.isEditTextFilled()){

					mAdapter.modify(position, modifyDialog.getText());

					modifyDialog.dismiss();
					Toast.makeText(BookmarkActivity.this, "�����Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
					
				}
				else Toast.makeText(BookmarkActivity.this, "������ �Է��ϼ���.", Toast.LENGTH_SHORT).show();
			}
		});

		modifyDialog.setCancelListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				modifyDialog.dismiss();
			}
		});

		modifyDialog.show();
		
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
					Toast.makeText(BookmarkActivity.this, "�����Ǿ����ϴ�.",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(BookmarkActivity.this, "������ �׸��� �����ϴ�.",
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
