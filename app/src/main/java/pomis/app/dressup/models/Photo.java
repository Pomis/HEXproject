package pomis.app.dressup.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import pomis.app.dressup.R;

import static pomis.app.dressup.utils.Converter.asPx;


/**
 * Created by romanismagilov on 17.03.18.
 */

@Layout(R.layout.item_photo)
@Animate(Animation.ENTER_TOP_DESC)
public class Photo {


    public enum Case {
        EXPANDED, SHRUNK, FULL
    }

    @View(R.id.iv_row_photo)
    public ImageView ivPhoto;

    public Bitmap image;
    public Uri imagePath;
    public Context context;
    public Case currentState = Case.SHRUNK;
    public Runnable callback;

//    public Photo(Bitmap image) {
//        this.image = image;
//    }

    public Photo() {
    }

    @SuppressLint("StaticFieldLeak")
    @Resolve
    public void onResolve() {
        switch (currentState) {
            case FULL:
                ivPhoto.getLayoutParams().height = asPx(300, context);
                break;

            case EXPANDED:
                ivPhoto.getLayoutParams().height = asPx(200, context);
                break;

            case SHRUNK:
                ivPhoto.getLayoutParams().height = asPx(100, context);
                break;
        }
        try {
            image = MediaStore.Images.Media
                    .getBitmap(context.getContentResolver(), imagePath);
            image = Bitmap.createScaledBitmap(image,
                    400, 300, false);
            ivPhoto.setImageBitmap(image);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            image = Glide.with(context)
//                    .asBitmap()
//                    .load(imagePath)
//                    .submit()
//                    .get();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }

    public void generateBitmap() {

    }

    @Click(R.id.iv_close)
    public void onCloseClick() {
        Log.d("kek", "ckicleocked");
        if (callback!=null) {
            callback.run();
        }
    }

    public void setOnCloseListener(Runnable callback) {
        this.callback = callback;
    }

}
