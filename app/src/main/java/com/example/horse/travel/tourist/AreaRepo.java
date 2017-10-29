package com.example.horse.travel.tourist;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ekekd on 2017-10-13.
 */

public class AreaRepo {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response{
        private Header header;
        private Body body;

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    public class Header{
        private String resultCode;
        private String resultMsg;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

    public class Body{
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(int numOfRows) {
            this.numOfRows = numOfRows;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public class Items{
        private ArrayList<Item> item;

        public ArrayList<Item> getItem() {
            return item;
        }

        public void setItem(ArrayList<Item> item) {
            this.item = item;
        }
    }

    public class Item{
        private String code;
        private String name;
        private String rnum;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRnum() {
            return rnum;
        }

        public void setRnum(String rnum) {
            this.rnum = rnum;
        }
    }



    public interface areaCodeAppInterface {
        @GET("/openapi/service/rest/KorService/areaCode")
        Call<AreaRepo> get_areaCode_retrofit(@Query("ServiceKey") String ServiceKey, @Query("numOfRows") String numOfRows, @Query("pageNo") String pageNo, @Query("MobileOS") String MobileOS, @Query("MobileApp") String MobileApp,
                                             @Query("areaCode") String areaCode, @Query("_type") String type);
    }


}//TourRepo End
