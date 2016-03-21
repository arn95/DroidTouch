package com.arn95.droidtouch;

import android.support.v4.app.FragmentManager;

import java.util.HashMap;

/**
 * Created by arnb on 3/21/16.
 */
public class DroidTouch {

    public static int ACTION_SHOW = 1;
    public static int ACTION_DISMISS = 0;

    private HashMap<String, DroidTouchDialog> dialogHashMap;
    private HashMap<String, DialogParams> dialogDimensionsHashMap;
    private FragmentManager fragmentManager;

    public DroidTouch(FragmentManager manager){
        dialogHashMap = new HashMap<>();
        dialogDimensionsHashMap = new HashMap<>();
        this.fragmentManager = manager;
    }

    public DroidTouchDialog createDialog(String tag, int viewId, int width, int height){
        if (!dialogHashMap.containsKey(tag)) {
            DroidTouchDialog dialog = DroidTouchDialog.newInstance(viewId, width, height);
            dialogDimensionsHashMap.put(tag, new DialogParams(viewId, width, height));
            dialogHashMap.put(tag, dialog);
            return dialog;
        }
        else
            return null;
    }


    public void showDialog(String tag){
        if (dialogHashMap.containsKey(tag))
            dialogHashMap.get(tag).show(fragmentManager, tag);
    }


    public void dismissDialog(String tag){
        if (fragmentManager.findFragmentByTag(tag) != null) {
            DroidTouchDialog droidTouchDialog = (DroidTouchDialog) fragmentManager.findFragmentByTag(tag);
            droidTouchDialog.dismiss();
            dialogDimensionsHashMap.remove(tag);
            dialogHashMap.remove(tag);
        }
    }

    public boolean isShown(String tag){
        if (fragmentManager.findFragmentByTag(tag) == null)
            return false;
        else
            return true;
    }

    public void changeDialogLayout(String tag, int viewId){
        DialogParams dialogParams = dialogDimensionsHashMap.get(tag);
        DroidTouchDialog dialog = DroidTouchDialog.newInstance(viewId, dialogParams.width, dialogParams.height);
        dialogHashMap.remove(tag);
        dialogHashMap.put(tag, dialog);
    }

    public void changeDialogWidth(String tag, int width){
        DialogParams dialogParams = dialogDimensionsHashMap.get(tag);
        DroidTouchDialog dialog = DroidTouchDialog.newInstance(dialogParams.viewId, width, dialogParams.height);
        dialogHashMap.remove(tag);
        dialogHashMap.put(tag, dialog);
    }

    public void changeDialogHeight(String tag, int height){
        DialogParams dialogParams = dialogDimensionsHashMap.get(tag);
        DroidTouchDialog dialog = DroidTouchDialog.newInstance(dialogParams.viewId, dialogParams.width, height);
        dialogHashMap.remove(tag);
        dialogHashMap.put(tag, dialog);
    }

}

class DialogParams {
    int width;
    int height;
    int viewId;

    public DialogParams(int viewId, int width, int height) {
        this.width = width;
        this.height = height;
        this.viewId = viewId;
    }
}
