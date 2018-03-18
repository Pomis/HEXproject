package pomis.app.dressup.models;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import java.util.ArrayList;

import pomis.app.dressup.R;
import pomis.app.dressup.activities.DetailsActivity;

/**
 * Created by romanismagilov on 18.03.18.
 */

@Layout(R.layout.item_offer)
public class Offer {
    public String name;
    public ArrayList<ImageModel> images;
    public Price price;
    public Type type;

    transient Context context;

    @View(R.id.tv_row_label)
    transient TextView tvLabel;

    @View(R.id.iv_row_offer)
    transient ImageView ivOffer;

    @View(R.id.tv_price)
    transient TextView tvPrice;

    public void setContext(Context context) {
        this.context = context;
    }

    private class Price {
        public float sale;
    }

    private class ImageModel {
        public String uri;
    }

    private class Type {
        public String group;
    }

    @Resolve
    void onResolve() {
        tvLabel.setText(name.split(" ")[0]);
        tvPrice.setText("€"+Math.floor(price.sale/70));
        Glide.with(context).load(images.get(0).uri).into(ivOffer);
    }

    @Click(R.id.rl_offer)
    void openDetails() {
        DetailsActivity.start(context, images.get(0).uri, type.group, price.sale, name);
    }
}
