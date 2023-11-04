package com.infinisync.shayariapp.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.infinisync.shayariapp.R;
import com.infinisync.shayariapp.dao.ShayariDatabase;
import com.infinisync.shayariapp.databinding.ItemShayariBinding;
import com.infinisync.shayariapp.model.ShayariModel;
import java.util.List;

public class ShayariAdapter extends RecyclerView.Adapter<ShayariAdapter.ShayariViewHolder> {

    Context context;
    List<ShayariModel> shayariModels;
    ClipboardManager clipboardManager;
    ShayariDatabase db;

    public ShayariAdapter(Context context, List<ShayariModel> shayariModel) {
        this.context = context;
        this.shayariModels = shayariModel;
        this.db = Room.databaseBuilder(context, ShayariDatabase.class, "shayari").build();
    }

    @NonNull
    @Override
    public ShayariViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShayariViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shayari, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShayariViewHolder holder, int position) {
        ShayariModel shayariModel = shayariModels.get(position);
        holder.binding.shayariText.setText(shayariModel.getShayariText());
        holder.binding.shayariAuthor.setText(shayariModel.getShayariAuthor());
        holder.binding.copy.setOnClickListener(view -> {
            String shayariText = shayariModel.getShayariText();
            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Shayari Text", shayariText);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Shayari copied to clipboard", Toast.LENGTH_SHORT).show();
        });
        holder.binding.share.setOnClickListener(view -> {
            String shayariText = shayariModel.getShayariText();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shayariText);
            context.startActivity(Intent.createChooser(shareIntent, "Share Shayari"));
        });

        if (shayariModel.isBookmarked == true) {
            holder.binding.bookmark.setImageResource(R.drawable.ic_bookmark);
        }else{
            holder.binding.bookmark.setImageResource(R.drawable.ic_bookmark_border);
        }

        holder.binding.bookmark.setOnClickListener(view -> {
            if (shayariModel.isBookmarked) {
                shayariModel.isBookmarked = false;
                holder.binding.bookmark.setImageResource(R.drawable.ic_bookmark_border);
                String shayariId = shayariModel.getShayariId();
                deleteBookmark(shayariId);
            } else {
                shayariModel.isBookmarked = true;
                holder.binding.bookmark.setImageResource(R.drawable.ic_bookmark);
                insertBookmark(shayariModel);
            }
        });


    }

    private void insertBookmark(ShayariModel shayariModel) {
        new Thread(() -> db.shayariDao().insert(shayariModel)).start();
    }

    private void deleteBookmark(String shayariId) {
        new Thread(() -> db.shayariDao().deleteById(shayariId)).start();
    }

    public void setShayariList(List<ShayariModel> shayariList) {
        this.shayariModels = shayariList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return shayariModels.size();
    }

    public class ShayariViewHolder extends RecyclerView.ViewHolder {
        ItemShayariBinding binding;

        public ShayariViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemShayariBinding.bind(itemView);
        }
    }

}
