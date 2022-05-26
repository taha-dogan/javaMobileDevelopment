package com.flakysob.kiralaapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.flakysob.kiralaapp.DAO.IUrunDAO;
import com.flakysob.kiralaapp.DAO.IUserDAO;
import com.flakysob.kiralaapp.Database.AppDatabase;
import com.flakysob.kiralaapp.Entity.UrunEntity;
import com.flakysob.kiralaapp.Entity.UserEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Ilanlar extends AppCompatActivity implements IlanRecyclerView{

    private IUrunDAO urunDAO;
    private AppDatabase appDatabase;
    private List<UrunEntity> urunler;
    private RecyclerView IlanlarRC;
    private FirebaseAuth mAuth;
    private IUserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilanlar);

        initComponents();
        loadData();
        Ilan_RecyclerViewAdapter adapter = new Ilan_RecyclerViewAdapter(this,urunler,this);
        IlanlarRC.setAdapter(adapter);
        IlanlarRC.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        FirebaseUser user = mAuth.getCurrentUser();
        UserEntity kullanici = userDAO.loadUserByEposta(user.getEmail());
        Log.w("deneme", "Kullanici ID: "+ kullanici.getId());
        Intent gelenIntent = getIntent();
        String kategoriID = gelenIntent.getStringExtra("kategoriID");
        Log.w("deneme", "Kategori ID: "+ kategoriID);
        urunler = urunDAO.loadUrunByKategoriAndUserID(Long.parseLong(kategoriID),kullanici.getId());
    }

    private void initComponents() {
        IlanlarRC = (RecyclerView) findViewById(R.id.RecyIlan);
        appDatabase = AppDatabase.getAppDatabase(Ilanlar.this);
        urunDAO = appDatabase.getUrunDAO();
        userDAO = appDatabase.getUserDAO();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onItemClick(UrunEntity urunEntity) {
        Intent intent = new Intent(Ilanlar.this,IcerikActivity.class);
        String ilanID = ""+urunEntity.getId();
        intent.putExtra("ilanID",ilanID);
        startActivity(intent);
    }
}