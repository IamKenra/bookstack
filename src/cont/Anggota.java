package cont;

import javafx.beans.property.*;

public class Anggota {
    private StringProperty nomorAnggota;
    private StringProperty nama;
    private IntegerProperty noTelepon;
    private StringProperty alamat;

    public Anggota(String nomorAnggota, String nama, int noTelepon, String alamat) {
        this.nomorAnggota = new SimpleStringProperty(nomorAnggota);
        this.nama = new SimpleStringProperty(nama);
        this.noTelepon = new SimpleIntegerProperty(noTelepon);
        this.alamat = new SimpleStringProperty(alamat);
    }

    public String getNomorAnggota() {
        return nomorAnggota.get();
    }

    public void setNomorAnggota(String nomorAnggota) {
        this.nomorAnggota.set(nomorAnggota);
    }

    public StringProperty nomorAnggotaProperty() {
        return nomorAnggota;
    }

    public String getNama() {
        return nama.get();
    }

    public void setNama(String nama) {
        this.nama.set(nama);
    }

    public StringProperty namaProperty() {
        return nama;
    }

    public int getNoTelepon() {
        return noTelepon.get();
    }

    public void setNoTelepon(int noTelepon) {
        this.noTelepon.set(noTelepon);
    }

    public IntegerProperty noTeleponProperty() {
        return noTelepon;
    }

    public String getAlamat() {
        return alamat.get();
    }

    public void setAlamat(String alamat) {
        this.alamat.set(alamat);
    }

    public StringProperty alamatProperty() {
        return alamat;
    }
}
