package com.infinisync.shayariapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.infinisync.shayariapp.R;
import com.infinisync.shayariapp.adapter.ShayariAdapter;
import com.infinisync.shayariapp.dao.ShayariDatabase;
import com.infinisync.shayariapp.databinding.ActivityBookmarkBinding;
import com.infinisync.shayariapp.model.ShayariModel;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private ActivityBookmarkBinding binding;
    private ShayariAdapter adapter;
    private ShayariDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
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


        binding.backImage.setOnClickListener(view -> onBackPressed());
        db = Room.databaseBuilder(this, ShayariDatabase.class, "shayari").build();
        adapter = new ShayariAdapter(this, new ArrayList<>());
        binding.shayariRecycler.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.shayariRecycler.setLayoutManager(linearLayoutManager);
        fetchAndDisplayBookmarkedShayari();
    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    private void fetchAndDisplayBookmarkedShayari() {
        new Thread(() -> {
            List<ShayariModel> bookmarkedShayari = db.shayariDao().getAllShayari();
            runOnUiThread(() -> adapter.setShayariList(bookmarkedShayari));
        }).start();
    }
}