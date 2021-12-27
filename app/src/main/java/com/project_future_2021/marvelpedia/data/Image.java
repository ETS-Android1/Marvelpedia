package com.project_future_2021.marvelpedia.data;

/*
*
Image Representations and Pathing
The Marvel Comics API does not provide full paths to images.
Instead, images are represented as a partial path to an image file and the canonical extension of that file.
Developers may select from a set of image variants (predefined sizes and ratios) in order to best serve the presentation of their web site or application.

To build a full image path from an image representation

Take the "path" element from the image representation
Append a variant name to the path element
Append the "extension" element to the variant name
For example, to display the image represented here:

"thumbnail": {
  "path": "http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73",
  "extension": "jpg"
}
Take the path element: http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73
Select an image variant name (see the full list below)
and append the variant name to the path element: http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73/portrait_xlarge
Append the extension: http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73/portrait_xlarge.jpg
In order to make your web site or application load and respond quickly and preserve end-user bandwidth,
we recommend using the smallest-sized image necessary to meet the needs our user interface.

Image Variants
The following named image sizes and ratios are available for you use products leveraging the Marvel API.
We add new image sizes from time to time, so please check this page in the future.

* Portrait              aspect ratio
portrait_small	        50x75px
portrait_medium	        100x150px
portrait_xlarge	        150x225px
portrait_fantastic      168x252px
portrait_uncanny	    300x450px
portrait_incredible	    216x324px


* Standard (square)     aspect ratio
standard_small	        65x45px
standard_medium	        100x100px
standard_large	        140x140px
standard_xlarge	        200x200px
standard_fantastic	    250x250px
standard_amazing	    180x180px

* Landscape             aspect ratio
landscape_small	        120x90px
landscape_medium	    175x130px
landscape_large	        190x140px
landscape_xlarge	    270x200px
landscape_amazing	    250x156px
landscape_incredible    464x261px

* Full size images
detail	                full image, constrained to 500px wide
full-size image	        no variant descriptor
*
* */

/*
*
Images:
Images are represented as a partial path and an extension. See the guide to images for information about how to construct full paths to image files.

attribute	type	description
path	string	The directory path of to the image.
extension	string	The file extension for the image.
*
* */
public class Image {

    //The directory path of to the image.
    private String path;

    //The file extension for the image.
    private String extension;

    //The image size, see top-level comment for more.
    //NOT provided by the Server response.
    private String variant;

    /**
     * No args constructor for use in serialization
     */
    public Image() {
    }

    /**
     * @param path
     * @param extension
     */
    public Image(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    public Image(String path, String extension, String variant) {
        this.path = path;
        this.extension = extension;
        this.variant = variant;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String makeImageWithVariant(String variant) {
        return path + "/" + variant + "." + extension;
    }

    @Override
    public String toString() {
        return "Image{" +
                "path='" + path + '\'' +
                ", extension='" + extension + '\'' +
                ", variant='" + variant + '\'' +
                '}';
    }
}
