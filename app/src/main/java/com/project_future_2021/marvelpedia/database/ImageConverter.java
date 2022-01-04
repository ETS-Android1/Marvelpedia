package com.project_future_2021.marvelpedia.database;

import android.util.Log;

import androidx.room.TypeConverter;

import com.project_future_2021.marvelpedia.data.Image;

public class ImageConverter {
    private static final String SEPARATOR = "=";
    private static final String TAG = "ImageConverter";

    @TypeConverter
    public static Image stringToImage(String dbRepresentation) {
        if (dbRepresentation == null) {
            Log.d(TAG, "Converting stringToImage: dbRepresentation was null.");
            return null;
        } else {
            String[] parts = dbRepresentation.split(SEPARATOR);
            String path = parts[0];
            String extension = parts[1];
            String variant = parts[2];
            Image resultImage = new Image(path, extension, variant);
            Log.d(TAG, "Converting stringToImage: " + resultImage.toString());
            return resultImage;
        }
    }

    @TypeConverter
    public static String imageToString(Image image) {
        if (image == null) {
            Log.d(TAG, "Converting imageToString: Image was null.");
            return null;
        } else {
            String path = image.getPath() + SEPARATOR;
            String extension = image.getExtension() + SEPARATOR;
            String variant = image.getVariant() + SEPARATOR;
            String resultString = path + extension + variant;
            Log.d(TAG, "Converting imageToString: " + resultString);
            return resultString;
        }
    }
}