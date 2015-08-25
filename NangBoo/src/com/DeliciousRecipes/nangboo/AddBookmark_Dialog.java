package com.DeliciousRecipes.nangboo;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddBookmark_Dialog extends Dialog{
	
	Button okBtn, cancelBtn;
	EditText editText;

	public AddBookmark_Dialog(Context context) {
		super(context);
		setContentView(R.layout.addbookmark_dialog);
		
		okBtn = (Button)findViewById(R.id.buttonA_addbookmark);
		cancelBtn = (Button)findViewById(R.id.buttonB_addbookmark);
		editText = (EditText)findViewById(R.id.name_addbookmark);
	}

	public void setOKClickListener(View.OnClickListener _okListener){
		okBtn.setOnClickListener(_okListener);
	}
	
	public void setCancelListener(View.OnClickListener _cancelListener){
		cancelBtn.setOnClickListener(_cancelListener);
	}
	
	public boolean isEditTextFilled(){
		if(editText.getText().toString().compareTo("")!=0) return true;
		else return false;
	}
	
	public String getText(){
		return editText.getText().toString();
	}
	
	public void setHintText(String text){
		editText.setHint(text);
	}
}
