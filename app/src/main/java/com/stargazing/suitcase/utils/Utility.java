package com.stargazing.suitcase.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class Utility {
    public static String encode(Context context, Uri imageUri) throws FileNotFoundException {

        var input = context.getContentResolver().openInputStream(imageUri);
        var bitmap = BitmapFactory.decodeStream(input);

        // Encode image to base64 string
        var baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        var imageBytes = baos.toByteArray();
        var imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    public static Bitmap decode(String imageString) {
        // Decode base64 string to image
        var imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        var decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }
}
