package impros.cale.yksgerisayim.Duyuru;

public class DuyuruPojo {
    private String baslik;
    private String icerik;

    public DuyuruPojo() {
    }

    public DuyuruPojo(String baslik, String icerik) {
        this.baslik = baslik;
        this.icerik = icerik;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }


}
