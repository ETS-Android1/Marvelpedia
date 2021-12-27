package com.project_future_2021.marvelpedia.data;

/*
*
URLs:
attribute	type	description
type	string	A text identifier for the URL.
url	string	A full URL (including scheme, domain, and path).
*
* */
public class Url {

    //A text identifier for the URL.
    private String type;

    //A full URL (including scheme, domain, and path).
    private String url;

    /**
     * No args constructor for use in serialization
     */
    public Url() {
    }

    /**
     * @param type
     * @param url
     */
    public Url(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Url{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
