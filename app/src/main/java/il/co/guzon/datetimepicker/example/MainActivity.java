package il.co.guzon.datetimepicker.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import il.co.guzon.datetimepicker.E_DateFormat;
import il.co.guzon.datetimepicker.E_DateTimeType;
import il.co.guzon.datetimepicker.PickerDialog;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, PickerDialog.DateDialogListener {
    public static SimpleDateFormat format_HH_mm = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat format_dd_MM_yyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    public static SimpleDateFormat format_MM_dd_yyyy = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    public static SimpleDateFormat format_dd_MM_yyyy_HH_mm = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat format_MM_dd_yyyy_HH_mm = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH);

    static Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((RadioButton) findViewById(R.id.cb_mdy)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.cb_dmy)).setOnCheckedChangeListener(this);
    }

public void OnDateTimeClick(View view) {
    PickerDialog.show(this,E_DateTimeType.DateTime,data.dmy ? E_DateFormat.DayMonthYear :  E_DateFormat.MonthDayYear ,data.dateTime,R.string.selectDateTime,this);
}

    public void OnDateClick(View view) {
        PickerDialog.show(this,E_DateTimeType.DateOnly,data.dmy ? E_DateFormat.DayMonthYear :  E_DateFormat.MonthDayYear ,data.date,R.string.selectDate,this);
    }

    public void OnTimeClick(View view) {
        PickerDialog.show(this,E_DateTimeType.TimeOnly,data.dmy ? E_DateFormat.DayMonthYear :  E_DateFormat.MonthDayYear ,data.time,R.string.selectTime,this);
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean change) {
        data.dmy = change && compoundButton.getId() == R.id.cb_dmy;
        data.mdy = change && compoundButton.getId() == R.id.cb_mdy;
    }

    @Override
    public void onSaveDate(Date date,E_DateTimeType type) {
        switch (type){
            case DateTime:
                data.dateTime=date;
                break;
            case DateOnly:
                data.date=date;
                break;
            case TimeOnly:
                data.time=date;
                break;
        }

        initUI();
    }

    @Override
    public void onCancel() {

    }

    class Data {
        Date dateTime;
        Date date;
        Date time;
        boolean mdy;
        boolean dmy=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data == null) {
            data = new Data();
        }
        initUI();
    }

    private void initUI() {
        ((RadioButton) findViewById(R.id.cb_mdy)).setChecked(data.mdy);
        ((RadioButton) findViewById(R.id.cb_dmy)).setChecked(data.dmy);

        ((TextView) findViewById(R.id.tv_date)).setText(getString(data.date, E_DateTimeType.DateOnly));
        ((TextView) findViewById(R.id.tv_dateTime)).setText(getString(data.dateTime, E_DateTimeType.DateTime));
        ((TextView) findViewById(R.id.tv_time)).setText(getString(data.time, E_DateTimeType.TimeOnly));

    }

    public String getString(Date date, E_DateTimeType type) {
        if (date == null) {
            return "";
        }

        if (type == E_DateTimeType.TimeOnly) {
            return format_HH_mm.format(date);
        }

        SimpleDateFormat format;
        if (type == E_DateTimeType.DateOnly) {
            format = data.dmy ? format_dd_MM_yyyy : format_MM_dd_yyyy;
        } else {
            format = data.dmy ? format_dd_MM_yyyy_HH_mm : format_MM_dd_yyyy_HH_mm;
        }

        return format.format(date);
    }
}
