package cut.ac.cy.my_tour_guide.poi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import cut.ac.cy.my_tour_guide.R;

@SuppressLint("SetJavaScriptEnabled")
public class PoiBrowser extends Activity{

	private WebView webView = null;
	private String link = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.poi_browser);
		
		Intent intent = getIntent();
		link = intent.getStringExtra("Link");
		
		webView = (WebView) findViewById(R.id.webView);
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webView.setWebViewClient(new contentHandler(this));
		webView.setWebChromeClient(new uiHandler(this));
		webView.loadUrl(link);
		
		
	}
	
	/**an kanei redirect i selida tote anoigei ton default browser tou kinitou
	 * gia na to apotrepsoume kaloume shouldOverrideUrlLoading
	 * Episis xeirizomaste ta error messages
	 * beggining android for appdev 245
	 */
	private class contentHandler extends WebViewClient{
		private Activity activity;
		private String errorMessage = "Page cannot be loaded!";
		public contentHandler(Activity activity) {
			this.activity = activity;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
		}
		
		
	}
	/**
	 * 
	 * kaleitai gia na mas deiksei tin progress kathws fortwnei i selida
	 *
	 */
	private class uiHandler extends WebChromeClient{
		private Activity activity;
		public uiHandler(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onProgressChanged(WebView view, int progress) {
			//to 1000 xrisimopoieitai giati exoun diaforetiki klimaka ta webviews
			activity.setProgress(progress * 1000);
		}
		
	}
}
