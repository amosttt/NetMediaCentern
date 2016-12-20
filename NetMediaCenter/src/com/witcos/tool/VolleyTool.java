package com.witcos.tool;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.jz.image.BitmapCache;

public class VolleyTool {

	public static ImageLoader getImageLoader(Context context) {
		RequestQueue queue = Volley.newRequestQueue(context);
		ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());
		return imageLoader;
	}

}
