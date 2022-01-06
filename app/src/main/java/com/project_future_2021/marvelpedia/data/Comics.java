package com.project_future_2021.marvelpedia.data;

import java.util.List;

public class Comics {

    private Integer available;

    //collectionURI	string	The path to the list of full view representations of the items in this resource list.
    private String collectionURI;

    //items	Array[entity summary representations]	A list of summary views of the items in this resource list.
    private List<Items> items;

    //returned	int	The number of resources returned in this resource list (up to 20).
    private Integer returned;

    public Comics() {
    }

    public Comics(Integer available, String collectionURI, List<Items> items, Integer returned) {
        this.available = available;
        this.collectionURI = collectionURI;
        this.items = items;
        this.returned = returned;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getCollectionURI() {
        return collectionURI;
    }

    public void setCollectionURI(String collectionURI) {
        this.collectionURI = collectionURI;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "Comics{" +
                "available=" + available +
                ", collectionURI='" + collectionURI + '\'' +
                ", items=" + items +
                ", returned=" + returned +
                '}';
    }
}
