package com.DeliciousRecipes.nangboo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebviewActivity extends Activity{

	
	Button back, bookmark;
	WebView webview;
	ProgressBar progressBar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.searching_webview);
	    
	    Intent intent = getIntent();
		Bundle inputData = intent.getBundleExtra("SEARCHING_URL");
		
		String url = inputData.getString("URL");

		back = (Button) findViewById(R.id.back_button_webview);
		bookmark = (Button) findViewById(R.id.add_bookmark_button);
		webview = (WebView) findViewById(R.id.webview);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(url);
		webview.setWebViewClient(new WebViewClient(){
			public boolean shoudOverridUrlLoading(WebView view, String url){
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setProgress(0);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.GONE);
				progressBar.setProgress(100);
				super.onPageFinished(view, url);
			}
		});
		webview.setWebChromeClient(new WebChromeClient(){
			public void onProgressChanged(WebView view, int newProgress){
				setValue(newProgress);
				super.onProgressChanged(view, newProgress);
			}
		});
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {				
				finish();
			}
			
		});
		
		bookmark.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(WebviewActivity.this, "즐겨찾기에 추가되었습니다.",Toast.LENGTH_SHORT).show();
			}
			
		});
	
	}
	
	public void setValue(int progress){
		this.progressBar.setProgress(progress);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if((keyCode==KeyEvent.KEYCODE_BACK) && webview.canGoBack()){
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
