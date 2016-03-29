package trikita.promote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class Dialogs {

    public static Dialog rateDialog(final Context c, final int id, final Intent intent) {
	return new AlertDialog.Builder(c)
	    .setTitle(R.string.rate_dialog_title)
	    .setMessage(R.string.rate_dialog_message)
	    .setPositiveButton(R.string.rate_dialog_btn_ok, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    c.startActivity(intent);
		    Promote.ban(c, id);
		}
	    }).setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    Promote.ban(c, id);
		}
	    }).setNeutralButton(R.string.dialog_btn_later, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		}
	    }).create();
    }

    public static Dialog shareWebDialog(final Context c, final int id, final Intent intent) {
	return new AlertDialog.Builder(c)
	    .setTitle(R.string.share_dialog_title)
	    .setMessage(R.string.share_dialog_message)
	    .setPositiveButton(R.string.share_dialog_btn_ok, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    c.startActivity(intent);
		    Promote.ban(c, id);
		}
	    }).setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    Promote.ban(c, id);
		}
	    }).setNeutralButton(R.string.dialog_btn_later, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		}
	    }).create();
    }

    public static Dialog shareAppDialog(final Context c, final int id, final Intent intent, int where) {
	LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View v = inflater.inflate(R.layout.dlg_share, null, false);
	Drawable d = Intents.getAppIcon(c, where);
	if (d != null) {
	    ((ImageView) v.findViewById(R.id.img_app_icon)).setImageDrawable(d);
	}

	return new AlertDialog.Builder(c)
	    .setTitle(R.string.share_dialog_title)
	    .setView(v)
	    .setPositiveButton(R.string.share_dialog_btn_ok, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    c.startActivity(intent);
		    Promote.ban(c, id);
		}
	    }).setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    Promote.ban(c, id);
		}
	    }).setNeutralButton(R.string.dialog_btn_later, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		}
	    }).create();
    }
}
