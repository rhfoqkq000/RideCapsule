package com.example.horse.travel.tourist;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ekekd on 2017-10-13.
 */

public class TourOverviewRepo {

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
        private String numOfRows;
        private String pageNo;
        private String totalCount;

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }

        public String getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(String numOfRows) {
            this.numOfRows = numOfRows;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }
    }

    public class Items{
        private Item item;

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }
    }

    public class Item{
        private String contentid;
        private String contenttypeid;
        private long createdtime;
        private Double mapx;
        private Double mapy;
        private String mlevel;
        private String overview;

        public String getContentid() {
            return contentid;
        }

        public void setContentid(String contentid) {
            this.contentid = contentid;
        }

        public String getContenttypeid() {
            return contenttypeid;
        }

        public void setContenttypeid(String contenttypeid) {
            this.contenttypeid = contenttypeid;
        }

        public long getCreatedtime() {
            return createdtime;
        }

        public void setCreatedtime(long createdtime) {
            this.createdtime = createdtime;
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

        public String getMlevel() {
            return mlevel;
        }

        public void setMlevel(String mlevel) {
            this.mlevel = mlevel;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }
    }
    public interface TourOverviewAppInterface {
        @GET("/openapi/service/rest/KorService/detailCommon")
        Call<TourOverviewRepo> get_overview_retrofit(@Query("ServiceKey") String ServiceKey, @Query("numOfRows") String numOfRows, @Query("pageNo") String pageNo, @Query("MobileOS") String MobileOS, @Query("MobileApp") String MobileApp,
                                                 @Query("contentId") String contentId, @Query("contentTypeId") String contentTypeId,
                                                 @Query("mapinfoYN") String mapinfoYN, @Query("overviewYN") String overviewYN ,@Query("_type") String type);
    }


}//TourRepo End
