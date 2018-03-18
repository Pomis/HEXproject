package pomis.app.dressup.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pomis.app.dressup.R;
import pomis.app.dressup.models.Photo;

public class SelectionActivity extends AppCompatActivity {

    public ArrayList<Parcelable> data;
    public ArrayList<Photo> photos;

    public static void start(Context context, ArrayList<Parcelable> data) {
        Intent starter = new Intent(context, SelectionActivity.class);
        starter.putExtra("data", data);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photos = new ArrayList<>();
        readIntent();
        setContentView(R.layout.activity_selection);

    }

    private void readIntent() {
        data = getIntent().getParcelableArrayListExtra("data");
        for (int i = 0; i < data.size(); i++) {
            final Uri imagePath = (Uri) data.get(i);
            Photo photo = new Photo();
            photo.imagePath = imagePath;
            photo.context = this;
            photos.add(photo);
        }
    }
}
