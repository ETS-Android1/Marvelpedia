package com.project_future_2021.marvelpedia.data;

import java.util.List;

/*
*
Characters:
attribute	type	description
id	int	The unique ID of the character resource.
name	string	The name of the character.
description	string	A short bio or description of the character.
modified	Date	The date the resource was most recently modified.
resourceURI	string	The canonical URL identifier for this resource.
urls	Array[Url]	A set of public web site URLs for the resource.
image	Image	The representative image for this character.
comics	ResourceList	A resource list containing comics which feature this character.
stories	ResourceList	A resource list of stories in which this character appears.
events	ResourceList	A resource list of events in which this character appears.
series	ResourceList	A resource list of series in which this character appears.
*
* */
public class Hero {

    //The unique ID of the character resource.
    private Integer id;

    //The name of the character.
    private String name;

    //A short bio or description of the character.
    private String description;

    //Date modified
    //The date the resource was most recently modified.
    private String modified;

    //Image image
    //The representative image for this character.
    private Image thumbnail;

    //The canonical URL identifier for this resource.
    private String resourceURI;

    //A resource list containing comics which feature this character.
    private Comics comics;

    //A resource list of series in which this character appears.
    private Series series;

    //A resource list of stories in which this character appears.
    private Stories stories;

    //A resource list of events in which this character appears.
    private Events events;

    //Array[Url] urls
    //A set of public web site URLs for the resource.
    private List<Url> urls;

    /**
     * No args constructor for use in serialization
     */
    public Hero() {
    }

    /**
     * @param id
     * @param name
     * @param description
     * @param modified
     * @param thumbnail
     * @param resourceURI
     * @param comics
     * @param series
     * @param stories
     * @param events
     * @param urls
     */
    public Hero(Integer id, String name, String description, String modified, Image thumbnail, String resourceURI, Comics comics, Series series, Stories stories, Events events, List<Url> urls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.modified = modified;
        this.thumbnail = thumbnail;
        this.resourceURI = resourceURI;
        this.comics = comics;
        this.series = series;
        this.stories = stories;
        this.events = events;
        this.urls = urls;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image image) {
        this.thumbnail = image;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public Comics getComics() {
        return comics;
    }

    public void setComics(Comics comics) {
        this.comics = comics;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Stories getStories() {
        return stories;
    }

    public void setStories(Stories stories) {
        this.stories = stories;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", modified='" + modified + '\'' +
                ", image=" + thumbnail +
                ", resourceURI='" + resourceURI + '\'' +
                ", comics=" + comics +
                ", series=" + series +
                ", stories=" + stories +
                ", events=" + events +
                ", urls=" + urls +
                '}';
    }
}
