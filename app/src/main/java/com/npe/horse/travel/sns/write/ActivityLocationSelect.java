package com.npe.horse.travel.sns.write;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.MainActivity;
import com.npe.horse.travel.R;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityLocationSelect extends AppCompatActivity {

    @BindView(R.id.location_et)
    EditText location_et;
    @BindView(R.id.location_alias_et) EditText location_alias_et;
    @BindView(R.id.location_search_et) EditText location_search_et;
    @BindView(R.id.locRe)
    RecyclerView locRe;

    SnsLocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 이용자의 지역을 입력 / 검색하는 액티비티임
        // 반드시 지역명과 상세주소가 입력되어야 ActivitySnsWrite로 이동할 수 있음
        // 지역명 상세주소 입력 후 추가 누르면 DB에 저장됨
        // 지역가져오기 누르면 GPS 혹은 네트워크 통해서 위치 받아옴 실내에서는 잘 안됨
        // 검색어 입력 후 검색 누르면 목록이 쭉 뜸.. 그거 선택하면 지역명과 상세주소에 입력됨
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);
        ButterKnife.bind(this);

        SnsLocationTextViewSingleton.getInstance().setLoaction_et(location_et);
        SnsLocationTextViewSingleton.getInstance().setLocation_alias_et(location_alias_et);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new SnsLocationAdapter();
        locRe.setLayoutManager(layoutManager);
        locRe.setAdapter(adapter);
    }

    @OnClick(R.id.get_loc_btn)
    void getLocation(){
        Toast.makeText(getApplicationContext(), "위치정보를 받아오고 있습니다. 실내에서는 오래 걸릴 수 있습니다. 잠시만 기다려주세요!", Toast.LENGTH_SHORT).show();
        SmartLocation.with(getApplicationContext()).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                Log.e("ActivityLocationSelect", "longtitude:"+location.getLongitude()+", latitude:"+location.getLatitude());
                location_alias_et.setText(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction addr", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction addr", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction addr", "Cannot get Address!");
        }
        return strAdd;
    }

    @OnClick(R.id.select_loc_btn)
    void insertLocation(){
        if(!location_et.getText().toString().equals("") && !location_alias_et.getText().toString().equals("")){
//            InterfaceSnsLocationInsert location = new Retrofit.Builder()
//                    .baseUrl("http://168.115.225.120:5000/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build().create(InterfaceSnsLocationInsert.class);
            InterfaceSnsLocationInsert location = ApiClient.getClient().create(InterfaceSnsLocationInsert.class);
            Call<SnsWriteDTO> call = location.writeLocation(location_et.getText().toString(), location_alias_et.getText().toString());
            call.enqueue(new Callback<SnsWriteDTO>() {
                @Override
                public void onResponse(Call<SnsWriteDTO> call, Response<SnsWriteDTO> response) {
                    Log.e("ActivityLocationSelect", "모얌"+response.body().getResult_code());
                    Toast.makeText(getApplicationContext(), "입력하신 지역을 추가하였습니다!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<SnsWriteDTO> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "지역명과 상세주소를 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.search_loc_btn)
    void searchLocation(){

        InterfaceSnsLocationSearch location = ApiClient.getClient().create(InterfaceSnsLocationSearch.class);
        Call<SnsLocationDTO> call = location.searchLocation(location_search_et.getText().toString());
        call.enqueue(new Callback<SnsLocationDTO>() {
            @Override
            public void onResponse(Call<SnsLocationDTO> call, Response<SnsLocationDTO> response) {
                if(response.body().getResult_code()==200){
                    List<SnsLocationInfo> resultBody = response.body().getResult_body();
//                    for(int i = 0; i < resultBody.size(); i ++){
//                        Log.e("ActivityLocationSelect", ""+resultBody.get(i).getId());
//                        Log.e("ActivityLocationSelect", resultBody.get(i).getLocation());
//                        Log.e("ActivityLocationSelect", resultBody.get(i).getLcoation_alias());
//                    }
                    adapter.addNew(resultBody);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SnsLocationDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.location_select_nextBtn)
    void locationSelectNextBtn(){
        if(!location_et.getText().toString().equals("")&&!location_alias_et.getText().toString().equals("")){
            Intent intent = new Intent(this, ActivitySnsWrite.class);
            intent.putExtra("location", location_et.getText().toString());
            intent.putExtra("location_alias", location_alias_et.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "지역명과 상세주소를 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.location_select_closeBtn)
    void locationCloseBtn(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}