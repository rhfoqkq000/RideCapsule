package com.example.horse.travel.capsule;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.horse.travel.R;
import com.example.horse.travel.capsule.retrofit.InterfaceCapsule;
import com.example.horse.travel.capsule.retrofit.MasterCapsule;
import com.example.horse.travel.capsule.retrofit.ResultCapsule;
import com.example.horse.travel.inn.FragmentInn;
import com.example.horse.travel.mypage.FragmentMypage;
import com.example.horse.travel.sns.FragmentSns;
import com.example.horse.travel.tourist.FragmentTourist;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityCapsule extends AppCompatActivity {

    @BindView(R.id.capsule_edit)
    EditText capsule_edit;
    @BindView(R.id.capsule_date)
    Button capsule_date;

//    전송받길 원하는 날짜 선택
    @OnClick(R.id.capsule_date)
    void dateClick(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(ActivityCapsule.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        capsule_date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        dialog.show();
    }

//    보내기 버튼 클릭
    @OnClick(R.id.capsule_send)
    void sendClick(){
        //        eidttext내용 저장
        capsuleContent = capsule_edit.getText().toString();
        getJson();
    }

    public String capsuleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule);
        ButterKnife.bind(this);
    }

    void getJson(){
        Retrofit client = new Retrofit.Builder().baseUrl(getString(R.string.retrofit_url))
                .addConverterFactory(GsonConverterFactory.create()).build();
        InterfaceCapsule json = client.create(InterfaceCapsule.class);
        Call<MasterCapsule> call =
                json.capsule(capsuleContent, capsule_date.getText().toString(), 9);
        call.enqueue(new Callback<MasterCapsule>() {
            @Override
            public void onResponse(Call<MasterCapsule> call, Response<MasterCapsule> response) {
                Log.i("Result","success!!");
            }

            @Override
            public void onFailure(Call<MasterCapsule> call, Throwable t) {
                t.printStackTrace();
                Log.i("Result","fail!!");
            }
        });
    }
}