package com.arn95.droidtouch;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by arnb on 3/21/16.
 */
public class DroidTouchDialog extends DialogFragment {

    private int viewId;
    private int width;
    private int height;
    Dialog dialog;

    public DroidTouchDialog(){
        //required
    }

    public static DroidTouchDialog newInstance(DialogParams params) {
        Bundle args = new Bundle();
        args.putInt("width", params.width);
        args.putInt("height", params.height);
        args.putInt("viewId", params.viewId);

        DroidTouchDialog fragment = new DroidTouchDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewId = getArguments().getInt("viewId");
        this.width = getArguments().getInt("width");
        this.height = getArguments().getInt("height");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(viewId, container, false);
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        this.dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.dialog.getWindow().setLayout(width, height);
    }
}
