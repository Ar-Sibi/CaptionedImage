package arsibi_has_no_website.captionedimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    RelativeLayout additionRelativeLayout;
    RelativeLayout intentRelativeLayout;
    RelativeLayout bottomLayout;
    EditText addtv;
    ImageCaptionAdapter adapter;
    int selectedindex=0;
    static List<ImageText> captionimg= new ArrayList<>();
    boolean delete=false;
    boolean crop=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getPermissions();
        bottomLayout=(RelativeLayout)findViewById(R.id.bottombuttonrellayout);
        additionRelativeLayout=(RelativeLayout)findViewById(R.id.additionrellayout);
        intentRelativeLayout=(RelativeLayout)findViewById(R.id.intentrelativelayout);
        addtv=(EditText)findViewById(R.id.addedtext);
        lv=(ListView)findViewById(R.id.image_caption_list);
        adapter = new ImageCaptionAdapter(getApplicationContext(),R.layout.image_text,captionimg);
        lv.setAdapter(adapter);
        if(getIntent().hasCategory(Intent.CATEGORY_LAUNCHER))
        loadData();
        else
            Log.d("MOO",getIntent().toString());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(delete==true){
                    captionimg.remove(position);
                    adapter.notifyDataSetChanged();
                    delete=false;
                }
                else{
                    if(crop==true){
                        cropThis(position);
                        selectedindex=position;
                    }
                    else
                    captionimg.get(position).switchViews();
                }
                crop=false;
                delete=false;
            }
        });
    }
    public void getPermissions() {
        if(SDK_INT>= M)
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_WRITE_EXTERNAL);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.REQUEST_WRITE_EXTERNAL:if(grantResults[0]==PackageManager.PERMISSION_GRANTED);
                else
                    getPermissions();
        }
    }

    public void cropThis(int index){
        Intent intent=new Intent(this,CropperHandler.class);
        intent.putExtra(Constants.INDEX,index);
        startActivityForResult(intent,Constants.CROP_THIS);
    }
    public void loadData(){
        File f=new File(this.getFilesDir(),"hello1.txt");
        Log.d("MOO","cu");
        try {
            Log.d("MOO","cu");
            if(!f.exists())
            f.createNewFile();
            Scanner s = new Scanner(f);
            while(s.hasNext()){
                Log.d("MOO","cu");
                ImageText t=new ImageText();
                t.filepath=s.nextLine();
                t.text=s.nextLine();
                t.image=BitmapFactory.decodeFile(t.filepath);
                captionimg.add(t);
            }
            s.close();
            adapter.notifyDataSetChanged();
        }catch (IOException e){Log.d("MOO","cu2");
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
        intentRelativeLayout.setVisibility(LinearLayout.VISIBLE);
    }
    public void sendToCamera(View v){
        intentRelativeLayout.setVisibility(LinearLayout.GONE);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent,Constants.CAMERA_REQUEST);
    }
    public void sendToGallery(View v){
        intentRelativeLayout.setVisibility(LinearLayout.GONE);
        Intent galleryintent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryintent,Constants.GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.GALLERY_REQUEST&&resultCode== Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
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
                } catch (IOException e) {}
            }
        }
        if(requestCode==Constants.CAMERA_REQUEST&&resultCode== Activity.RESULT_OK){
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
        if(requestCode==Constants.CROP_THIS&&resultCode==Activity.RESULT_OK){
            int xbeg=data.getIntExtra(Constants.X_START,0);
            int ybeg=data.getIntExtra(Constants.Y_START,0);
            int xend=data.getIntExtra(Constants.X_END,0);
            int yend=data.getIntExtra(Constants.Y_END,0);
            int index=data.getIntExtra(Constants.INDEX,0);
            Bitmap b= captionimg.get(index).image;
            xbeg*=b.getWidth();
            xbeg/=800;
            ybeg*=b.getHeight();
            ybeg/=800;
            xend*=b.getWidth();
            xend/=800;
            yend*=b.getHeight();
            yend/=800;
            Log.d("MOO",String.format("%d   %d",xend-xbeg,b.getWidth()));
            Bitmap newB=Bitmap.createBitmap(b,xbeg,ybeg,xend-xbeg,yend-ybeg);
            captionimg.get(index).image=newB;
            captionimg.get(index).filepath=writeBitmapToFile(newB);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),"Crop Successful",Toast.LENGTH_SHORT).show();
        }
    }
    public String writeBitmapToFile(Bitmap bmp){
        String TimeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file=new File(getFilesDir(),TimeStamp+"pic.png");
        try {
            file.createNewFile();
            FileOutputStream f=new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG,85,f);
            f.flush();
            f.close();
        }catch (IOException e){}
        return file.getAbsolutePath();
    }
    public void cropper(View v){
        crop=true;
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