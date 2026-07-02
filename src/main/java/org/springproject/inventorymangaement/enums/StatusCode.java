package org.springproject.inventorymangaement.enums;

public enum StatusCode {
    SUCCESS(200)  ,INVALIDINPUT(400) ,NOTFOUND(404) , ERROR(500);

    private int code;
    StatusCode(int code){
            this.code =code;
    }

    public int getCode(){
        return code;
    }

}
