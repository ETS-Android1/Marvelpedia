package com.project_future_2021.marvelpedia.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

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
@Entity(tableName = "heroes_table")
public class Hero implements Parcelable {

    //The unique ID of the character resource.
    @PrimaryKey
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
    //@Ignore
    //@TypeConverters(ImageConverter.class)
    private Image thumbnail;

    //The canonical URL identifier for this resource.
    private String resourceURI;

    //A resource list containing comics which feature this character.
    @Ignore
    private Comics comics;

    //A resource list of series in which this character appears.
    @Ignore
    private Series series;

    //A resource list of stories in which this character appears.
    @Ignore
    private Stories stories;

    //A resource list of events in which this character appears.
    @Ignore
    private Events events;

    //Array[Url] urls
    //A set of public web site URLs for the resource.
    @Ignore
    private List<Url> urls;

    private Boolean isFavorite = false;

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

    public Hero(Integer id, String name, String description, String modified, Image thumbnail, String resourceURI, Comics comics, Series series, Stories stories, Events events, List<Url> urls, Boolean isFavorite) {
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
        this.isFavorite = isFavorite;
    }

    public Hero(Integer id, String name, String description, String modified, Image thumbnail, Boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.modified = modified;
        this.thumbnail = thumbnail;
        this.isFavorite = isFavorite;
    }

    public Hero(Integer id, String name, String description, Boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isFavorite = isFavorite;
    }

    public Hero(Integer id, String name, String description, Image thumbnail, Boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.isFavorite = isFavorite;
    }

    protected Hero(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        description = in.readString();
        modified = in.readString();
        resourceURI = in.readString();
        byte tmpIsFavorite = in.readByte();
        isFavorite = tmpIsFavorite == 0 ? null : tmpIsFavorite == 1;
    }

    public static final Creator<Hero> CREATOR = new Creator<Hero>() {
        @Override
        public Hero createFromParcel(Parcel in) {
            return new Hero(in);
        }

        @Override
        public Hero[] newArray(int size) {
            return new Hero[size];
        }
    };

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

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", modified='" + modified + '\'' +
                ", thumbnail=" + thumbnail +
                ", resourceURI='" + resourceURI + '\'' +
                ", comics=" + comics +
                ", series=" + series +
                ", stories=" + stories +
                ", events=" + events +
                ", urls=" + urls +
                ", isFavorite=" + isFavorite +
                '}';
    }

    public static Hero copyHero(Hero inputHero) {
        Hero copiedHero = new Hero();

        copiedHero.setId(inputHero.getId());
        copiedHero.setId(inputHero.getId());
        copiedHero.setName(inputHero.getName());
        copiedHero.setDescription(inputHero.getDescription());
        copiedHero.setModified(inputHero.getModified());
        copiedHero.setThumbnail(inputHero.getThumbnail());
        copiedHero.setResourceURI(inputHero.getResourceURI());
        copiedHero.setComics(inputHero.getComics());
        copiedHero.setSeries(inputHero.getSeries());
        copiedHero.setStories(inputHero.getStories());
        copiedHero.setEvents(inputHero.getEvents());
        copiedHero.setUrls(inputHero.getUrls());
        copiedHero.setFavorite(inputHero.getFavorite());

        return copiedHero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        //return id.equals(hero.id) && name.equals(hero.name);
        // super-duper important to check for isFavorite comparison too, or else the UI does not update properly.
        return id.equals(hero.id) && name.equals(hero.name) && isFavorite.equals(hero.isFavorite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, modified, thumbnail, resourceURI, comics, series, stories, events, urls, isFavorite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(modified);
        dest.writeString(resourceURI);
        dest.writeByte((byte) (isFavorite == null ? 0 : isFavorite ? 1 : 2));
    }
}
