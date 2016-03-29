package trikita.promote;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class Intents {
    public final static int NONE = -1;
    public final static int RATE = 0;
    public final static int FACEBOOK_APP = 1;
    public final static int FACEBOOK_WEB = 2;
    public final static int TWITTER_APP = 3;
    public final static int TWITTER_WEB = 4;

    private final static String PLAY_APP_URL = "https://play.google.com/store/apps/details?id=";
    private final static String FACEBOOK_PKG = "com.facebook.katana";
    private final static String FACEBOOK_URL = "https://www.facebook.com/sharer/sharer.php?u=";
    private final static String TWITTER_PKG = "com.twitter";
    private final static String TWITTER_URL = "https://twitter.com/intent/tweet?text=%s&url=%s";

    public static int canShare(Context c, int where) {
	switch (where) {
	    case Promote.TWITTER:
		return (has(c, TWITTER_PKG) ? TWITTER_APP : TWITTER_WEB);
	    case Promote.TWITTER_FACEBOOK:
		return (has(c, TWITTER_PKG) ? TWITTER_APP : (has(c, FACEBOOK_PKG) ? FACEBOOK_APP : TWITTER_WEB));
	    case Promote.FACEBOOK:
		return (has(c, FACEBOOK_PKG) ? FACEBOOK_APP : FACEBOOK_WEB);
	    case Promote.FACEBOOK_TWITTER:
		return (has(c, FACEBOOK_PKG) ? FACEBOOK_APP : (has(c, TWITTER_PKG) ? TWITTER_APP : FACEBOOK_WEB));
	}
	return NONE;
    }

    public static Intent of(Context c, int where, String url, String text) {
	switch (where) {
	    case RATE:
		return new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_APP_URL + c.getPackageName()));
	    case FACEBOOK_APP:
		return facebook(c, url);
	    case TWITTER_APP:
		return twitter(c, url, text);
	    case FACEBOOK_WEB:
		return new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL+url));
	    case TWITTER_WEB:
		return new Intent(Intent.ACTION_VIEW,
		    Uri.parse(String.format(TWITTER_URL, urlEncode(text), urlEncode(url))));
	}
	return null;
    }

    public static Drawable getAppIcon(Context c, int id) {
	PackageManager pm = c.getPackageManager();
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.addCategory("android.intent.category.LAUNCHER");
	List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

	String packageName = (id == TWITTER_APP ? TWITTER_PKG : FACEBOOK_PKG);
	for (ResolveInfo ri : apps) {
	    if (ri.activityInfo.packageName.toLowerCase().startsWith(packageName)) {
		return pm.getApplicationIcon(ri.activityInfo.applicationInfo);
	    }
	}
	return null;
    }

    private static boolean has(Context c, String packageName) {
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.addCategory("android.intent.category.LAUNCHER");
	List<ResolveInfo> matches = c.getPackageManager().queryIntentActivities(intent, 0);
	for (ResolveInfo info : matches) {
	    if (info.activityInfo.packageName.toLowerCase().startsWith(packageName)) {
		return true;
	    }
	}
	return false;
    }

    private static Intent twitter(Context c, String url, String text) {
	String tweetUrl = String.format(TWITTER_URL, urlEncode(text), urlEncode(url));
	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

	List<ResolveInfo> matches = c.getPackageManager().queryIntentActivities(intent, 0);
	for (ResolveInfo info : matches) {
	    if (info.activityInfo.packageName.toLowerCase().startsWith(TWITTER_PKG)) {
		intent.setPackage(info.activityInfo.packageName);
		return intent;
	    }
	}
	return null;
    }

    private static Intent facebook(Context c, String url) {
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType("text/plain");
	intent.putExtra(Intent.EXTRA_TEXT, url);

	List<ResolveInfo> matches = c.getPackageManager().queryIntentActivities(intent, 0);
	for (ResolveInfo info : matches) {
	    if (info.activityInfo.packageName.toLowerCase().startsWith(FACEBOOK_PKG)) {
		intent.setPackage(info.activityInfo.packageName);
		return intent;
	    }
	}
	return null;
    }

    private static String urlEncode(String s) {
	try {
	    return URLEncoder.encode(s, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException("URLEncoder.encode() failed for " + s);
	}
    }
}
