package impros.cale.yksgerisayim.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import impros.cale.yksgerisayim.Duyuru.DuyuruAdapter;
import impros.cale.yksgerisayim.Duyuru.DuyuruPojo;
import impros.cale.yksgerisayim.R;

public class frgNotifications extends Fragment {
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference duyuruRef = database.collection("Notifications");
    private DuyuruAdapter duyuruAdapter;
    RecyclerView recyclerView;
    Activity act;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_notifications, container, false);
        recyclerView = rootView.findViewById(R.id.rcyNotifications);
        act = frgNotifications.this.getActivity();
        setupRecyclerView();
        return rootView;
    }
    private void setupRecyclerView() {
        try {
            //Query query = duyuruRef.orderBy("id", Query.Direction.DESCENDING);
            Query query = duyuruRef;
            FirestoreRecyclerOptions<DuyuruPojo> options = new FirestoreRecyclerOptions.Builder<DuyuruPojo>()
                    .setQuery(query, DuyuruPojo.class)
                    .build();
            duyuruAdapter = new DuyuruAdapter(options);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(act));
            recyclerView.setAdapter(duyuruAdapter);

        } catch (Exception ex) {
            Log.d("x3k : ", ex.getMessage());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            duyuruAdapter.startListening();

        } catch (Exception ex) {
            Log.d("x3k :", ex.getMessage());
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            duyuruAdapter.stopListening();

        } catch (Exception ex) {
            Log.d("x3k :", ex.getMessage());
        }
    }
}
