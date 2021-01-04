package il.co.guzon.datetimepicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Date;


public class PickerDialog extends DialogFragment {
    DateDialogListener dialogListener;
    DateObject dateObject;
    int title;
    Views views;
    E_DateFormat dateFormat;
    e_step currentStep;
    int startYear;

    public enum e_step {year, month, day, hour, minute}

    ArrayList<String> listDays, listMonths, listYears, listHours, listMinutes;

    public interface DateDialogListener {
        void onSaveDate(Date date, E_DateTimeType type);
        void onCancel();
    }

    public PickerDialog() {
    }

    @SuppressLint("ValidFragment")
    public PickerDialog(E_DateTimeType type, Date dateTime, E_DateFormat dateFormat, int title, DateDialogListener dialogListener) {
        this.dialogListener = dialogListener;
        this.title = title;
        this.dateObject =new DateObject(type,dateTime);
        this.dateFormat = dateFormat;
        if(type == E_DateTimeType.DateOnly){
            this.currentStep =(dateFormat == E_DateFormat.DayMonthYear ? e_step.day : e_step.month);
        }else{
            this.currentStep =  e_step.minute;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.date_dialog, container, false);
        initList();
        views = new Views(v);
        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initList() {
        listDays = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            listDays.add(i + "");
        }

        listMonths = new ArrayList<>();
        for (String s : getResources().getStringArray(R.array.months)) {
            listMonths.add(s);
        }

        startYear  = DateUtils.getYear(new Date());
        listYears = new ArrayList<>();
        for (int i = startYear; i > 1899; i--) {
            listYears.add(i + "");
        }

        listHours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String s = (i < 10 ? "0" : "") + i;
            listHours.add(s);
        }

        listMinutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            String s = (i < 10 ? "0" : "") + i;
            listMinutes.add(s);
        }
    }

    class Views implements View.OnClickListener, AdapterView.OnItemClickListener {

        GridView gv_items;
        TextView btn_ok;
        TextView tv_date;
        TextView tv_day;
        TextView tv_month;
        TextView tv_year;
        TextView tv_minute;
        TextView tv_hour;
        TextView tv_explation;

        TextView btn_back;
        TextView tv_title;
        TextView[] tabs;
        public Views(View v) {
            tv_title = v.findViewById(R.id.tv_title);

            gv_items = v.findViewById(R.id.gv_items);
            btn_ok = v.findViewById(R.id.btn_ok);

            if (dateFormat == E_DateFormat.DayMonthYear) {
                tv_day = v.findViewById(R.id.tv_d1);
                tv_month = v.findViewById(R.id.tv_d2);
            } else {
                tv_day = v.findViewById(R.id.tv_d2);
                tv_month = v.findViewById(R.id.tv_d1);
            }
            tv_day.setText(R.string.day);
            tv_month.setText(R.string.month);

            tv_date = v.findViewById(R.id.tv_date);
            tv_explation = v.findViewById(R.id.tv_explation);
            tv_year = v.findViewById(R.id.tv_year);
            tv_minute = v.findViewById(R.id.tv_minute);
            tv_hour = v.findViewById(R.id.tv_hour);
            tabs = new TextView[]{tv_minute, tv_hour, tv_day, tv_month, tv_year};

            btn_back = v.findViewById(R.id.btn_back);

            btn_ok.setOnClickListener(this);
            btn_back.setOnClickListener(this);

            for (TextView tv : tabs) {
                tv.setOnClickListener(this);
            }

            gv_items.setOnItemClickListener(this);

            tv_minute.setVisibility(dateObject.type == E_DateTimeType.DateOnly ? View.GONE : View.VISIBLE);
            tv_hour.setVisibility(tv_minute.getVisibility());
            tv_month.setVisibility(dateObject.type == E_DateTimeType.TimeOnly ? View.GONE : View.VISIBLE);
            tv_day.setVisibility(tv_month.getVisibility());
            tv_year.setVisibility(tv_month.getVisibility());

            tv_minute.setTag(e_step.minute);
            tv_hour.setTag(e_step.hour);
            tv_month.setTag(e_step.month);
            tv_day.setTag(e_step.day);
            tv_year.setTag(e_step.year);


            initUI();
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.btn_ok) {
                finish();
                return;
            }

            if (id == R.id.btn_back) {
                if (dialogListener != null) {
                    dialogListener.onCancel();
                }
                dismiss();
                return;
            }

            if (view.getTag() != null) {
                setStep((e_step) view.getTag());
            }
        }

        private void finish() {
            String s = checkMissingPart();
            if (s.length() > 0) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                return;
            }

            if (dialogListener != null) {
                dialogListener.onSaveDate(dateObject.toDate(), dateObject.type);
            }

            dismiss();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (currentStep) {
                case day: {
                    dateObject.day = position + 1;
                    setStep(dateFormat == E_DateFormat.DayMonthYear ? e_step.month : e_step.year);
                    break;
                }
                case month: {
                    dateObject.month = position + 1;
                    setStep(dateFormat == E_DateFormat.DayMonthYear ? e_step.year : e_step.day);
                    break;
                }
                case year: {
                    dateObject.year = startYear - position;
                    finish();
                    return;
                }
                case minute: {
                    dateObject.minute= position;
                    setStep(e_step.hour);
                    break;
                }
                case hour: {
                    dateObject.hour = position;
                    if(dateObject.type==E_DateTimeType.TimeOnly){
                        finish();
                        return;
                    }else{
                        setStep(dateFormat == E_DateFormat.DayMonthYear ? e_step.day : e_step.month);
                    }
                    break;
                }
            }
        }


        private String checkMissingPart() {
            if (dateObject.hasData()) {
                return "";
            }

            if (dateObject.type != E_DateTimeType.TimeOnly) {
                if (dateObject.day == null) {
                    setStep(e_step.day);
                    return getString(R.string.please_select_day);
                }

                if (dateObject.month == null) {
                    setStep(e_step.month);
                    return getString(R.string.please_select_month);
                }

                if (dateObject.year == null) {
                    setStep(e_step.year);
                    return getString(R.string.please_select_year);
                }
            }


            if (dateObject.type != E_DateTimeType.DateOnly) {
                if (dateObject.minute == null) {
                    setStep(e_step.minute);
                    return getString(R.string.please_select_minute);
                }

                if (dateObject.hour == null) {
                    setStep(e_step.hour);
                    return getString(R.string.please_select_hour);
                }
            }

            return "";
        }

        private void setStep(e_step step) {
            if (currentStep == null || currentStep != step) {
                currentStep = step;
                initUI();
            }
        }

        private void initUI() {
            ArrayList<String> list = null;
            int explationResId = 0;
            int numColumns = 0;

            switch (currentStep) {
                case year:
                    explationResId = R.string.setYear;
                    numColumns = 3;
                    list = listYears;
                    break;
                case month:
                    explationResId = R.string.setMonth;
                    numColumns = 2;
                    list = listMonths;
                    break;
                case day:
                    explationResId = R.string.setDay;
                    numColumns = 6;
                    list = listDays;
                    break;
                case hour:
                    explationResId = R.string.setHour;
                    numColumns = 6;
                    list = listHours;
                    break;
                case minute:
                    explationResId = R.string.setMinute;
                    numColumns = 4;
                    list = listMinutes;
                    break;
            }


            tv_explation.setText(explationResId);
            DateAdapter adapter = new DateAdapter(getActivity(), list);
            gv_items.setNumColumns(numColumns);
            gv_items.setAdapter(adapter);

            tv_date.setText(Html.fromHtml(dateObject.toHtmlString(dateFormat)));

            for(TextView tv:tabs){
                boolean selected = currentStep.ordinal() == ((e_step) tv.getTag()).ordinal();
                tv.setBackgroundResource(selected ? R.drawable.white :R.drawable.gray);
                tv.setTextColor(getColor(getActivity(), (selected ? R.color.red_dark :R.color.black)));
                tv.setTypeface(null,selected ?  Typeface.BOLD : Typeface.NORMAL );
            }

            tv_title.setText(title);
            tv_title.setVisibility(tv_title.length() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    public static int getColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resId, context.getTheme());
        } else {
            return context.getResources().getColor(resId);
        }
    }

    class DateAdapter extends ArrayAdapter<String> {
        Context context;

        public DateAdapter(Context context, ArrayList<String> objects) {
            super(context, R.layout.date_item, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                convertView = mInflater.inflate(R.layout.date_item, null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.tv_value);

            String text = getItem(position);

            tv.setText(text);

            return convertView;
        }
    }

    public static void show(FragmentActivity activity, E_DateTimeType type,  E_DateFormat dateFormat, Date date, int title, DateDialogListener dialogListener) {
        FragmentManager fm = activity.getSupportFragmentManager();
        PickerDialog dialog = new PickerDialog(type, date, dateFormat, title, dialogListener);
        dialog.show(fm, "DateDialog");
    }
}
