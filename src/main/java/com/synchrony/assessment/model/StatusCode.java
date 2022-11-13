package com.synchrony.assessment.model;

public enum StatusCode
{
    UNKNOWN_HOST("Couldn't find api.imgur.com, are you connected to the internet?", 1),
    SUCCESS("The action was successful!", 200),
    BAD_REQUEST("Upload interupted or corrupted.", 400),
    UNAUTHORIZED("Action requires Auth. Credentials are invalid.", 401),
    FORBIDDEN("You don't have access.  Possible lack of API credits.", 403),
    NOT_FOUND("The requested image or album does not exist.", 404),
    FILE_TOO_BIG("The file that you tried to upload was too big!", 413),
    UPLOAD_LIMITED("You are uploading to quickly! You've been rate limited.", 429),
    INTERNAL_SERVER_ERROR("Imgur unexpected internal server error. Not our fault.", 500),
    SERVICE_UNAVAILABLE("Imgur is unavailable currently.  Most likely over capacity.", 502),
    UNKNOWN_ERROR("An error occured, but we don't know what kind.", -1);

    private String description;
    private int httpCode;

    private StatusCode(String description, int httpCode)
    {
        this.description = description;
        this.httpCode = httpCode;
    }

    public static StatusCode getStatus(int code)
    {
        switch (code)
        {
            case 1:
                return UNKNOWN_HOST;
            case 200:
                return SUCCESS;
            case 400:
                return BAD_REQUEST;
            case 401:
                return UNAUTHORIZED;
            case 403:
                return FORBIDDEN;
            case 404:
                return NOT_FOUND;
            case 413:
                return FILE_TOO_BIG;
            case 429:
                return UPLOAD_LIMITED;
            case 500:
                return INTERNAL_SERVER_ERROR;
            case 502:
                return SERVICE_UNAVAILABLE;
            default:
                return UNKNOWN_ERROR;
        }
    }

    public String getDescription()
    {
        return description;
    }

    public int getHttpCode()
    {
        return httpCode;
    }

    @Override
    public String toString()
    {
        return String.format("StatusCode - %s: %s - %s: %d - %s: %s",
                "Name", super.toString(),
                "HttpCode", getHttpCode(),
                "Description", getDescription());
    }
}