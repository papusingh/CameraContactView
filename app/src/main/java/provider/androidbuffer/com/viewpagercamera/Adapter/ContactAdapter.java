package provider.androidbuffer.com.viewpagercamera.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import provider.androidbuffer.com.viewpagercamera.CallBack.OnCall;
import provider.androidbuffer.com.viewpagercamera.Model.ContactModel;
import provider.androidbuffer.com.viewpagercamera.R;

/**
 * Created by incred-dev on 31/8/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    List<ContactModel> contactModelList;
    private OnCall onCall;

    public ContactAdapter(List<ContactModel> contactModelList, OnCall onCall) {
        this.contactModelList = contactModelList;
        this.onCall = onCall;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ContactModel contactModel = contactModelList.get(holder.getAdapterPosition());

        if (contactModel != null){

            if (contactModel.getName() != null){
                holder.tvName.setText(contactModel.getName());
            }

            if (contactModel.getNumber() != null){
                holder.tvNumber.setText(contactModel.getNumber());
            }

            holder.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // call the action call
                    onCall.onCallClicked(contactModel.getNumber());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvNumber;
        Button btnCall;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }
}
