package com.example.navendu.savecontacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.zip.ZipOutputStream;

import de.siegmar.fastcsv.writer.CsvWriter;

public class MainActivity extends AppCompatActivity {


    TextView t1;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public  void fetchContacts()
    {
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection={ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER} ;
        String selection=null;
        String[] selectionArgs=null;
        String sortOrder=null;

        ArrayList<String[]> data=new ArrayList<String[]>();


        ContentResolver resolver=getContentResolver();
        Cursor cursor=resolver.query(uri,projection,selection,selectionArgs,sortOrder);
        String ans="";
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ans+=name+","+num+"\n";

        }
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(getApplicationContext(),"available",Toast.LENGTH_SHORT).show();
            File root=Environment.getExternalStorageDirectory();
            File Dir=new File(root.getAbsolutePath()+"/MYAPPNAVENDU");


            if(!Dir.exists()) {
                Dir.mkdir();


                Toast.makeText(getApplicationContext(),"dir not exists",Toast.LENGTH_SHORT).show();


            }

            File file=new File(Dir,"message.txt");
            try {


                Toast.makeText(getApplicationContext(),"begin",Toast.LENGTH_SHORT).show();
                FileOutputStream out=new FileOutputStream(file);


                Log.i("THISSS", "fetchContacts:file stream success ");
                out.write(ans.getBytes());

                out.close();

                Log.i("THISSS", "fetchContacts:file stream success dcjdbjvjdvvjd");


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"not available",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestcode, @NonNull String[] permission,@NonNull int[] granted) {
        super.onRequestPermissionsResult(requestcode,permission,granted);

        if(requestcode==1334)
        {

            if(permission.length>0)
            {
                if(granted[0]==PackageManager.PERMISSION_GRANTED)
                {
                    fetchContacts();

                }
                else if(granted[0]==PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(getApplicationContext(),"please allow permission to save contacts",Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(),"please allow permission to save contacts",Toast.LENGTH_SHORT).show();

            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"please allow permission to save contacts",Toast.LENGTH_SHORT).show();

        }
    }

    public void hello(View view)
    {


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1334);



    }



}
