package com.example.das.imagecrop;

import android.content.Context;
import android.content.Intent;

/**
 * Created by das on 2015. 7. 10..
 */
public class MediaStoreUtils {
    public static Intent getPickImageIntent(final Context context) {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        return Intent.createChooser(intent, "Select picture");
    }
}
