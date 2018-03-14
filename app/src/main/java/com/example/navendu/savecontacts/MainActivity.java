        package com.example.navendu.savecontacts;
        import android.Manifest;
        import android.app.ProgressDialog;
        import android.content.ContentResolver;
        import android.content.Context;
        import android.content.CursorLoader;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Environment;
        import android.provider.ContactsContract;
        import android.support.annotation.NonNull;
        import android.support.design.widget.CoordinatorLayout;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.BufferedInputStream;
        import java.io.BufferedOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.concurrent.Callable;
        import java.util.zip.ZipEntry;
        import java.util.zip.ZipOutputStream;

        import io.reactivex.Observable;
        import io.reactivex.Observer;
        import io.reactivex.android.schedulers.AndroidSchedulers;
        import io.reactivex.disposables.Disposable;
        import io.reactivex.functions.Function;
        import io.reactivex.schedulers.Schedulers;


        public class MainActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String TAG = "MainActivity";
    TextView t1;
    Button b1;
    MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button)findViewById(R.id.b2);


        b.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v)
        {


  //          myAsyncTask = new MyAsyncTask();
//            myAsyncTask.execute("");







        startAsyncTask("start");






        }

        });

    }


            private void startAsyncTask(String input) {
                Observable.just("start").map(new Function<String, Void>() {
                    @Override
                    public Void apply(String s) throws Exception {
                        return doInBackground(s);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Void>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Void aVoid) {
                                View h=findViewById(R.id.jj);
                                Snackbar.make(h," contacts saved", Snackbar.LENGTH_LONG).setAction("Action",null).setDuration(5000).show();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                                Log.i("status","complete");
                            }
                        });
            }

            //------------- full mapping to async task functions -----------------//
            //pre execute work here
           private void onPreExecute() {
                //progressBar.setVisibility(View.VISIBLE);
            }

            //do background things here
            private Void doInBackground(String data) throws InterruptedException {

                fetchContacts();




               // return data.length();
                return null;
            }

            //post execution work here
            private void onPostExecute(int result) {
              //  progressBar.setVisibility(View.GONE);
                View h=findViewById(R.id.jj);
                Snackbar.make(h,"contacts saved", Snackbar.LENGTH_LONG).setAction("Action",null).show();
               // textView.setText("Length of input is " + result);
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
            File root=Environment.getExternalStorageDirectory();
            File Dir=new File(root.getAbsolutePath()+"/MYAPPNAVENDU");
            if(!Dir.exists()) {
                Dir.mkdir();
            }

            File file=new File(Dir,"message.csv");
            try {
                FileOutputStream out=new FileOutputStream(file);
//              Log.i("THISSS", "fetchContacts:file stream success ");
                out.write(ans.getBytes());

                out.close();

//              Log.i("THISSS", "fetchContacts:file stream success dcjdbjvjdvvjd");

                String[] s = new String[1];
                String inputPath = root.getAbsolutePath()+"/MYAPPNAVENDU";
                s[0] = inputPath + "/message.csv";
                zip(s, inputPath + "/contacts.zip");

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
    private boolean doesUserHavePermission(Context context)
    {
        int result1 = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int result3 = context.checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS);


        return (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED) &&(result3 == PackageManager.PERMISSION_GRANTED);
    }
    public void zip(String[] _files, String zipFileName) {
        int BUFFER=1024;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                Log.v("Compress","Destination"+zipFileName);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void hello(View view)
    {
        if(!doesUserHavePermission(this))
            permissionRequest();
        TextView t=findViewById(R.id.tt);

        if(t.getText().toString()=="hello world")
        t.setText("hey tere this is main thread");
        else
            t.setText("hello world");

    }

    private void permissionRequest() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }
    class MyAsyncTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;


        //Runs on a different thread
        @Override
        protected String doInBackground(String... params) {
           fetchContacts();
            return "";}

        //Runs on UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        //Runs on UI thread
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hide();

        }
    }

}