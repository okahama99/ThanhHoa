package com.example.thanhhoa.enums;

public class SearchType {
    public enum PLANT{
        ID,
        NAME,
        HEIGHT
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
        ID,
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

    public enum CONTRACT_DETAIL{
        ID,
        STARTDATE,
        ENDDATE,
        TIMEWORKING,
        TOTALPRICE,
        NOTE
    }

    public enum ORDER{
        ID,
        CREATEDDATE,
        RECEIVEDDATE,
        PROGRESSSTATUS
    }

    public enum STORE{
        ID,
        STORENAME,
        ADDRESS
    }

    public enum ORDER_STATUS{
        PACKAGING,
        DELIVERING,
        RECEIVED
    }

    public enum PSP{
        ID,
        POTSIZE,
        PRICEPERPLANT,
        STATUS
    }

    public enum WORKING_DATE{
        ID,
        WORKINGDATE
    }

    public enum CATEGORY{
        ID,
        NAME
    }

    public enum REPORT{
        ID,
        DESCRIPTION,
        REASON,
        CREATEDDATE,
        STATUS
    }

    public enum SERVICE_PRICE{
        ID,
        PRICE,
        APPLYDATE,
        STATUS
    }

    public enum STORE_PLANT_RECORD{
        ID,
        AMOUNT,
        IMPORTDATE,
        REASON
    }
}
