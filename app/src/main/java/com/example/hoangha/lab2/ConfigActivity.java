package com.example.hoangha.lab2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private Button btnConfig;
    private SearchRequest data;
    private Spinner spnOrder;
    private CheckBox cbArt, cbFashion, cbSport;
    private DatePicker dpDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        data = (SearchRequest) getIntent().getParcelableExtra("data");

        btnConfig = (Button) findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(this);

        spnOrder = (Spinner) findViewById(R.id.spnOrder);
        spnOrder.setOnItemSelectedListener(this);

        cbArt = (CheckBox) findViewById(R.id.cbArt);
        cbFashion = (CheckBox) findViewById(R.id.cbFashion);
        cbSport = (CheckBox) findViewById(R.id.cbSport);
        cbArt.setOnCheckedChangeListener(this);
        cbFashion.setOnCheckedChangeListener(this);
        cbSport.setOnCheckedChangeListener(this);

        Calendar calendar = Calendar.getInstance();
        dpDate = (DatePicker) findViewById(R.id.dpDate);


        int year, month, day, date;
        if(data.getBeginDate() != null) {
            date = Integer.valueOf(data.getBeginDate());
            day = date % 100;
            month = (date / 100) % 100;
            year = date / 10000;
            dpDate.updateDate(year, month, day);
        } else {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        dpDate.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String s_month;
                if ((monthOfYear+1)/10 == 0)
                    s_month = "0"+(monthOfYear+1);
                else
                    s_month = ""+(monthOfYear+1);
                String date = "" + year + s_month + dayOfMonth;
                ToastShow(date);
                data.setBeginDate(date);
            }
        });

        getdata();

    }

    private void getdata() {

/*       if(data.getBeginDate() != null) {
           int year, month, day, date;
           date = Integer.valueOf(data.getBeginDate());
           day = date % 100;
           month = (date / 100) % 100;
           year = date / 10000;
           dpDate.updateDate(year, month, day);
       }*/

        if (data.getOrder().equals("Newest"))
            spnOrder.setSelection(0);
        else
            spnOrder.setSelection(1);

        cbArt.setChecked(data.isHasArts());
        cbFashion.setChecked(data.isHasFashionAndStyle());
        cbSport.setChecked(data.isHasSports());
    }

    private void ToastShow(String display) {
        Toast.makeText(this, display, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfig:
                Intent i = new Intent();
                //data.setBeginDate("20160110");
                //data.setOrder("newest");

                i.putExtra("dataR", data);
                setResult(RESULT_OK, i);

                finish();
                break;
            default:
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        data.setOrder(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cbArt:
                if (isChecked)
                    data.setHasArts(true);
                else
                    data.setHasArts(false);
                break;
            case R.id.cbFashion:
                if (isChecked)
                    data.setHasFashionAndStyle(true);
                else
                    data.setHasFashionAndStyle(false);
                break;
            case R.id.cbSport:
                if (isChecked)
                    data.setHasSports(true);
                else
                    data.setHasSports(false);
                break;
            default:
        }
    }
}
