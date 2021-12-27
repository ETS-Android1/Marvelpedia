package com.project_future_2021.marvelpedia.data;

import java.util.List;

/*
*
Events:
attribute	type	description
id	int	The unique ID of the event resource.
title	string	The title of the event.
description	string	A description of the event.
resourceURI	string	The canonical URL identifier for this resource.
urls	Array[Url]	A set of public web site URLs for the event.
modified	Date	The date the resource was most recently modified.
start	Date	The date of publication of the first issue in this event.
end	Date	The date of publication of the last issue in this event.
thumbnail	Image	The representative image for this event.
comics	ResourceList	A resource list containing the comics in this event.
stories	ResourceList	A resource list containing the stories in this event.
series	ResourceList	A resource list containing the series in this event.
characters	ResourceList	A resource list containing the characters which appear in this event.
creators	ResourceList	A resource list containing creators whose work appears in this event.
next	EventSummary	A summary representation of the event which follows this event.
previous	EventSummary	A summary representation of the event which preceded this event.
*
* */
public class Events {

    private Integer available;

    //collectionURI	string	The path to the list of full view representations of the items in this resource list.
    private String collectionURI;

    //items	Array[entity summary representations]	A list of summary views of the items in this resource list.
    private List<Items> items = null;

    //returned	int	The number of resources returned in this resource list (up to 20).
    private Integer returned;

    /**
     * No args constructor for use in serialization
     */
    public Events() {
    }

    public Events(Integer available, String collectionURI, List<Items> items, Integer returned) {
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

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
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

    @Override
    public String toString() {
        return "Events{" +
                "available=" + available +
                ", collectionURI='" + collectionURI + '\'' +
                ", items=" + items +
                ", returned=" + returned +
                '}';
    }
}
