package com.arn95.droidtouch;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by arnb on 3/21/16.
 */
public class DroidTouch implements View.OnTouchListener{

    private HashMap<String, DroidTouchBundle> dialogBundleHashMap;
    private FragmentManager fragmentManager;
    private Vibrator vibrator;
    private boolean vibrator_enabled;
    private Context current_context;
    private DroidTouchBundle activeDialogBundle;

    public DroidTouch(Context c, FragmentManager manager){
        this.fragmentManager = manager;
        vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
        current_context = c;
        vibrator_enabled = true;
        dialogBundleHashMap = new HashMap<>();
    }

    public boolean addDialog(String tag, View viewToTouch, int viewToInflateId, Intent activityIntent){
        if (!dialogBundleHashMap.containsKey(tag)) {
            DialogParams dialogParams = new DialogParams(viewToInflateId,viewToTouch.getId(),1000,1000);
            DroidTouchDialog dialog = DroidTouchDialog.newInstance(dialogParams);
            DroidTouchBundle touchBundle = new DroidTouchBundle(dialog, tag, dialogParams,activityIntent);
            dialogBundleHashMap.put(tag, touchBundle);
            return true;
        }
        else
            return false;
    }

    public void setVibration(boolean val){
        vibrator_enabled = val;
    }


    private void showDialog(String tag){
        if (vibrator_enabled)
            vibrator.vibrate(30);
        if (dialogBundleHashMap.containsKey(tag)) {
            DroidTouchBundle bundle = dialogBundleHashMap.get(tag);
            bundle.dialog.show(fragmentManager,tag);
        } else {
            Log.e("DroidTouch", "Dialog attempted to show is not created");
        }
    }


    private void dismissDialog(String tag){
        DroidTouchDialog droidTouchDialog = (DroidTouchDialog) fragmentManager.findFragmentByTag(tag);
        if (droidTouchDialog != null)
            droidTouchDialog.dismiss();
        else {
            Log.e("DroidTouch", "Dialog attempted to dismiss does not exist");
        }
    }

    public boolean isShown(String tag){
        if (fragmentManager.findFragmentByTag(tag) == null)
            return false;
        else
            return true;
    }

    public void changeDialogLayout(String tag, int viewToInflate){
        DialogParams oldDialogParams = dialogBundleHashMap.get(tag).params;
        Intent oldIntent = dialogBundleHashMap.get(tag).activityIntent;

        DialogParams dialogParams = new DialogParams(oldDialogParams.viewTouchedId,viewToInflate,oldDialogParams.width,oldDialogParams.height);
        DroidTouchDialog dialog = DroidTouchDialog.newInstance(dialogParams);
        DroidTouchBundle droidTouchBundle = new DroidTouchBundle(dialog,tag, dialogParams, oldIntent);

        dialogBundleHashMap.remove(tag);
        dialogBundleHashMap.put(tag, droidTouchBundle);
    }

    public void changeDialogWidth(String tag, int width){
        DialogParams oldDialogParams = dialogBundleHashMap.get(tag).params;
        Intent oldIntent = dialogBundleHashMap.get(tag).activityIntent;

        DialogParams dialogParams = new DialogParams(oldDialogParams.viewTouchedId,oldDialogParams.viewId,width,oldDialogParams.height);
        DroidTouchDialog dialog = DroidTouchDialog.newInstance(dialogParams);
        DroidTouchBundle droidTouchBundle = new DroidTouchBundle(dialog,tag, dialogParams, oldIntent);

        dialogBundleHashMap.remove(tag);
        dialogBundleHashMap.put(tag, droidTouchBundle);
    }

    public void changeDialogHeight(String tag, int height){
        DialogParams oldDialogParams = dialogBundleHashMap.get(tag).params;
        Intent oldIntent = dialogBundleHashMap.get(tag).activityIntent;

        DialogParams dialogParams = new DialogParams(oldDialogParams.viewTouchedId,oldDialogParams.viewId, oldDialogParams.width, height);
        DroidTouchDialog dialog = DroidTouchDialog.newInstance(dialogParams);
        DroidTouchBundle droidTouchBundle = new DroidTouchBundle(dialog,tag,dialogParams,oldIntent);

        dialogBundleHashMap.remove(tag);
        dialogBundleHashMap.put(tag, droidTouchBundle);
    }

    public void changeDialogIntent(String tag, Intent newIntent){
        DialogParams oldDialogParams = dialogBundleHashMap.get(tag).params;

        DialogParams dialogParams = new DialogParams(oldDialogParams.viewTouchedId,oldDialogParams.viewId,oldDialogParams.width,oldDialogParams.height);
        DroidTouchDialog dialog = DroidTouchDialog.newInstance(dialogParams);
        DroidTouchBundle droidTouchBundle = new DroidTouchBundle(dialog,tag,dialogParams,newIntent);

        dialogBundleHashMap.remove(tag);
        dialogBundleHashMap.put(tag, droidTouchBundle);
    }

    private DroidTouchBundle getActiveDialogBundle() {
        Collection<DroidTouchBundle> collection = dialogBundleHashMap.values();
        Iterator<DroidTouchBundle> iterator = collection.iterator();
        DroidTouchBundle bundle = null;
        while (iterator.hasNext()){
            bundle = iterator.next();
            if (bundle.dialog.isVisible()){
                return bundle;
            }
        }
        return null;
    }

    private boolean startActivity(String tag){
        if (dialogBundleHashMap.containsKey(tag)) {
            DroidTouchBundle bundle = dialogBundleHashMap.get(tag);
            current_context.startActivity(bundle.activityIntent);
            bundle.isActivityStarted = true;
            return true;
        }
        else
            return false;
    }

    private DroidTouchBundle getBundleByViewTouchedId(int viewId){
        for (DroidTouchBundle bundle : dialogBundleHashMap.values()) {
            if (bundle.params.viewTouchedId == viewId){
                return bundle;
            }
        }
        return null;
    }

    public void setCurrentActivityState(String tag, boolean state){
        dialogBundleHashMap.get(tag).isActivityStarted = !state;
        if (activeDialogBundle != null && activeDialogBundle.dialogTag.equals(tag)) {
                activeDialogBundle.isActivityStarted = !state;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //getting the bundle belonging to the current view id
         activeDialogBundle = getBundleByViewTouchedId(v.getId());
        if (activeDialogBundle != null) {
            String current_tag = activeDialogBundle.dialogTag;
            if (!activeDialogBundle.dialog.isVisible()) { //dialog not showing
                if (event.getPressure() > 0.7 && !activeDialogBundle.isActivityStarted) {
                    //show dialog
                    if (!activeDialogBundle.dialog.isAdded())
                        showDialog(current_tag);
                }
            }
            else { //dialog showing

                if (event.getPressure() >= 1) {
                    if (!activeDialogBundle.isActivityStarted) {
                        //open activity fullscreen
                        vibrator.vibrate(30);
                        dismissDialog(current_tag);
                        startActivity(current_tag);
                    }
                } else if (event.getPressure() < 0.7 || event.getAction() == MotionEvent.ACTION_UP) {
                    //dismiss dialog
                    dismissDialog(current_tag);
                }
            }
            return true;
        }
        return false;
    }
}

class DroidTouchBundle {
    DialogParams params;
    Intent activityIntent;
    DroidTouchDialog dialog;
    String dialogTag;
    boolean isActivityStarted = false;

    public DroidTouchBundle(DroidTouchDialog droidTouchDialog, String dialogTag, DialogParams params, Intent activityIntent){
        this.params = params;
        this.activityIntent = activityIntent;
        this.dialog = droidTouchDialog;
        this.dialogTag = dialogTag;
    }
}

class DialogParams {
    int width;
    int height;
    int viewId;
    int viewTouchedId;

    public DialogParams(int viewId, int viewTouchedId, int width, int height) {
        this.width = width;
        this.height = height;
        this.viewId = viewId;
        this.viewTouchedId = viewTouchedId;
    }
}
