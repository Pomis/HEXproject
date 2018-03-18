package pomis.app.dressup.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import pomis.app.dressup.R;
import pomis.app.dressup.activities.SelectionActivity;
import pomis.app.dressup.base.InjectionFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends InjectionFragment {


    @BindView(R.id.tv_add)
    TextView tvAdd;
    Unbinder unbinder;
    @BindView(R.id.iv_photos)
    ImageView ivPhotos;
    @BindView(R.id.iv_test_bg)
    ImageView ivTestBg;
    @BindView(R.id.rl_add_photo)
    RelativeLayout rlAddPhoto;

    public PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        unbinder = ButterKnife.bind(this, view);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Skia.ttf");
        tvAdd.setTypeface(type, Typeface.BOLD);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.rl_add_photo)
    public void onViewClicked() {
        FishBun.with(PhotoFragment.this).setImageAdapter(new GlideAdapter()).setIsUseDetailView(false)
                .setMaxCount(3)
                .setActionBarColor(
                        getActivity().getResources().getColor(R.color.camel),
                        getActivity().getResources().getColor(R.color.camelDark))
                .setMinCount(1)
                .setPickerSpanCount(3)
                .setButtonInAlbumActivity(true)
                .setCamera(true)
                .setAllViewTitle("All")
                .setActionBarTitle("Image Library")
                .textOnImagesSelectionLimitReached("Limit Reached!")
                .textOnNothingSelected("Nothing Selected")
                .startAlbum();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    // you can get an image path(ArrayList<String>) on <0.6.2

                    ArrayList<Parcelable> path = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                    Toasty.info(getActivity(), path.size() + " Images received").show();
                    SelectionActivity.start(getActivity(), path);

                    break;
                }
        }
    }
}
