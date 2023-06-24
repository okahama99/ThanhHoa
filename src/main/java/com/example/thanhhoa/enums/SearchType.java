package com.example.thanhhoa.enums;

public class SearchType {
    public enum PLANT{
        ID,
        NAME,
        HEIGHT,
        PRICE
    }

    public enum STATUS{
        ACTIVE,
        INACTIVE
    }

    public enum LOGIN_WITH_EMAIL_OR_PHONE{
        EMAIL,
        PHONE
    }

    public enum USER{
        USERNAME,
        FULLNAME,
        EMAIL,
        PHONE,
        ADDRESS,
        GENDER,
        CREATEDDATE,
        STATUS
    }

    public enum SERVICE{
        ID,
        NAME,
        DESCRIPTION,
        PRICE,
        STATUS
    }

    public enum CONTRACT{
        ID,
        TITLE,
        CREATEDDATE,
        ENDEDDATE,
        STATUS
    }

    public enum ORDER{
        ID,
        CREATEDDATE,
        RECEIVEDDATE,
        STATUS
    }

    public enum STORE{
        ID,
        STORENAME,
        ADDRESS
    }
}
