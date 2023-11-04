package com.infinisync.shayariapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.infinisync.shayariapp.R;
import com.infinisync.shayariapp.adapter.CategoryAdapter;
import com.infinisync.shayariapp.databinding.ActivityHomeBinding;
import com.infinisync.shayariapp.model.CategoryModel;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryModel> categoryModels;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();

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



        intCategory();
        binding.bookmark.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
            startActivity(intent);
        });



    }

    private void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }



    public void intCategory() {
        categoryModels = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryModels);
        getCategory();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.categoryRecylerView.setAdapter(categoryAdapter);
        binding.categoryRecylerView.setLayoutManager(gridLayoutManager);
    }
    public void getCategory() {
        database.collection("category")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryModels.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                        CategoryModel model = snapshot.toObject(CategoryModel.class);
                        model.setCategoryId(snapshot.getId());
                        categoryModels.add(model);
                    }

                    categoryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Failed To Fetch Data", Toast.LENGTH_SHORT).show());
    }

}
