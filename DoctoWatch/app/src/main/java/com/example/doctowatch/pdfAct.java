package com.example.doctowatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.service.AccountAuthService;

import java.io.File;
import java.util.ArrayList;

public class pdfAct extends AppCompatActivity {

    Button logout;
    private static final String TAG = "Account";
    private AccountAuthService mAuthService;

    public static ArrayList<File> mfiles = new ArrayList<>();
    RecyclerView recyclerView;
    public static final int REQUET_PERMISSION=1;
    File folder;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        logout=findViewById(R.id.logout);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        recyclerView = findViewById(R.id.rv_View_pdf);

        permission();
    }

    private void permission() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(pdfAct.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                Toast.makeText(this,"Please Grant Permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(pdfAct.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUET_PERMISSION);
            }
        }
        else
        {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            initViews();
        }
    }

    private void initViews() {
        folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        mfiles=getPfdFiles(folder);

        ArrayList<File> mypdf = getPfdFiles(Environment.getExternalStorageDirectory());
        items = new String[mypdf.size()];
        for(int i=0;i<items.length;i++)
        {
            items[i]= mypdf.get(i).getName().replace(".pdf","");
        }
        PDFAdaptor pdfAdaptor = new PDFAdaptor(this, mfiles,items);
        recyclerView.setAdapter(pdfAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

    }

    private ArrayList<File> getPfdFiles(File folder){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = folder.listFiles();
        if(files!=null)
        {
            for(File singleFile: files)
            {
                if(singleFile.isDirectory() && !singleFile.isHidden())
                {
                    arrayList.addAll(getPfdFiles(singleFile));
                }
                else
                {

                    if(singleFile.getName().endsWith(".pdf"))
                    {
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

    private void signOut() {
        Task<Void> signOutTask = mAuthService.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "signOut Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "signOut fail");
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUET_PERMISSION)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted..",Toast.LENGTH_SHORT).show();
                initViews();
            }
            else
            {
                Toast.makeText(this,"Please Grant permission",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openMainActivity(){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"Successfully Logged Out!",Toast.LENGTH_SHORT).show();
    }
}