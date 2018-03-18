package pomis.app.dressup.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mindorks.placeholderview.PlaceHolderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pomis.app.dressup.App;
import pomis.app.dressup.R;
import pomis.app.dressup.models.Offer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.phv_details)
    PlaceHolderView phvDetails;
    @BindView(R.id.item_name)
    TextView itemName;
    @BindView(R.id.item_price)
    TextView itemPrice;
    @BindView(R.id.tv_add)
    TextView tvAdd;

    public static void start(Context context, String uri, String type, float price, String name) {
        Intent starter = new Intent(context, DetailsActivity.class);
        starter.putExtra("uri", uri);
        starter.putExtra("type", type);
        starter.putExtra("name", name);
        starter.putExtra("price", price);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        readIntent();
    }

    private void readIntent() {
        String uri = getIntent().getStringExtra("uri");
        String name = getIntent().getStringExtra("name");
        String type = getIntent().getStringExtra("type");
        float price = getIntent().getFloatExtra("price", 0);
        itemPrice.setText("€" + price/70);
        itemName.setText(name.split(" ")[0]);
        App.getApi().getStyle(type, uri).enqueue(new Callback<List<Offer>>() {
            @Override
            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                for (Offer offer : response.body()) {
                    phvDetails.addView(offer);
                }
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable t) {

            }
        });

    }


}
