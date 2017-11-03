package com.npe.horse.travel.capsule;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.npe.horse.travel.ApiClient;
import com.npe.horse.travel.R;
import com.npe.horse.travel.capsule.retrofit.InterfaceCapsule;
import com.npe.horse.travel.capsule.retrofit.CapsuleDTO;
import com.npe.horse.travel.kakao.KakaoSingleton;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by horse on 2017. 10. 16..
 */

public class ActivityCapsuleContent extends AppCompatActivity {

    private int dotsCount;
    private ImageView[] dots;
    public RequestBody requestBodyCapsuleContent;
    private ArrayList<File> imgFileArr;
    private ArrayList<RequestBody> emailArr;
    private ArrayList<EditText> editTextArr;
    private RequestBody requestBodyCapsuleDate;
    private MultipartBody.Part[] imagesParts;
    private int count = 0;

    @BindView(R.id.capsule_img_bt)
    Button capsule_img_bt;
    @BindView(R.id.capsule_viewPager)
    ViewPager capsule_viewPager;
    @BindView(R.id.sliderDots)
    LinearLayout sliderDotsPanel;
    @BindView(R.id.capsule_edit)
    EditText capsule_edit;
    @BindView(R.id.capsule_date)
    Button capsule_date;
    @BindView(R.id.add_mail_bt1)
    Button add_mail_bt1;
    @BindView(R.id.add_mail_bt2)
    Button add_mail_bt2;
    @BindView(R.id.add_mail_bt3)
    Button add_mail_bt3;
    @BindView(R.id.add_mail_bt4)
    Button add_mail_bt4;
    @BindView(R.id.add_mail_bt5)
    Button add_mail_bt5;
    @BindView(R.id.together_mail1)
    EditText together_mail1;
    @BindView(R.id.together_mail2)
    EditText together_mail2;
    @BindView(R.id.together_mail3)
    EditText together_mail3;
    @BindView(R.id.together_mail4)
    EditText together_mail4;
    @BindView(R.id.together_mail5)
    EditText together_mail5;

    //    이미지 선택 버튼
    @OnClick(R.id.capsule_img_bt)
    void img_select() {
        FishBun.with(ActivityCapsuleContent.this)
                .MultiPageMode()
                .setMaxCount(10)
                .setMinCount(1)
                .setPickerSpanCount(5)
                .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                .setActionBarTitleColor(Color.parseColor("#000000"))
                .setAlbumSpanCount(2, 3)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(true)
                .setAllViewTitle("All")
                .setActionBarTitle("사진선택")
                .textOnNothingSelected("Please select one or more!")
                .startAlbum();
    }

    @OnClick(R.id.add_mail_bt1)
    void add_mail_bt1() {
        add_mail_bt1.setVisibility(View.GONE);
        together_mail1.setVisibility(View.VISIBLE);
        add_mail_bt2.setVisibility(View.VISIBLE);
        count += 1;
    }

    @OnClick(R.id.add_mail_bt2)
    void add_mail_bt2() {
        add_mail_bt2.setVisibility(View.GONE);
        together_mail2.setVisibility(View.VISIBLE);
        add_mail_bt3.setVisibility(View.VISIBLE);
        count += 1;
    }

    @OnClick(R.id.add_mail_bt3)
    void add_mail_bt3() {
        add_mail_bt3.setVisibility(View.GONE);
        together_mail3.setVisibility(View.VISIBLE);
        add_mail_bt4.setVisibility(View.VISIBLE);
        count += 1;
    }

    @OnClick(R.id.add_mail_bt4)
    void add_mail_bt4() {
        add_mail_bt4.setVisibility(View.GONE);
        together_mail4.setVisibility(View.VISIBLE);
        add_mail_bt5.setVisibility(View.VISIBLE);
        count += 1;
    }
    @OnClick(R.id.add_mail_bt5)
    void add_mail_bt5() {
        add_mail_bt5.setVisibility(View.GONE);
        together_mail5.setVisibility(View.VISIBLE);
        count += 1;
    }


    //    전송받길 원하는 날짜 선택
    @OnClick(R.id.capsule_date)
    void dateClick() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(ActivityCapsuleContent.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        capsule_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        dialog.show();
    }

    //    보내기 버튼 클릭
    @OnClick(R.id.capsule_send)
    void sendClick() {

        emailArr = new ArrayList<>();

        try {
            if (capsule_viewPager.getAdapter() == null ||
                    capsule_edit.getText().toString().replace(" ", "").equals("") ||
                    together_mail1.getText().toString().replace(" ", "").equals("") ||
                    capsule_date.getText().equals("추억 받을 날짜 선택")) {
                Log.d("SEND", "NOT PASS");
                Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_LONG).show();
            } else {
                Log.d("SEND", "PASS");
            //        eidttext내용 저장
            requestBodyCapsuleContent = RequestBody.create(MediaType.parse("text/plain"), capsule_edit.getText().toString());
//            이메일저장
            for (int i = 0; i < count; i++) {
                if (editTextArr.get(i).getText().toString().replace(" ", "").equals("")) {
                    break;
                } else {
                    RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("text/plain"), editTextArr.get(i).getText().toString());
                    emailArr.add(requestBodyEmail);
                }
            }
//            이미지저장
            imagesParts = new MultipartBody.Part[imgFileArr.size()];

            for (int i = 0; i < imgFileArr.size(); i++) {
                File file = imgFileArr.get(i);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                imagesParts[i] = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
            }
//            날짜저장
            requestBodyCapsuleDate = RequestBody.create(MediaType.parse("text/plain"), capsule_date.getText().toString());
            getJson(requestBodyCapsuleContent, requestBodyCapsuleDate, imagesParts,emailArr);

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("추억 저장");

                alert.setMessage("에러가 발생 하였습니다.");

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.show();
            }
        }catch (Exception e){
            Log.e("SEND",e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //FishBun에서 가져온 이미지 uri 저장
                    ArrayList<Uri> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Log.e("============", path.get(0).toString());
                    imgFileArr = new ArrayList<>();
                    SingletonCapsule.getInstance().setLength(path.size());
                    for (int i = 0; i < path.size(); i++) {
                        try {
//                            images에 갤러리 사진 url넣기
//                            SingletonCapsule.getInstance().setUri(path);
//                            Log.d("================", ""+images[0]);
                            File file = new File(getPath(path.get(i)));
//                            compressor = 이미지 용량 줄여줌 화질이나 사이즈 변화가 얼마나 있는지는 아직 확인안함
                            File compressedImageFile = new Compressor(this).compressToFile(file);
//                            Log.e("AFTER RESIZING SIZE OF FILE"+i, String.valueOf(compressedImageFile.length()/1024));
                            imgFileArr.add(compressedImageFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,path);
                    capsule_viewPager.setAdapter(viewPagerAdapter);

                    dotsCount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotsCount];

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i] = new ImageView(this);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8, 0, 8, 0);
                        sliderDotsPanel.addView(dots[i], params);
                    }

                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    capsule_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < dotsCount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                            }
                            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    capsule_viewPager.setVisibility(View.VISIBLE);
                    sliderDotsPanel.setVisibility(View.VISIBLE);
                    capsule_img_bt.setVisibility(View.GONE);

                    break;
                }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = null;
        try {
            if (uri == null) {
                return null;
            }
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            Log.e("GET_PATH", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return uri.getPath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_content);
        ButterKnife.bind(this);

        editTextArr = new ArrayList<>();
        editTextArr.add(together_mail1);
        editTextArr.add(together_mail2);
        editTextArr.add(together_mail3);
        editTextArr.add(together_mail4);
        editTextArr.add(together_mail5);
    }

    void getJson(RequestBody requestBodyCapsuleContent, RequestBody requestBodyCapsuleDate, MultipartBody.Part[] imagesParts, ArrayList<RequestBody> emailArr) {
        InterfaceCapsule uploadImage = ApiClient.getClient().create(InterfaceCapsule.class);
<<<<<<< HEAD
        MultipartBody.Part[] imagesParts = new MultipartBody.Part[imgFileArr.size()];
        for(int i = 0; i < imgFileArr.size(); i++){
            try {
                File file = imgFileArr.get(i);
                File compressedImageFile = new Compressor(this).setQuality(75).compressToFile(file);
//                  Log.e("AFTER RESIZING SIZE OF FILE"+i, String.valueOf(compressedImageFile.length()/1024));
                imgFileArr.add(compressedImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int user_id = KakaoSingleton.getInstance().getId();

        for (int i = 0; i < imgFileArr.size(); i++) {
            File file = imgFileArr.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            imagesParts[i] = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
        }
=======

        int user_id = KakaoSingleton.getInstance().getId();
>>>>>>> 050141a4677cd1fa510717001c10bb49e1496ada


        Call<CapsuleDTO> call = uploadImage.capsule(requestBodyCapsuleContent, requestBodyCapsuleDate, user_id, imagesParts, emailArr);
        call.enqueue(new Callback<CapsuleDTO>() {
            @Override
            public void onResponse(Call<CapsuleDTO> call, Response<CapsuleDTO> response) {
                Log.e("IMAGE UPLOAD", "Capsule Success!!" + response.body().getResult_code());
                finish();
            }

            @Override
            public void onFailure(Call<CapsuleDTO> call, Throwable t) {
                t.printStackTrace();
                Log.i("Result", t.getMessage());
            }
        });

    }
}