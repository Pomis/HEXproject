package pomis.app.dressup.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pomis.app.dressup.R;

public class WaitingActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, WaitingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
    }

    @Override
    public void onBackPressed() {

    }
}
