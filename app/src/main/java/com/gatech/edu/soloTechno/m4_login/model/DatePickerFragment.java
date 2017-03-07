package com.gatech.edu.soloTechno.m4_login.model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gatech.edu.soloTechno.m4_login.R;

/**
 * Created by Joshua on 3/3/2017.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /*
    * Sets a date of user input
    *
    * @params view Datepicker view
    * @params year int value of year
    * @params month int value of month
    * @params day int value of day
    * */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView tv1 = (TextView) getActivity().findViewById(R.id.pick_date_input);
        tv1.setText(view.getMonth() + 1 + "/" + view.getDayOfMonth() + "/" + view.getYear());
    }
}