package com.example.navendu.savecontacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

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

        ContentResolver resolver=getContentResolver();
        Cursor cursor=resolver.query(uri,projection,selection,selectionArgs,sortOrder);
        String ans="";
        while (cursor.moveToNext())
        {
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ans+=name+"="+num;
            ans+="\n";
        }

        t1=findViewById(R.id.tt);
        t1.setText(ans);
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
