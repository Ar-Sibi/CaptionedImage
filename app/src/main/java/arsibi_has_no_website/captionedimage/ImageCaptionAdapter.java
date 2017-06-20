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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static arsibi_has_no_website.captionedimage.MainActivity.captionimg;


public class ImageCaptionAdapter extends BaseAdapter
{
    TextView tv;
    ImageView imageView;
    boolean image=true;
    Context c;

    public ImageCaptionAdapter(Context c) {
        this.c = c;
    }

    @Override
    public int getCount() {
        return captionimg.size();
    }

    @Override
    public Object getItem(int position) {
        return captionimg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v;
        v= inflater.inflate(R.layout.image_text,parent,false);
        String s= captionimg.get(position).text;
        final Bitmap I=Bitmap.createScaledBitmap(captionimg.get(position).image,200,200,true);
        captionimg.get(position).tv=((TextView)v.findViewById(R.id.caption));
        captionimg.get(position).img=((ImageView)v.findViewById(R.id.image));
        captionimg.get(position).tv.setText(s);
        captionimg.get(position).img.setImageBitmap(I);


        if(captionimg.get(position).checker==false)
            captionimg.get(position).switchViews();
        if(captionimg.get(position).checker==true){
            captionimg.get(position).switchViews();
            captionimg.get(position).switchViews();
        }
        return v;
    }

}
