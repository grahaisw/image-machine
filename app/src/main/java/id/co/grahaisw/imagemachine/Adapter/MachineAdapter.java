package id.co.grahaisw.imagemachine.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import id.co.grahaisw.imagemachine.DatabaseHelper;
import id.co.grahaisw.imagemachine.MachineDetailActivity;
import id.co.grahaisw.imagemachine.Model.Machine;
import id.co.grahaisw.imagemachine.R;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {

    private final Context context;
    private final List<Machine> machineList;

    public MachineAdapter(Context context, List<Machine> machineList) {
        this.context = context;
        this.machineList = machineList;
    }

    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup viewGroup, final int selectedItem) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.machine_list, viewGroup, false);
        return new MachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MachineViewHolder customViewHolder, final int position) {
        Machine machine = machineList.get(position);
        customViewHolder.name.setText(machine.getName());
        customViewHolder.type.setText(machine.getType());
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

    public class MachineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name;
        protected TextView type;
        protected FrameLayout edit;
        protected FrameLayout delete;

        public MachineViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.name);
            type = view.findViewById(R.id.type);
            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View view) {
            Machine machine = machineList.get(getAdapterPosition());
            if(view.getId() == R.id.edit){
                Intent intent = new Intent(context, MachineDetailActivity.class);
                intent.putExtra("id", machine.getId());
                intent.putExtra("menu", "machine_data");
                context.startActivity(intent);
            }else{
                DatabaseHelper db = new DatabaseHelper(context);
                db.deleteMachine(machine.getId());
                machineList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), getItemCount());
            }
        }
    }
}
