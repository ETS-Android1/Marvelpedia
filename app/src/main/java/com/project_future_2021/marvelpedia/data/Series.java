package com.project_future_2021.marvelpedia.data;

import java.util.List;

/*
*
Series:
attribute	type	description
id	int	The unique ID of the series resource.
title	string	The canonical title of the series.
description	string	A description of the series.
resourceURI	string	The canonical URL identifier for this resource.
urls	Array[Url]	A set of public web site URLs for the resource.
startYear	int	The first year of publication for the series.
endYear	int	The last year of publication for the series (conventionally, 2099 for ongoing series).
rating	string	The age-appropriateness rating for the series.
modified	Date	The date the resource was most recently modified.
thumbnail	Image	The representative image for this series.
comics	ResourceList	A resource list containing comics in this series.
stories	ResourceList	A resource list containing stories which occur in comics in this series.
events	ResourceList	A resource list containing events which take place in comics in this series.
characters	ResourceList	A resource list containing characters which appear in comics in this series.
creators	ResourceList	A resource list of creators whose work appears in comics in this series.
next	SeriesSummary	A summary representation of the series which follows this series.
previous	SeriesSummary	A summary representation of the series which preceded this series.
*
* */
public class Series {

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
    public Series() {
    }

    public Series(Integer available, String collectionURI, List<Items> items, Integer returned) {
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
        return "Series{" +
                "available=" + available +
                ", collectionURI='" + collectionURI + '\'' +
                ", items=" + items +
                ", returned=" + returned +
                '}';
    }
}
