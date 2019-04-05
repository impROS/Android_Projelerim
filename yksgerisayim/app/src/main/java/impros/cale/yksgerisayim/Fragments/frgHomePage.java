package impros.cale.yksgerisayim.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import impros.cale.yksgerisayim.Helpers.GeriSayim;
import impros.cale.yksgerisayim.R;

public class frgHomePage extends Fragment {
    Activity act;
    View lytPrgSaniye, lytPrgDakika, lytPrgSaat, lytPrgGun;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frg_home_page, container, false);
        act = frgHomePage.this.getActivity();

        lytPrgSaniye = root.findViewById(R.id.lytPrgSaniye);
        lytPrgDakika = root.findViewById(R.id.lytPrgDakika);
        lytPrgSaat = root.findViewById(R.id.lytPrgSaat);
        lytPrgGun = root.findViewById(R.id.lytPrgGun);

        sayacBasla("2019-06-23 10:15:00");
        return root;
    }

    public void sayacBasla(String yksTarih) {
        GeriSayim.geriSayim(act, yksTarih, lytPrgSaniye, lytPrgDakika, lytPrgSaat, lytPrgGun);
    }
}
