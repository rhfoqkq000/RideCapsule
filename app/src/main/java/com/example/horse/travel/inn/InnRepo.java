package com.example.horse.travel.inn;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ekekd on 2017-10-21.
 */

public class InnRepo {

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
        private String addr1;
        private String addr2;
        private int areacode;
        private String cat1;
        private String cat2;
        private String cat3;
        private int contentid;
        private int contenttypeid;
        private long createdtime;
        private String firstimage;
        private String firstimage2;
        private Double mapx;
        private Double mapy;
        private long modifiedtime;
        private int readcount;
        private int sigungucode;
        private String tel;
        private String title;

        public String getAddr1() {
            return addr1;
        }
        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr2() {
            return addr2;
        }
        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public int getAreacode() {
            return areacode;
        }
        public void setAreacode(int areacode) {
            this.areacode = areacode;
        }

        public String getCat1() {
            return cat1;
        }
        public void setCat1(String cat1) {
            this.cat1 = cat1;
        }

        public String getCat2() {
            return cat2;
        }
        public void setCat2(String cat2) {
            this.cat2 = cat2;
        }

        public String getCat3() {
            return cat3;
        }
        public void setCat3(String cat3) {
            this.cat3 = cat3;
        }

        public int getContentid() {
            return contentid;
        }
        public void setContentid(int contentid) {
            this.contentid = contentid;
        }

        public int getContenttypeid() {
            return contenttypeid;
        }
        public void setContenttypeid(int contenttypeid) {
            this.contenttypeid = contenttypeid;
        }

        public long getCreatedtime() {
            return createdtime;
        }
        public void setCreatedtime(long createdtime) {
            this.createdtime = createdtime;
        }

        public String getFirstimage() {
            return firstimage;
        }
        public void setFirstimage(String firstimage) {
            this.firstimage = firstimage;
        }

        public String getFirstimage2() {
            return firstimage2;
        }
        public void setFirstimage2(String firstimage2) {
            this.firstimage2 = firstimage2;
        }

        public Double getMapx() {
            return mapx;
        }
        public void setMapx(Double mapx) {
            this.mapx = mapx;
        }

        public Double getMapy() {
            return mapy;
        }
        public void setMapy(Double mapy) {
            this.mapy = mapy;
        }

        public long getModifiedtime() {
            return modifiedtime;
        }
        public void setModifiedtime(long modifiedtime) {
            this.modifiedtime = modifiedtime;
        }

        public int getReadcount() {
            return readcount;
        }
        public void setReadcount(int readcount) {
            this.readcount = readcount;
        }

        public int getSigungucode() {
            return sigungucode;
        }
        public void setSigungucode(int sigungucode) {
            this.sigungucode = sigungucode;
        }

        public String getTel() {
            return tel;
        }
        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

    }
    public interface InnAppInterface {
        @GET("/openapi/service/rest/KorService/searchStay")
        Call<InnRepo> get_inn_retrofit(@Query("ServiceKey") String ServiceKey, @Query("numOfRows") String numOfRows, @Query("pageNo") String pageNo, @Query("MobileOS") String MobileOS, @Query("MobileApp") String MobileApp,
                                       @Query("arrange") String arrange, @Query("listYN") String listYN, @Query("areaCode") String areaCode, @Query("contentTypeId") String contentTypeId,
                                       @Query("_type") String type);
        @GET("/openapi/service/rest/KorService/searchStay")
        Call<InnRepo> get_inn_retrofit(@Query("ServiceKey") String ServiceKey, @Query("numOfRows") String numOfRows, @Query("pageNo") String pageNo, @Query("MobileOS") String MobileOS, @Query("MobileApp") String MobileApp,
                                       @Query("arrange") String arrange, @Query("listYN") String listYN, @Query("areaCode") String areaCode, @Query("sigunguCode") String sigunguCode, @Query("contentTypeId") String contentTypeId,
                                       @Query("_type") String type);
    }
}
