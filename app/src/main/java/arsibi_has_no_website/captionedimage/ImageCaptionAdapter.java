package arsibi_has_no_website.captionedimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class ImageCaptionAdapter extends ArrayAdapter<ImageText>
{
    TextView tv;
    ImageView imageView;
    boolean image=true;
    public ImageCaptionAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ImageText> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v;
        if(convertView!=null)
        {v= convertView;
        }
        else
            v= inflater.inflate(R.layout.image_text,parent,false);
        String s=MainActivity.captionimg.get(position).text;
        final Bitmap I=Bitmap.createScaledBitmap(MainActivity.captionimg.get(position).image,200,200,true);
        MainActivity.captionimg.get(position).tv=((TextView)v.findViewById(R.id.caption));
        MainActivity.captionimg.get(position).img=((ImageView)v.findViewById(R.id.image));
        MainActivity.captionimg.get(position).tv.setText(s);
        MainActivity.captionimg.get(position).img.setImageBitmap(I);


        if(MainActivity.captionimg.get(position).checker==false)
            MainActivity.captionimg.get(position).switchViews();
        if(MainActivity.captionimg.get(position).checker==true){
            MainActivity.captionimg.get(position).switchViews();
            MainActivity.captionimg.get(position).switchViews();
        }
        return v;
    }
}
