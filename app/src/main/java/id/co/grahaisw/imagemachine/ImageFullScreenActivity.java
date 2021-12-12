package id.co.grahaisw.imagemachine;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageFullScreenActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        Uri pictureUri = Uri.parse(getIntent().getExtras().getString("uri"));
        String transition = getIntent().getExtras().getString("transition");

        ImageView full_screen_image = findViewById(R.id.full_screen_image);
        full_screen_image.setImageURI(pictureUri);
        full_screen_image.setTransitionName(transition);
        supportStartPostponedEnterTransition();
    }
}
