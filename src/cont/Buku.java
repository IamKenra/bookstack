package cont;

public class Buku {
    private String namaBuku;
    private String penulis;
    private int tahunTerbit;
    private String isbn;
    private int nomorRakId;
    private int jumlah;
    private String status;

    public Buku(String namaBuku, String penulis, int tahunTerbit, String isbn, int nomorRakId, int jumlah, String status) {
        this.namaBuku = namaBuku;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
        this.isbn = isbn;
        this.nomorRakId = nomorRakId;
        this.jumlah = jumlah;
        this.status = status;
    }

    public String getNamaBuku() {
        return namaBuku;
    }

    public void setNamaBuku(String namaBuku) {
        this.namaBuku = namaBuku;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public int getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getNomorRakId() {
        return nomorRakId;
    }

    public void setNomorRakId(int nomorRakId) {
        this.nomorRakId = nomorRakId;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
