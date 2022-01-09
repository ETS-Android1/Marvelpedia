package com.project_future_2021.marvelpedia.database;

import androidx.room.TypeConverter;

import com.project_future_2021.marvelpedia.data.Image;

public class ImageConverter {
    private static final String SEPARATOR = "=";
    private static final String TAG = "ImageConverter";

    @TypeConverter
    public static Image stringToImage(String dbRepresentation) {
        if (dbRepresentation == null) {
            return null;
        } else {
            String[] parts = dbRepresentation.split(SEPARATOR);
            String path = parts[0];
            String extension = parts[1];
            String variant = parts[2];
            Image resultImage = new Image(path, extension, variant);
            return resultImage;
        }
    }

    @TypeConverter
    public static String imageToString(Image image) {
        if (image == null) {
            return null;
        } else {
            String path = image.getPath() + SEPARATOR;
            String extension = image.getExtension() + SEPARATOR;
            String variant = image.getVariant() + SEPARATOR;
            String resultString = path + extension + variant;
            return resultString;
        }
    }
}