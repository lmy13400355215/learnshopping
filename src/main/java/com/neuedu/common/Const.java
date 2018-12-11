package com.neuedu.common;

public class Const {

    public static final Integer SUCCESS_CODE=0;
    public static final Integer ERROR_CODE=1;


    public static final Integer USER_ROLE_CUSTOMER=0;//普通用户
    public static final Integer USER_ROLE_ADMIN=1;//管理员

    public static final String CURRENTUSER="CURRENTUSER";

    public static final String USERNAME="username";
    public static final String EMAIL="email";

    public enum ProductStatusEnum{
        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;


        ProductStatusEnum(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        private  int  status;
        private String msg;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
