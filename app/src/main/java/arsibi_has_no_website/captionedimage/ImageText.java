package arsibi_has_no_website.captionedimage;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hp on 11-06-2017.
 */

public class ImageText {
    Bitmap image;
    String text;
    TextView tv;
    ImageView img;
    String filepath;
    boolean checker=true;
    public void switchViews(){
        if(checker==true){
            img.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.VISIBLE);
            checker=false;
        }
        else{
            img.setVisibility(View.VISIBLE);
            tv.setVisibility(View.INVISIBLE);
            checker=true;
        }
    }
}

