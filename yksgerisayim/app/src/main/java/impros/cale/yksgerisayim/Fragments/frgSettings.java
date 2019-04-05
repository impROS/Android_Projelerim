package impros.cale.yksgerisayim.Fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import impros.cale.yksgerisayim.Helpers.UI;
import impros.cale.yksgerisayim.MainActivity;
import impros.cale.yksgerisayim.Notification.NotificationHelper;
import impros.cale.yksgerisayim.Pojos.SettingsPojo;
import impros.cale.yksgerisayim.R;


import static android.view.View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION;

public class frgSettings extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    TextView txtBildirimSaat, txtUserName;
    Button btnBildirimAyarKaydet;

    Switch chkBildirimAktiflik, chkBildirimTitresim, chkBildirimSes;
    NumberPicker np;
    Activity act;
    int saat = 18;
    int dakika = 00;
    int bildirimAralik = 1;
    ArrayList<View> views;
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.frg_settings, container, false);
        act = frgSettings.this.getActivity();

        txtBildirimSaat = root.findViewById(R.id.txtBildirimSaat);
        txtBildirimSaat = root.findViewById(R.id.txtBildirimSaat);
        txtUserName = root.findViewById(R.id.txtUserName);
        chkBildirimAktiflik = root.findViewById(R.id.chkBildirimAktiflik);
        chkBildirimTitresim = root.findViewById(R.id.chkBildirimTitresim);
        chkBildirimSes = root.findViewById(R.id.chkBildirimSes);
        btnBildirimAyarKaydet = root.findViewById(R.id.btnBildirimAyarKaydet);
        np = root.findViewById(R.id.numberPicker);

        views = new ArrayList<>();
        root.findViewsWithText(views, "tagBildirim", FIND_VIEWS_WITH_CONTENT_DESCRIPTION);


        np.setMinValue(2);
        np.setMaxValue(20);
        np.setOnValueChangedListener(onValueChangeListener);


        saat = UI.getSettings(act).getBildirimsaat();
        dakika = UI.getSettings(act).getBildirimDakika();
        bildirimAralik = UI.getSettings(act).getBildirimAralik();
        chkBildirimAktiflik.setChecked(UI.getSettings(act).isBildirimAktiflik());
        chkBildirimSes.setChecked(UI.getSettings(act).isSes());
        chkBildirimTitresim.setChecked(UI.getSettings(act).isTitresim());
        bildirimAyarEnable(chkBildirimAktiflik.isChecked());

        txtBildirimSaat.setText(saat + ":" + dakika);
        np.setValue(bildirimAralik);

        txtBildirimSaat.setOnClickListener(this);
        btnBildirimAyarKaydet.setOnClickListener(this);


        chkBildirimAktiflik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bildirimAyarEnable(isChecked);
            }
        });

        return root;
    }

    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    UI.log("i : " + ",i1 : " + i1);
                    bildirimAralik = i1;
                }
            };

    public void bildirimAyarEnable(boolean isEnable) {
        if (isEnable) {
            for (View view : views) {
                view.setEnabled(true);
            }
        } else {
            for (View view : views) {
                view.setEnabled(false);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtBildirimSaat:
                final Calendar takvim = Calendar.getInstance();
                saat = takvim.get(Calendar.HOUR_OF_DAY);
                dakika = takvim.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(act,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                saat = hourOfDay;
                                dakika = minute;
                                txtBildirimSaat.setText(hourOfDay + ":" + minute);
                            }
                        }, saat, dakika, true);

                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", tpd);
                tpd.show();
                break;

            case R.id.btnBildirimAyarKaydet:
                UI.getSettings(act).setBildirimAktiflik(chkBildirimAktiflik.isChecked());
                UI.getSettings(act).setBildirimsaat(saat);
                UI.getSettings(act).setBildirimDakika(dakika);
                UI.getSettings(act).setSes(chkBildirimSes.isChecked());
                UI.getSettings(act).setTitresim(chkBildirimTitresim.isChecked());
                UI.getSettings(act).setUserName(txtUserName.getText().toString());
                UI.getSettings(act).setBildirimAralik(bildirimAralik);
                UI.saveSettings(act, UI.getSettings(act));

                NotificationHelper.createNotification(act, saat, dakika, bildirimAralik);

                MainActivity.changeUserName(txtUserName.getText().toString());

                Fragment newFragment = new frgHomePage();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        UI.log("value" + oldVal);
    }
}
