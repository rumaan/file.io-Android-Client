package com.thecoolguy.rumaan.fileio.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.shawnlin.numberpicker.NumberPicker;
import com.thecoolguy.rumaan.fileio.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rumaankhalander on 13/01/18.
 */

public class ChooseExpireDaysFragment extends DialogFragment {

    private static final String TAG = "ChooseDaysFragment";
    @BindView(R.id.numberpicker)
    NumberPicker numberPicker;
    private DialogClickListener mClicklistener;

    @OnClick(R.id.btn_select_days)
    void onSelectDays() {
        mClicklistener.onDialogPositiveClick(getDialog(), this);
    }

    public int getNumberPickerValue() {
        return numberPicker.getValue();
    }

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.choose_expire_fragment, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mClicklistener = (DialogClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement the listener");
        }
    }
}
