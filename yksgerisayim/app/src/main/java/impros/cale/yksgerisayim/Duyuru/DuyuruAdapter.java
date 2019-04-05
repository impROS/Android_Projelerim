package impros.cale.yksgerisayim.Duyuru;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import impros.cale.yksgerisayim.R;


public class DuyuruAdapter extends FirestoreRecyclerAdapter<DuyuruPojo, DuyuruAdapter.DuyuruHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DuyuruAdapter(@NonNull FirestoreRecyclerOptions<DuyuruPojo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final DuyuruHolder holder, int position, @NonNull DuyuruPojo model) {
        holder.txtBaslik.setText(model.getBaslik());
        holder.txtIcerik.setText(model.getIcerik());
        holder.crdMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtIcerik.getMaxLines() == 1) {
                    holder.txtIcerik.setSingleLine(false);
                } else {
                    holder.txtIcerik.setSingleLine(true);
                }
            }
        });
    }

    @NonNull
    @Override
    public DuyuruHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        return new DuyuruHolder(view);
    }

    class DuyuruHolder extends RecyclerView.ViewHolder {
        TextView txtBaslik;
        TextView txtIcerik;
        TextView txtID;
        CardView crdMain;

        public DuyuruHolder(View view) {
            super(view);
            txtBaslik = view.findViewById(R.id.txtBaslik);
            txtIcerik = view.findViewById(R.id.txtIcerik);
            crdMain = view.findViewById(R.id.crdMain);
        }
    }
}
