package com.yemek.tunceli.impros.tunceliyemeklistesi;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class frmMain extends AppCompatActivity {
    private Button btnNext, btnBack;
    private TextView txtBirinciYemek, txtIkinciYemek, txtUcuncuYemek, txtDorduncuYemek, txtTarih, txtGun;
    private TextView txtYemekhaneDurum;
    private ArrayList<String> arrTarih, arrBirinciYemek, arrIkinciYemek, arrUcuncuYemek, arrDorduncuYemek;
    private int yemekIndex = 0;
    boolean yemekGunuMu = false, veriCekildiMi = false;
    private String strBugun = "-";
    private int bugunIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_frm_main);

            arrTarih = new ArrayList<>();
            arrBirinciYemek = new ArrayList<>();
            arrIkinciYemek = new ArrayList<>();
            arrUcuncuYemek = new ArrayList<>();
            arrDorduncuYemek = new ArrayList<>();

            btnNext = (Button) findViewById(R.id.btnNext);
            btnBack = (Button) findViewById(R.id.btnBack);

            txtGun = (TextView) findViewById(R.id.txtGun);
            txtBirinciYemek = (TextView) findViewById(R.id.txtBirinciYemek);
            txtIkinciYemek = (TextView) findViewById(R.id.txtIkinciYemek);
            txtUcuncuYemek = (TextView) findViewById(R.id.txtUcuncuYemek);
            txtDorduncuYemek = (TextView) findViewById(R.id.txtDorduncuYemek);
            txtTarih = (TextView) findViewById(R.id.txtTarih);

            txtYemekhaneDurum = (TextView) findViewById(R.id.txtYemekhaneDurum);

            if (internetVarMi()) {
                basla();

            } else {
                txtYemekhaneDurum.setText("İnternet Yok");
                txtTarih.setText("Lütfen İnternet Bağlantınızı Açıp Tıklayın.");
                txtBirinciYemek.setText("Veriler Yüklenemedi..");
                txtTarih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (internetVarMi()) {
                            try {
                                basla();

                            } catch (Exception ex) {

                                uyari("Hata ", ex);
                            }


                        }

                    }
                });
            }

        } catch (Exception ex) {
            txtBirinciYemek.setText(ex.getMessage());

        }


    }

    private void basla() {

        butonKontrol();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new veriGetir().yemekDegistir(++yemekIndex);
                butonKontrol();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new veriGetir().yemekDegistir(--yemekIndex);
                butonKontrol();
            }
        });
        txtTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!strBugun.equalsIgnoreCase("-")) {
                    txtTarih.setText(strBugun);
                    new veriGetir().yemekDegistir(bugunIndex);
                    yemekIndex = bugunIndex;
                    butonKontrol();

                }

            }
        });
        new veriGetir().execute();
        Timer _t = new Timer();

        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run() {
                        new veriGetir().yemekDurumu();
                    }
                });
            }
        }, 5000, 1000);
    }

    private boolean internetVarMi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void butonKontrol() {
        if (veriCekildiMi) {
            if (yemekIndex < 1) {
                btnBack.setEnabled(false);
            } else if (yemekIndex > arrTarih.size() - 2) {
                btnNext.setEnabled(false);

            } else {
                btnNext.setEnabled(true);
                btnBack.setEnabled(true);
            }
            new veriGetir().gunKontrol();

        }

    }

    private static String url = "http://improsyazilim.com/2017/11/14/min-max-normallestirmesijava-kodu/";
    ;
    //"www.munzur.edu.tr/birimler/idari/sks/Pages/yemeklistesi.aspx/";
    //"http://improsyazilim.com/2017/11/14/min-max-normallestirmesijava-kodu/";

    private class veriGetir extends AsyncTask<Void, Void, Void> {

        String title;

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                // URLConnection conn = new URL(url).openConnection();

                // InputStream in = conn.getInputStream();
                // title = convertStreamToString(in);
                //  title=getDataFromUrl("https://www.munzur.edu.tr/birimler/idari/sks/Pages/yemeklistesi.aspx");

                title = getData("https://www.munzur.edu.tr/birimler/idari/sks/Pages/yemeklistesi.aspx");
                int baslangicIndex = title.indexOf("table class=\"slide-image\"");
                int sonIndex = title.indexOf("</form>");
                title = title.substring(baslangicIndex, sonIndex);

                title = title.replaceAll("</td>", "");
                title = title.replaceAll("<td>", "");
                title = title.replaceAll("<table class=\"slide-image\">", "");
                title = title.replaceAll("<tbody><tr>", "");
                title = title.replaceAll("<tr>", "");
                title = title.replaceAll("</table>", "");
                title = title.replaceAll("</b>", "");
                title = title.replaceAll("<b>", "");
                title = title.replaceAll("</hr>", "");
                title = title.replaceAll("<hr>", "");
                title = title.replaceAll("<tr>", "");
                title = title.replaceAll("</tr>", "");
                title = title.replaceAll("<hr/>", "");
                //   title = title.replaceAll(" n ", "");
                arrTarih = getTarihArray(title);
                arrBirinciYemek = getBirinciYemekArray(title);
                arrIkinciYemek = getIkinciYemekArray(title);
                arrUcuncuYemek = getUcuncuYemekArray(title);
                arrDorduncuYemek = getDorduncuYemekArray(title);

                // } catch (MalformedURLException e) {
                //  txtBirinciYemek.setText(e.getMessage());
                // } catch (IOException e) {
                //   txtBirinciYemek.setText(e.getMessage());
            } catch (Exception ex) {
                txtBirinciYemek.setText(ex.getMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            veriCekildiMi = true;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String gun = sdf.format(new Date());
            for (int i = 0; i < arrTarih.size(); i++) {
                if (arrTarih.get(i).equalsIgnoreCase(gun)) {

                    yemekIndex = i;
                    bugunIndex = yemekIndex;
                }
            }
            yemekDegistir(yemekIndex);

            butonKontrol();
            //  txtIkinciYemek.setText("Veri : " + title);
        }

        private void trustAllHosts() {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }};

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getData(String uri) {

            BufferedReader reader = null;

            try {

                URL url = new URL(uri);
                HttpURLConnection con = null;

                URL testUrlHttps = new URL(uri);
                if (testUrlHttps.getProtocol().toLowerCase().equals("https")) {
                    trustAllHosts();
                    HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                    https.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    con = https;
                } else {
                    con = (HttpURLConnection) url.openConnection();
                }


                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "windows-1254"));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            }
        }


        private ArrayList<String> getTarihArray(String rawData) {
            String strKelime = "Tarih:";
            ArrayList<String> arrTarihData = new ArrayList<>();
            int tarihIndex = rawData.indexOf(strKelime);
            while (tarihIndex > 0) {
                arrTarihData.add(rawData.substring(tarihIndex + strKelime.length(), tarihIndex + 17).trim());
                Log.d("Veri:" + rawData.substring(tarihIndex + strKelime.length(), tarihIndex + 17), ".Tarih İndex ");
                rawData = rawData.substring(tarihIndex + 6, rawData.length());
                tarihIndex = rawData.indexOf(strKelime);
            }
            return arrTarihData;
        }

        private ArrayList<String> getBirinciYemekArray(String rawData) {
            String strKelime = "1.Yemek:";
            String strAyrac = "Kalori";
            ArrayList<String> arrTarihData = new ArrayList<>();
            int tarihIndex = rawData.indexOf(strKelime);
            int ayracIndex = rawData.indexOf(strAyrac);

            while (tarihIndex > 0) {
                arrTarihData.add(rawData.substring(tarihIndex + strKelime.length(), ayracIndex));
                Log.d("Veri:" + rawData.substring(tarihIndex + strKelime.length(), ayracIndex), "1.Yemek İndex ");
                rawData = rawData.substring(ayracIndex + 5, rawData.length());
                tarihIndex = rawData.indexOf(strKelime);
                ayracIndex = rawData.indexOf(strAyrac);

            }
            return arrTarihData;
        }

        private ArrayList<String> getIkinciYemekArray(String rawData) {
            String strKelime = "2.Yemek:";
            String strAyrac = "3.Yemek";
            ArrayList<String> arrTarihData = new ArrayList<>();
            int tarihIndex = rawData.indexOf(strKelime);
            int ayracIndex = rawData.indexOf(strAyrac);

            while (tarihIndex > 0) {
                arrTarihData.add(rawData.substring(tarihIndex + strKelime.length(), ayracIndex));
                Log.d("Veri:" + rawData.substring(tarihIndex + strKelime.length(), ayracIndex), "2.Yemek İndex ");
                rawData = rawData.substring(ayracIndex + 5, rawData.length());
                tarihIndex = rawData.indexOf(strKelime);
                ayracIndex = rawData.indexOf(strAyrac);

            }
            return arrTarihData;
        }

        private ArrayList<String> getUcuncuYemekArray(String rawData) {
            String strKelime = "3.Yemek:";
            String strAyrac = "4.Yemek";
            ArrayList<String> arrTarihData = new ArrayList<>();
            int tarihIndex = rawData.indexOf(strKelime);
            int ayracIndex = rawData.indexOf(strAyrac);

            while (tarihIndex > 0) {
                arrTarihData.add(rawData.substring(tarihIndex + strKelime.length(), ayracIndex));
                Log.d("Veri:" + rawData.substring(tarihIndex + strKelime.length(), ayracIndex), "3.Yemek İndex ");
                rawData = rawData.substring(ayracIndex + 5, rawData.length());
                tarihIndex = rawData.indexOf(strKelime);
                ayracIndex = rawData.indexOf(strAyrac);

            }
            return arrTarihData;
        }

        private ArrayList<String> getDorduncuYemekArray(String rawData) {
            String strKelime = "4.Yemek:";
            String strAyrac = "<hr />";
            ArrayList<String> arrTarihData = new ArrayList<>();
            int tarihIndex = rawData.indexOf(strKelime);
            int ayracIndex = rawData.indexOf(strAyrac);

            while (tarihIndex > 0) {
                try {

                    arrTarihData.add(rawData.substring(tarihIndex + strKelime.length(), ayracIndex));
                    Log.d("Veri:" + rawData.substring(tarihIndex + strKelime.length(), ayracIndex), "4.Yemek İndex ");
                    rawData = rawData.substring(ayracIndex + 5, rawData.length());
                    tarihIndex = rawData.indexOf(strKelime);
                    ayracIndex = rawData.indexOf(strAyrac);
                } catch (Exception ex) {

                    Log.d("Hata : 4.Yemek  : ", ex.getMessage().toString());
                }


            }
            return arrTarihData;
        }


        private void gunKontrol() {
            String str_date = txtTarih.getText() + "";
            DateFormat formatter;
            Date date = null;
            formatter = new SimpleDateFormat("dd.MM.yyyy");
            try {
                date = formatter.parse(str_date);
            } catch (ParseException e) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    String gun = sdf.format(new Date());
                    date = formatter.parse(gun);
                } catch (Exception ex) {
                    txtBirinciYemek.setText("Hata " + e.getMessage());
                }


            }


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            String[] days = new String[]{"Pazar", "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi"};

            txtGun.setText(days[dayOfWeek - 1]);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String gun = sdf.format(new Date());

            if (gun.equalsIgnoreCase(txtTarih.getText() + "")) {
                txtTarih.setText("Bugün");
                yemekGunuMu = true;
                strBugun = txtTarih.getText() + "";
              //  uyari("Yemek Durumu1 : " + yemekGunuMu);
//
                //uyari("Yemek Durumu2 : " + yemekGunuMu);

            }
            if (veriCekildiMi && !yemekGunuMu) {
                yemekGunuMu = false;
                txtYemekhaneDurum.setText("Bugün Yemek Yok");
            }


            yemekDurumu();


        }


        private void yemekDurumu() {
            if (yemekGunuMu) {
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("HH");
                    String strSaat = sdf.format(new Date());
                    int saat = 99;
                    try {
                        saat = Integer.parseInt(strSaat);
                    } catch (Exception ex) {
                        uyari("Parse : ", ex);

                    }

                    sdf = new SimpleDateFormat("mm");
                    strSaat = sdf.format(new Date());
                    int dakika = 99;
                    try {
                        dakika = Integer.parseInt(strSaat);
                    } catch (Exception ex) {
                        uyari("Parse : ", ex);

                    }
                    //txtGun.setText(saat + " : " + dakika);

                    if (yemekGunuMu) {
                        if (saat > 10 && saat <= 12) {
                            durumDegis(true);
                            // uyari("11 : ");
                        } else if (saat > 14 && saat < 17) {
                            //  uyari("2 : ");
                            if (saat == 15 && dakika < 30) {
                                durumDegis(false);
                                // uyari("3 : ");
                            } else {
                                durumDegis(true);
                                // uyari("4 : ");
                            }

                        }else{
                            durumDegis(false);
                        }

                        // txtGun.setText(saat + "");
                    }
                } catch (Exception ex) {
                    uyari("Son  : " + ex);
                    //txtIkinciYemek.setText();
                }
            }


        }

        private void durumDegis(boolean yemekVarMi) {
            if (yemekVarMi) {
                txtYemekhaneDurum.setTextColor(Color.GREEN);
                txtYemekhaneDurum.setText("Yemekhane Açık");
            } else {

                txtYemekhaneDurum.setTextColor(Color.RED);
                txtYemekhaneDurum.setText("Yemekhane Kapalı");
            }
        }

        private void yemekDegistir(int index) {
            txtTarih.setText(arrTarih.get(index).trim());
            txtBirinciYemek.setText(arrBirinciYemek.get(index).trim());
            txtIkinciYemek.setText(arrIkinciYemek.get(index).trim());
            txtUcuncuYemek.setText(arrUcuncuYemek.get(index).trim());
            txtDorduncuYemek.setText(arrDorduncuYemek.get(index).trim());

        }
    }

    private void uyari(String strMesaj) {

        Toast.makeText(getApplicationContext(), strMesaj, Toast.LENGTH_SHORT).show();
    }

    private void uyari(String strMesaj, Exception ex) {

        Toast.makeText(getApplicationContext(), strMesaj + " : " + ex.getStackTrace()[0].getLineNumber() + " : " + ex.getCause() + " : " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
