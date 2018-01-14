package com.thecoolguy.rumaan.fileio.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.thecoolguy.rumaan.fileio.R;

/**
 * Created by rumaankhalander on 13/01/18.
 */

public class ChooseExpireDaysFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.choose_expire_fragment, null);

        builder.setView(view);
        return builder.create();
    }
}
