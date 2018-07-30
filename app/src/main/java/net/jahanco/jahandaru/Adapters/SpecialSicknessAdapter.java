package net.jahanco.jahandaru.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.jahanco.jahandaru.Activities.SpecialItemActivity;
import net.jahanco.jahandaru.App;
import net.jahanco.jahandaru.Controls.CustomTextView;
import net.jahanco.jahandaru.Models.SpecialSick;
import net.jahanco.jahandaru.R;

import java.util.ArrayList;


public class SpecialSicknessAdapter
        extends RecyclerView.Adapter<SpecialSicknessAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<SpecialSick> sicks;
    private OnOpenDialogSet onOpenDialogSet;
    public SpecialSicknessAdapter(Context paramContext, ArrayList<SpecialSick> sicks,OnOpenDialogSet onOpenDialogSet) {
        this.context = paramContext;
        this.onOpenDialogSet=onOpenDialogSet;
        this.sicks = sicks;
    }

    public int getItemCount() {
        return this.sicks.size();
    }

    public void onBindViewHolder(ItemViewHolder paramItemViewHolder, int paramInt) {
        paramItemViewHolder.bind(this.sicks.get(paramInt));
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new ItemViewHolder(LayoutInflater.from(this.context).inflate(R.layout.sickness_adapter, paramViewGroup, false));
    }

    public class ItemViewHolder
            extends RecyclerView.ViewHolder
            implements OnClickListener {

        private CustomTextView title;
        private LinearLayout root;

        public ItemViewHolder(View paramView) {
            super(paramView);
            title = (CustomTextView) paramView.findViewById(R.id.sick_title);
            root = (LinearLayout) itemView.findViewById(R.id.sick_root);
            root.setOnClickListener(this);
        }

        public void bind(SpecialSick sick) {
            title.setText(sick.getName());
        }

        public void onClick(View paramView) {
            if (App.prefManager.getPreference("premium").equals("-1")) {
                if(onOpenDialogSet!=null){
                    onOpenDialogSet.openDialogSet();
                }
            } else {
                Intent intent = new Intent(App.context, SpecialItemActivity.class);
                intent.putExtra("id", sicks.get(getLayoutPosition()).getId() + "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);
            }

        }
    }
    public interface OnOpenDialogSet{
        public void openDialogSet();
    }
}

