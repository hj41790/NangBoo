package com.DeliciousRecipes.nangboo.copy;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.DeliciousRecipes.nangboo.R;

public class CustomDialog extends Dialog{
	
	Button 		okBtn 		= null;
	Button 		cancelBtn 	= null;
	TextView 	textview 	= null;

	public CustomDialog(Context context) {
		super(context);

		setContentView(R.layout.layout_dialog);
		
		okBtn = (Button)findViewById(R.id.buttonA_popupBox);
		cancelBtn = (Button)findViewById(R.id.buttonB_popupBox);
		textview = (TextView)findViewById(R.id.text_popupBox);
	}

	public void setOKClickListener(View.OnClickListener _okListener){
		okBtn.setOnClickListener(_okListener);
	}
	
	public void setCancelListener(View.OnClickListener _cancelListener){
		cancelBtn.setOnClickListener(_cancelListener);
	}
	
	public void setContentText(String contentText){
		textview.setText(contentText);
	}
}
