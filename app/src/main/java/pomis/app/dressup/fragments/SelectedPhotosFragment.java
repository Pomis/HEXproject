package pomis.app.dressup.fragments;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pomis.app.dressup.App;
import pomis.app.dressup.R;
import pomis.app.dressup.activities.SelectionActivity;
import pomis.app.dressup.activities.ShopActivity;
import pomis.app.dressup.activities.WaitingActivity;
import pomis.app.dressup.base.InjectionFragment;
import pomis.app.dressup.models.Offer;
import pomis.app.dressup.models.Photo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pomis.app.dressup.utils.Converter.asDp;
import static pomis.app.dressup.utils.Converter.asPx;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedPhotosFragment extends InjectionFragment {
    final int DURATION = 1000;


    @BindView(R.id.phv_photos)
    PlaceHolderView phvPhotos;
    Unbinder unbinder;
    SelectionActivity activity;
    @BindView(R.id.iv_wand)
    ImageView ivWand;
    @BindView(R.id.tv_magic)
    TextView tvMagic;
    @BindView(R.id.rl_magic)
    RelativeLayout rlMagic;
    @BindView(R.id.rl_next)
    RelativeLayout rlNext;
    @BindView(R.id.rl_wear_types)
    RelativeLayout rlWearTypes;
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.iv_shoe)
    ImageView ivShoe;
    @BindView(R.id.iv_bottom)
    ImageView ivBottom;
    @BindView(R.id.tv_wear_types)
    TextView tvWearTypes;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private String type;

    ArrayList<String> sets = new ArrayList<>();
    int i = 0;

    public SelectedPhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_photos, container, false);
        unbinder = ButterKnife.bind(this, view);
        activity = (SelectionActivity) getActivity();
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Skia.ttf");
        tvMagic.setTypeface(type, Typeface.BOLD);
        tvWearTypes.setTypeface(type, Typeface.BOLD);
        tvNext.setTypeface(type, Typeface.BOLD);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (Photo photo : activity.photos) {
            phvPhotos.addView(photo);
            photo.setOnCloseListener(() -> {
                phvPhotos.removeView(photo);
                if (phvPhotos.getViewResolverCount() == 0) {
                    activity.finish();
                }
                rescale();
                phvPhotos.refresh();

            });
        }

        rescale();
        rlWearTypes.animate().xBy(800);
        rlMagic.animate().xBy(800);
        ivShoe.animate().yBy(asPx(-400, getContext()));
    }

    void rescale() {
        activity.photos.get(activity.photos.size() - 1).currentState = Photo.Case.EXPANDED;
        ((Photo) phvPhotos.getAllViewResolvers().get(
                phvPhotos.getAllViewResolvers().size() - 1
        )).currentState = phvPhotos.getAllViewResolvers().size() > 1 ?
                Photo.Case.EXPANDED : Photo.Case.FULL;

//        phvPhotos.refresh();
    }

    @OnClick(R.id.rl_next)
    void onNextClick() {

        phvPhotos.animate().xBy(800)
                .alpha(0f)
                .setDuration(DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        rlNext.animate().xBy(800)
                .alpha(0f)
                .setDuration(DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());

        rlWearTypes.setVisibility(View.VISIBLE);
        rlWearTypes.animate().xBy(-800)
                .alpha(1f)
                .setDuration(DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        ivShoe.animate().yBy(asPx(500, getActivity())).setDuration(DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());
    }


    @OnClick(R.id.rl_magic)
//    @OnClick(R.id.tv_magic)
    void onMagicClick() {
        Toasty.info(activity, "clicket");
        WaitingActivity.start(activity);
        i = 0;

        for (Photo photo : activity.photos) {
//            File imgFile = new File(getRealPathFromURI(activity.photos.get(0).imagePath));


            photo.generateBitmap();
            File f = convertToFile(photo.image);
            RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"), f);
            MultipartBody.Part propertyImagePart = MultipartBody.Part.createFormData("file",
                    f.getName(), propertyImage);
            api.upload(propertyImagePart).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("kek", "kek" + response.code());
                    sets.add(response.body().toString());
                    i++;
                    if (i == activity.photos.size()) {
                        Log.d("kek", "left cycle");
                        api.items(sets.get(0), sets.get(1), sets.get(2), type).enqueue(new Callback<List<Offer>>() {
                            //
                            @Override
                            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                                Log.d("kek", "Final result: " + response.toString());
                                App.offers = response.body();
                                ShopActivity.start(getContext());
                            }

                            @Override
                            public void onFailure(Call<List<Offer>> call, Throwable t) {
                                t.printStackTrace();
                                ShopActivity.start(getContext());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        }

    }


    @OnClick(R.id.iv_bottom)
    void bottomClick() {
        type = "bottom";
        ivTop.setImageDrawable(activity.getResources().getDrawable(R.drawable.top));
        ivBottom.setImageDrawable(activity.getResources().getDrawable(R.drawable.bottom_hl));
        ivShoe.setImageDrawable(activity.getResources().getDrawable(R.drawable.shoe));
        showMagicButton();
    }

    @OnClick(R.id.iv_top)
    void topClick() {
        type = "top";
        ivTop.setImageDrawable(activity.getResources().getDrawable(R.drawable.top_hl));
        ivBottom.setImageDrawable(activity.getResources().getDrawable(R.drawable.bottom));
        ivShoe.setImageDrawable(activity.getResources().getDrawable(R.drawable.shoe));
        showMagicButton();
    }

    @OnClick(R.id.iv_shoe)
    void shoeClick() {
        type = "shoe";
        ivTop.setImageDrawable(activity.getResources().getDrawable(R.drawable.top));
        ivBottom.setImageDrawable(activity.getResources().getDrawable(R.drawable.bottom));
        ivShoe.setImageDrawable(activity.getResources().getDrawable(R.drawable.shoe_hl));
        showMagicButton();
    }


    void showMagicButton() {
        if (rlMagic.getVisibility() != View.VISIBLE) {
            rlMagic.setVisibility(View.VISIBLE);
            rlMagic.animate().xBy(-800)
                    .alpha(1f)
                    .setDuration(DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
        }
    }

    File convertToFile(Bitmap bmp) {
        try {
            //create a file to write bitmap data
            File f = new File(activity.getCacheDir(), "test");
            f.createNewFile();

            //Convert bitmap to byte array

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
