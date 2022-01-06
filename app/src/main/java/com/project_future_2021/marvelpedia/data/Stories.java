package com.project_future_2021.marvelpedia.data;
/*
*
*
* "stories":
{
                    "available": 21,
                    "collectionURI": "http://gateway.marvel.com/v1/public/characters/1011334/stories",
                    "items": [
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/19947",
                            "name": "Cover #19947",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/19948",
                            "name": "The 3-D Man!",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/19949",
                            "name": "Cover #19949",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/19950",
                            "name": "The Devil's Music!",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/19951",
                            "name": "Cover #19951",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/19952",
                            "name": "Code-Name:  The Cold Warrior!",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/47184",
                            "name": "AVENGERS: THE INITIATIVE (2007) #14",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/47185",
                            "name": "Avengers: The Initiative (2007) #14 - Int",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/47498",
                            "name": "AVENGERS: THE INITIATIVE (2007) #15",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/47499",
                            "name": "Avengers: The Initiative (2007) #15 - Int",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/47792",
                            "name": "AVENGERS: THE INITIATIVE (2007) #16",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/47793",
                            "name": "Avengers: The Initiative (2007) #16 - Int",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/48361",
                            "name": "AVENGERS: THE INITIATIVE (2007) #17",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/48362",
                            "name": "Avengers: The Initiative (2007) #17 - Int",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/49103",
                            "name": "AVENGERS: THE INITIATIVE (2007) #18",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/49104",
                            "name": "Avengers: The Initiative (2007) #18 - Int",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/49106",
                            "name": "Avengers: The Initiative (2007) #18, Zombie Variant - Int",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/49888",
                            "name": "AVENGERS: THE INITIATIVE (2007) #19",
                            "type": "cover"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/49889",
                            "name": "Avengers: The Initiative (2007) #19 - Int",
                            "type": "interiorStory"
                        },
                        {
                            "resourceURI": "http://gateway.marvel.com/v1/public/stories/54371",
                            "name": "Avengers: The Initiative (2007) #14, Spotlight Variant - Int",
                            "type": "interiorStory"
                        }
                    ],
                   "returned": 20
*
*
*
*
*
*
*
*
*
*
*
* */


import java.util.List;

/*
*
Stories:
attribute	type	description
id	int	The unique ID of the story resource.
title	string	The story title.
description	string	A short description of the story.
resourceURI	string	The canonical URL identifier for this resource.
type	string	The story type e.g. interior story, cover, text story.
modified	Date	The date the resource was most recently modified.
thumbnail	Image	The representative image for this story.
comics	ComicList	A resource list containing comics in which this story takes place.
series	SeriesList	A resource list containing series in which this story appears.
events	EventList	A resource list of the events in which this story appears.
characters	CharacterList	A resource list of characters which appear in this story.
creators	CreatorList	A resource list of creators who worked on this story.
originalissue	ComicSummary	A summary representation of the issue in which this story was originally published.
*
* */
public class Stories {

    private Integer available;

    //returned	int	The number of resources returned in this resource list (up to 20).
    private Integer returned;

    //collectionURI	string	The path to the list of full view representations of the items in this resource list.
    private String collectionURI;

    //items	Array[entity summary representations]	A list of summary views of the items in this resource list.
    private List<Items> items = null;

    /**
     * No args constructor for use in serialization
     */
    public Stories() {
    }

    public Stories(Integer available, Integer returned, String collectionURI, List<Items> items) {
        this.available = available;
        this.returned = returned;
        this.collectionURI = collectionURI;
        this.items = items;
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
        return "Stories{" +
                "available=" + available +
                ", returned=" + returned +
                ", collectionURI='" + collectionURI + '\'' +
                ", items=" + items +
                '}';
    }
}