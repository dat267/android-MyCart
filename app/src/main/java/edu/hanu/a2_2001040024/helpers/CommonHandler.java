package edu.hanu.a2_2001040024.helpers;
import android.os.Handler;
import android.os.Looper;
public class CommonHandler {
	private static Handler handler;
	private CommonHandler() {
	}
	public static synchronized Handler getInstance() {
		if (handler == null) {
			handler = new Handler(Looper.getMainLooper());
		}
		return handler;
	}
}
