package cont;
import java.time.LocalDate;

public class Peminjaman {
    private int id;
    private String isbn;
    private String nama;
    private String nomorAnggota;
    private String buku;
    private LocalDate tanggalPeminjaman;
    private LocalDate tanggalPengembalian;

    public Peminjaman(int id, String nomorAnggota, String isbn, LocalDate tanggalPeminjaman, LocalDate tanggalPengembalian) {
        this.id = id;
        this.nomorAnggota = nomorAnggota;
        this.isbn = isbn;
        this.tanggalPeminjaman = tanggalPeminjaman;
        this.tanggalPengembalian = tanggalPengembalian;
    }
    
    // Metode getter untuk properti 'id'
    public int getId() {
        return id;
    }
    
    // Metode getter untuk properti 'isbn'
    public String getIsbn() {
        return isbn;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorAnggota() {
        return nomorAnggota;
    }

    public void setNomorAnggota(String nomorAnggota) {
        this.nomorAnggota = nomorAnggota;
    }

    public String getBuku() {
        return buku;
    }

    public void setBuku(String buku) {
        this.buku = buku;
    }

    public LocalDate getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public void setTanggalPeminjaman(LocalDate tanggalPeminjaman) {
        this.tanggalPeminjaman = tanggalPeminjaman;
    }

    public LocalDate getTanggalPengembalian() {
        return tanggalPengembalian;
    }

    public void setTanggalPengembalian(LocalDate tanggalPengembalian) {
        this.tanggalPengembalian = tanggalPengembalian;
    }
}
