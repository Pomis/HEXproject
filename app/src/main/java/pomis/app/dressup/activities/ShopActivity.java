package pomis.app.dressup.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.mindorks.placeholderview.PlaceHolderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import pomis.app.dressup.App;
import pomis.app.dressup.R;
import pomis.app.dressup.models.Offer;

public class ShopActivity extends AppCompatActivity {

    @BindView(R.id.phv_offers)
    PlaceHolderView phvOffers;

    public static void start(Context context) {
        Intent starter = new Intent(context, ShopActivity.class);

        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        initList();
    }

    private void initList() {
        phvOffers.setLayoutManager(new GridLayoutManager(this, 2));
        Toasty.info(this, "count: "+ App.offers.size()).show();
        for (Offer offer : App.offers) {
            offer.setContext(this);
            phvOffers.addView(offer);
        }

    }
}
