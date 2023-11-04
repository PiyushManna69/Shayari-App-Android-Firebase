package com.infinisync.shayariapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.infinisync.shayariapp.R;
import com.infinisync.shayariapp.adapter.ShayariAdapter;
import com.infinisync.shayariapp.dao.ShayariDao;
import com.infinisync.shayariapp.dao.ShayariDatabase;
import com.infinisync.shayariapp.databinding.ActivityShayariBinding;
import com.infinisync.shayariapp.model.ShayariModel;

import java.util.ArrayList;

public class ShayariActivity extends AppCompatActivity {

    private ActivityShayariBinding binding;
    private ShayariAdapter shayariAdapter;
    private ArrayList<ShayariModel> shayariModels;
    private FirebaseFirestore database;
    private String cId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShayariBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.adsUnitId));
        loadAds();
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                loadAds();
            }
        });





        database = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String cNtxt =intent.getStringExtra("categoryName");
        cId =intent.getStringExtra("categoryId");
        binding.cNTxt.setText(cNtxt);
        binding.backImage.setOnClickListener(view -> onBackPressed());
        intShayari();
    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    public void intShayari() {
        shayariModels = new ArrayList<>();
        shayariAdapter = new ShayariAdapter(this, shayariModels);
        binding.shayariRecylerView.setAdapter(shayariAdapter);
        getShayari();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.shayariRecylerView.setLayoutManager(linearLayoutManager);
    }


    public void getShayari() {
        database.collection("category")
                .document(cId)
                .collection("shayari")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() < 1) {
                            Toast.makeText(ShayariActivity.this, "No Shayari Available", Toast.LENGTH_SHORT).show();
                        } else {
                            shayariModels.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                ShayariModel model = snapshot.toObject(ShayariModel.class);
                                model.setShayariId(snapshot.getId());
                                shayariModels.add(model);
                                if (isShayariInRoomDatabase(model.getShayariId())) {
                                    model.setBookmarked(true);
                                } else {
                                    model.setBookmarked(false);
                                }
                            }
                            shayariAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(ShayariActivity.this, "Failed to fetch shayari", Toast.LENGTH_SHORT).show());
    }

    private boolean isShayariInRoomDatabase(String shayariId) {
        final boolean[] result = {false};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ShayariDatabase db = Room.databaseBuilder(getApplicationContext(), ShayariDatabase.class, "shayari").build();
                ShayariDao shayariDao = db.shayariDao();
                ShayariModel shayari = shayariDao.getShayariById(shayariId);
                result[0] = shayari != null;
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

}