package trikita.promote;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

public final class Promote {

    public final static int RATE = 0;
    public final static int SHARE = 1;

    public final static int FACEBOOK_TWITTER = 0;
    public final static int TWITTER_FACEBOOK = 1;
    public final static int FACEBOOK = 2;
    public final static int TWITTER = 3;

    public static Condition.Value after(int n) { return new Condition().after(n); }
    public static Condition.Value every(int n) { return new Condition().every(n); }
    public static Condition.Value every(int n, float backoff) { return new Condition().every(n, backoff); }

    public static Promote ban(Context c, int id) {
	prefs(c).edit().putBoolean("promote:" + id + ":banned", true).apply();
	return null;
    }

    public static Promote reset(Context c) {
	prefs(c).edit().clear().apply();
	return null;
    }

    private static SharedPreferences prefs(Context c) {
	return c.getSharedPreferences("promote", 0);
    }

    private static class DialogInfo {
	public final int impressions; // how many times dialog has been shown
	public final int requests; // how many times dialog has been requested
	public final int impressionDay; // days since installation when the dialog was last shown
	public final int impressionRequest; // requests since installation when the dialog was last shown
	public final int daysSinceInstall;
	public final boolean banned;

	private final int id; // dialog ID

	private final SharedPreferences prefs;

	public DialogInfo(Context c, int id) {
	    this.id = id;
	    this.prefs = prefs(c);

	    int now = (int) (System.currentTimeMillis() / 1000 / 60 / 60 / 24);
	    if (!prefs.contains("promote:start")) {
		prefs.edit().putInt("promote:start", now).apply();
	    }
	    int start = prefs.getInt("promote:start", now);
	    this.daysSinceInstall = now - start;

	    this.impressionDay = prefs.getInt("promote:" + id + ":impression_day", -1);
	    this.impressionRequest = prefs.getInt("promote:" + id + ":impression_request", -1);

	    this.requests = prefs.getInt("promote:" + id + ":requests", 0);
	    this.impressions = prefs.getInt("promote:" + id + ":impressions", 0);

	    this.banned = prefs.getBoolean("promote:" + id + ":banned", false);
	}

	public void submit(boolean approved) {
	    prefs.edit().putInt("promote:" + id + ":requests", this.requests + 1).apply();
	    if (approved) {
		prefs.edit()
		    .putInt("promote:" + id + ":impressions", this.impressions + 1)
		    .putInt("promote:" + id + ":impressions_day", this.daysSinceInstall)
		    .putInt("promote:" + id + ":impression_request", this.requests).apply();
	    }
	}
    }

    public static class Condition {

	private class Rule {
	    int min;
	    int step;
	    float backoff;

	    public boolean isTrue(int currentValue, int lastValue, int count) {
		if (lastValue < 0) {
		    return (currentValue - min) >= 0;
		} else {
		    return (currentValue - lastValue) >= step * Math.pow(backoff, count-1);
		}
	    }
	}

	private final Rule daysRule = new Rule();
	private final Rule requestsRule = new Rule();

	public abstract class Value {
	    public Condition days() {
		apply(Condition.this.daysRule);
		return Condition.this;
	    }
	    public Condition times() {
		apply(Condition.this.requestsRule);
		return Condition.this;
	    }
	    protected abstract void apply(Rule r);
	}
	private class After extends Value  {
	    private final int min;
	    public After(int n) {
		this.min = n;
	    }
	    protected void apply(Rule  r) {
		r.min = this.min;
	    }
	}
	private class Every extends Value  {
	    private final int step;
	    private final float backoff;
	    public Every(int step, float backoff) {
		this.step = step;
		this.backoff = backoff;
	    }
	    protected void apply(Rule r) {
		r.step = this.step;
		r.backoff = this.backoff;
	    }
	}

	public Value after(int n) { return new After(n); }
	public Value every(int n) { return new Every(n, 1f); }
	public Value every(int n, float backoff) { return new Every(n, backoff); }

	public boolean shouldShow(Context c, int id) {
	    DialogInfo info = new DialogInfo(c, id);
	    return !info.banned &&
		daysRule.isTrue(info.daysSinceInstall, info.impressionDay, info.impressions) &&
		requestsRule.isTrue(info.requests, info.impressionRequest, info.impressions);
	}

	public boolean show(Context c, int id, Dialog dialog) {
	    boolean ok = false;
	    DialogInfo req = new DialogInfo(c, id);
	    if (shouldShow(c, id)) {
		if (dialog != null) { // FIXME
		    dialog.show();
		}
		ok = true;
	    }
	    req.submit(ok);
	    return ok;
	}

	public boolean share(Context c, int where, String url) {
	    return share(c, where, url, null);
	}

	public boolean share(Context c, int where, String url, String text) {
	    int id = Intents.canShare(c, where);
	    switch (id) {
		case Intents.FACEBOOK_APP:
		case Intents.TWITTER_APP:
		    return show(c,
			SHARE,
			Dialogs.shareAppDialog(c, SHARE, Intents.of(c, id, url, text), id));
		case Intents.FACEBOOK_WEB:
		case Intents.TWITTER_WEB:
		    return show(c,
			SHARE,
			Dialogs.shareWebDialog(c, SHARE, Intents.of(c, id, url, text)));
		default:
		    return false;
	    }
	}

	public boolean rate(Context c) {
	    return show(c,
		RATE,
		Dialogs.rateDialog(c, RATE, Intents.of(c, Intents.RATE, null, null)));
	}
    }
}
