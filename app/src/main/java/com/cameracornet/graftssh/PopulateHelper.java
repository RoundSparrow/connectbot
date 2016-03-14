package com.cameracornet.graftssh;

import android.content.Context;

/**
 * Created by adminsag on 3/13/16.
 */
public class PopulateHelper {
	public static void putOnClipboard(Context context, String content)
	{
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(content);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData.newPlainText("Populate This", content);
			clipboard.setPrimaryClip(clip);
		}
	}
}
