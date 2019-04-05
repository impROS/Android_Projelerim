package impros.cale.yksgerisayim.Pojos;
public class SettingsPojo {
    private boolean isBildirimAktiflik = true;
    private boolean isTitresim = false;
    private boolean isSes = true;
    private int bildirimsaat = 18;
    private int bildirimDakika = 00;
    private int bildirimAralik = 1;
    private int girisSayisi = 0;

    private String userName="";

    public int getBildirimAralik() {
        return bildirimAralik;
    }

    public void setBildirimAralik(int bildirimAralik) {
        this.bildirimAralik = bildirimAralik;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGirisSayisi() {
        return girisSayisi;
    }

    public void setGirisSayisi(int girisSayisi) {
        this.girisSayisi = girisSayisi;
    }

    public boolean isBildirimAktiflik() {
        return isBildirimAktiflik;
    }

    public void setBildirimAktiflik(boolean bildirimAktiflik) {
        isBildirimAktiflik = bildirimAktiflik;
    }

    public boolean isTitresim() {
        return isTitresim;
    }

    public void setTitresim(boolean titresim) {
        isTitresim = titresim;
    }

    public boolean isSes() {
        return isSes;
    }

    public void setSes(boolean ses) {
        isSes = ses;
    }

    public int getBildirimsaat() {
        return bildirimsaat;
    }

    public void setBildirimsaat(int bildirimsaat) {
        this.bildirimsaat = bildirimsaat;
    }

    public int getBildirimDakika() {
        return bildirimDakika;
    }

    public void setBildirimDakika(int bildirimDakika) {
        this.bildirimDakika = bildirimDakika;
    }


}
