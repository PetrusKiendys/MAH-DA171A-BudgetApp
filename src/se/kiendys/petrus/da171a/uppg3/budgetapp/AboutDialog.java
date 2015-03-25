package se.kiendys.petrus.da171a.uppg3.budgetapp;

/******
 * Modification of William J. Francis code, can be found at: http://www.techrepublic.com/blog/app-builder/a-reusable-about-dialog-for-your-android-apps/504
 *****/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutDialog extends Dialog {
	
	private static Context context = null;
	
	@SuppressWarnings("static-access")
	public AboutDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_about);
		
		setTitle(R.string.global_app_name);
		
		TextView tvLegal = (TextView)findViewById(R.id.tvAboutDialogLegalText);
		String legalStr = readPlainTextFile(R.raw.legal_text);
		legalStr = legalStr.toUpperCase();
		tvLegal.setText(legalStr);
		
		TextView tvInfo = (TextView)findViewById(R.id.tvAboutDialogInfoText);
		tvInfo.setText(Html.fromHtml(readPlainTextFile(R.raw.info_text)));
		tvInfo.setTextSize(12);
		tvInfo.setLinkTextColor(Color.WHITE);
		Linkify.addLinks(tvInfo, Linkify.ALL);
	}
	
	public static String readPlainTextFile(int id) {
		InputStream is = context.getResources().openRawResource(id);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		StringBuilder text = new StringBuilder();
		try {
			while ((line = br.readLine()) != null)
				text.append(line);
		} catch (IOException e) {
			return null;
		}
		return text.toString();
	}
	
	

}
