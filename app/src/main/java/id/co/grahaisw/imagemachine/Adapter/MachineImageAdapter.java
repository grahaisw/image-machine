package id.co.grahaisw.imagemachine.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import id.co.grahaisw.imagemachine.ImageFullScreenActivity;
import id.co.grahaisw.imagemachine.R;

public class MachineImageAdapter extends RecyclerView.Adapter<MachineImageAdapter.MachineImageViewHolder> {

    private final Context context;
    private final ArrayList<Uri> uriArrayList;

    public MachineImageAdapter(Context context, ArrayList<Uri> uriArrayList) {
        this.context = context;
        this.uriArrayList = uriArrayList;
    }

    public class MachineImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView thumbnail;

        public MachineImageViewHolder(final View view){
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            FrameLayout delete = view.findViewById(R.id.delete);
            thumbnail.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.thumbnail) {
                Intent intent = new Intent(context, ImageFullScreenActivity.class);
                intent.putExtra("uri", uriArrayList.get(getAdapterPosition()).toString());
                intent.putExtra("transition", ViewCompat.getTransitionName(thumbnail));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context, thumbnail, Objects.requireNonNull(ViewCompat.getTransitionName(thumbnail)));
                context.startActivity(intent, options.toBundle());
            }else if(view.getId() == R.id.delete){
                uriArrayList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), getItemCount());
            }
        }
    }

    @NonNull
    @Override
    public MachineImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.machine_thumbnail, viewGroup, false);
        return new MachineImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineImageViewHolder holder, int position) {
        holder.thumbnail.setImageURI(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }
}
