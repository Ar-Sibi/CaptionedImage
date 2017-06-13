package arsibi_has_no_website.captionedimage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST=1203;
    ListView lv;
    RelativeLayout additionRelativeLayout;
    RelativeLayout bottomLayout;
    EditText addtv;
    ImageCaptionAdapter adapter;
    static List<ImageText> captionimg= new ArrayList<>();
    boolean delete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bottomLayout=(RelativeLayout)findViewById(R.id.bottombuttonrellayout);
        additionRelativeLayout=(RelativeLayout)findViewById(R.id.additionrellayout);
        addtv=(EditText)findViewById(R.id.addedtext);
        lv=(ListView)findViewById(R.id.image_caption_list);
        adapter = new ImageCaptionAdapter(getApplicationContext(),R.layout.image_text,captionimg);
        lv.setAdapter(adapter);
        loadData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(delete==true){
                    captionimg.remove(position);
                    adapter.notifyDataSetChanged();
                    delete=false;
                }
                else{
                    captionimg.get(position).switchViews();
                }
                delete=false;
            }
        });
    }
    public void loadData(){
        File f=new File(this.getFilesDir(),"hello1.txt");
        try {
            if(!f.exists())
            f.createNewFile();
            Scanner s = new Scanner(f);
            while(s.hasNext()){
                ImageText t=new ImageText();
                t.filepath=s.nextLine();
                t.text=s.nextLine();
                t.image=BitmapFactory.decodeFile(t.filepath);
                captionimg.add(t);
            }
            s.close();
            adapter.notifyDataSetChanged();
        }catch (IOException e){
            Log.d("WOO",e.toString());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onStop() {
        super.onStop();

        File f=new File(this.getFilesDir(),"hello1.txt");
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            for(int i=0;i<captionimg.size();i++){
                out.write((captionimg.get(i).filepath+"\n").getBytes());
                out.write((captionimg.get(i).text+"\n").getBytes());
            }
            out.close();
        }catch (IOException e){
        }
    }
    public void addItems(View v){
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent choose=new Intent(Intent.ACTION_CHOOSER);
        choose.putExtra(Intent.EXTRA_INTENT,cameraintent);
        choose.putExtra(Intent.EXTRA_TITLE,"Gallery");
        Intent[] intents = {galleryintent};
        choose.putExtra(Intent.EXTRA_INITIAL_INTENTS,intents);
        startActivityForResult(choose,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==GALLERY_REQUEST&&resultCode== Activity.RESULT_OK){
        Uri  selectedImage=data.getData();
        if(selectedImage!=null) {
            String[] filepathcolumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filepathcolumn, null, null, null);
            cursor.moveToFirst();
            int columnindex = cursor.getColumnIndex(filepathcolumn[0]);
            String picturepath = cursor.getString(columnindex);
            cursor.close();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                if (bitmap != null) {
                    ImageText t = new ImageText();
                    t.image = bitmap;
                    t.filepath = picturepath;
                    captionimg.add(t);
                    getCaption();
                }
            } catch (IOException e) {
            }
        }
        else{
            Bitmap bit;
            Cursor cursor=getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{MediaStore.Images.Media.DATA,MediaStore.Images.Media.DATE_ADDED,MediaStore.Images.ImageColumns.ORIENTATION},MediaStore.Images.Media.DATE_ADDED,null,"date_added DESC");
            cursor.moveToFirst();
            Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            String photopath=uri.toString();
            bit=BitmapFactory.decodeFile(photopath);
            if (bit != null) {
                ImageText t = new ImageText();
                t.image = bit;
                t.filepath = photopath;
                captionimg.add(t);
                getCaption();
            }
        }
    }
    }
    public void getCaption(){
        additionRelativeLayout.setVisibility(LinearLayout.VISIBLE);
        bottomLayout.setVisibility(LinearLayout.GONE);
    }
    public void addCaption(View v){
        captionimg.get(captionimg.size()-1).text=addtv.getText().toString();
        addtv.setText("");
        addtv.clearFocus();
        additionRelativeLayout.setVisibility(LinearLayout.GONE);
        bottomLayout.setVisibility(LinearLayout.VISIBLE);
        adapter.notifyDataSetChanged();
    }
    public void removeItems(View v){
    delete=!delete;
    if(delete==true)
        Toast.makeText(getApplicationContext(),"Click on item to be deleted",Toast.LENGTH_SHORT).show();
    else
        Toast.makeText(getApplicationContext(),"Deletion Cancelled",Toast.LENGTH_SHORT).show();
    }
}
