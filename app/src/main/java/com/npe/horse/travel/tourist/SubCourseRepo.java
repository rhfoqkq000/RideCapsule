package com.npe.horse.travel.tourist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ekekd on 2017-10-13.
 */

public class SubCourseRepo {

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
        private String subcontentid;
        private String subdetailalt;
        private String subdetailimg;
        private String subdetailoverview;
        private String subname;
        private String subnum;


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

        public String getSubcontentid() {
            return subcontentid;
        }

        public void setSubcontentid(String subcontentid) {
            this.subcontentid = subcontentid;
        }

        public String getSubdetailalt() {
            return subdetailalt;
        }

        public void setSubdetailalt(String subdetailalt) {
            this.subdetailalt = subdetailalt;
        }

        public String getSubdetailimg() {
            return subdetailimg;
        }

        public void setSubdetailimg(String subdetailimg) {
            this.subdetailimg = subdetailimg;
        }

        public String getSubdetailoverview() {
            return subdetailoverview;
        }

        public void setSubdetailoverview(String subdetailoverview) {
            this.subdetailoverview = subdetailoverview;
        }

        public String getSubname() {
            return subname;
        }

        public void setSubname(String subname) {
            this.subname = subname;
        }

        public String getSubnum() {
            return subnum;
        }

        public void setSubnum(String subnum) {
            this.subnum = subnum;
        }
    }
    public interface SubCourseAppInterface {
        @GET("/openapi/service/rest/KorService/detailInfo")
        Call<SubCourseRepo> get_subcourse_retrofit(@Query("ServiceKey") String ServiceKey, @Query("numOfRows") String numOfRows, @Query("pageNo") String pageNo, @Query("MobileOS") String MobileOS, @Query("MobileApp") String MobileApp,
                                                   @Query("contentId") String contentId, @Query("contentTypeId") String contentTypeId,
                                                   @Query("detailYN") String detailYN, @Query("_type") String type);
    }


}//TourRepo End
