package net.jahanco.jahandaru.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import net.jahanco.jahandaru.Activities.ItemActivity;
import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Models.ItemEntity;
import net.jahanco.jahandaru.R;

import java.io.File;
import java.util.ArrayList;


public class MedicalItemsAdapter
        extends RecyclerView.Adapter<MedicalItemsAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<ItemEntity> itemEntities;

    public MedicalItemsAdapter(Context paramContext, ArrayList<ItemEntity> itemEntities) {
        this.context = paramContext;
        this.itemEntities = itemEntities;
        Log.i("siz", itemEntities.size() + "");
    }

    public int getItemCount() {
        return this.itemEntities.size();
    }

    public void onBindViewHolder(ItemViewHolder paramItemViewHolder, int paramInt) {
        paramItemViewHolder.bind(this.itemEntities.get(paramInt));
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new ItemViewHolder(LayoutInflater.from(this.context).inflate(R.layout.medical_adapter_2, paramViewGroup, false));
    }

    public class ItemViewHolder
            extends RecyclerView.ViewHolder
            implements OnClickListener {

        private CustomTextView itemTitle;
        private ImageView itemImage;
        private RelativeLayout itemRoot;
        public ItemViewHolder(View paramView) {
            super(paramView);
            itemTitle = (CustomTextView) paramView.findViewById(R.id.item_title);
            itemImage = (ImageView) paramView.findViewById(R.id.item_image);
            itemRoot= (RelativeLayout) paramView.findViewById(R.id.item_root);
            itemRoot.setOnClickListener(this);
        }

        public void bind(ItemEntity paramStoryItem) {
            itemTitle.setText(paramStoryItem.getName().replace("\r\n", "").replace("  ", ""));
                String imageUri = App.EXTERNAL_STORAGE_URI + "images/" + paramStoryItem.getId();
                Uri uri = Uri.fromFile(new File(imageUri));

                Picasso.with(context).load(uri)
                        .placeholder(R.drawable.placeholder)
                        .into(itemImage);

        }

        public void onClick(View paramView) {
            Intent intent=new Intent(App.context, ItemActivity.class);
            intent.putExtra("id",itemEntities.get(getLayoutPosition()).getId()+"");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.context.startActivity(intent);
        }
    }

}

