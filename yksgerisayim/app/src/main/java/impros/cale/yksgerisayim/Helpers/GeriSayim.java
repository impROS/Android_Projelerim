package impros.cale.yksgerisayim.Helpers;


import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import impros.cale.yksgerisayim.R;


public class GeriSayim {
    private static CountDownTimer countDownTimer;
    private static long sayacMillis;

    public static void geriSayim(Activity act, String sayacTarih, final View lytSaniye, final View lytDakika,
                                 final View lytSaat, final View lytGun) {

        final CircularProgressBar prgSaniye, prgDakika, prgSaat, prgGun;
        final TextView txtSaniye, txtDakika, txtSaat, txtGun;

        txtSaniye = lytSaniye.findViewById(R.id.tmpTxt);
        prgSaniye = lytSaniye.findViewById(R.id.tmpPrg);
        setupProgressBar(act, prgSaniye, 60f);

        txtDakika = lytDakika.findViewById(R.id.tmpTxt);
        prgDakika = lytDakika.findViewById(R.id.tmpPrg);
        setupProgressBar(act, prgDakika, 60f);

        txtSaat = lytSaat.findViewById(R.id.tmpTxt);
        prgSaat = lytSaat.findViewById(R.id.tmpPrg);
        setupProgressBar(act, prgSaat, 24f);

        txtGun = lytGun.findViewById(R.id.tmpTxt);
        prgGun = lytGun.findViewById(R.id.tmpPrg);
        setupProgressBar(act, prgGun, 365f);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(sayacTarih);
            Calendar gun = Calendar.getInstance();
            sayacMillis = mDate.getTime() - gun.getTimeInMillis();
            UI.log("Sayac : " + sayacMillis);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (countDownTimer != null) {
            UI.log("Timer Durduruldu..");
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(sayacMillis, 1000) {
            public void onTick(long millisUntilFinished) {

                float saniye = Float.parseFloat(String.format("%d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                float dakika = Float.parseFloat(String.format("%d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % (60)));
                float saat = Float.parseFloat(String.format("%d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24));
                float gun = Float.parseFloat(String.format("%d", TimeUnit.MILLISECONDS.toDays(millisUntilFinished) % 365));

                txtSaniye.setText((int) saniye + "\nSaniye");
                txtDakika.setText((int) dakika + "\nDakika");
                txtSaat.setText((int) saat + "\nSaat");
                txtGun.setText((int) gun + "\nGün");

                prgSaniye.setProgress(60 - saniye);
                prgDakika.setProgress(60 - dakika);
                prgSaat.setProgress(24 - saat);
                prgGun.setProgress(365 - gun);
            }

            public void onFinish() {

            }
        }.start();
    }

    private static void setupProgressBar(Activity act, CircularProgressBar prgTmp, float maxValue) {
        prgTmp.setProgressMax(maxValue);
        prgTmp.setColor(ContextCompat.getColor(act, R.color.colorToolbar));
        prgTmp.setBackgroundColor(ContextCompat.getColor(act, R.color.fbWhite));
        prgTmp.setProgressBarWidth(act.getResources().getDimension(R.dimen.progressBarWidth));
        prgTmp.setBackgroundProgressBarWidth(act.getResources().getDimension(R.dimen.backgroundProgressBarWidth));
    }
}
