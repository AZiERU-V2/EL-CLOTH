package com.example.el_cloth

data class Pengguna(
    val idPengguna: String = "",
    val email: String = "",
    val nama: String = "",
    val alamat: String = "",
    val keranjangId: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idPengguna" to idPengguna,
            "email" to email,
            "nama" to nama,
            "alamat" to alamat,
            "keranjangId" to keranjangId
        )
    }
}

data class Produk(
    val idProduk: String = "",
    val nama: String = "",
    val jenis: String = "",
    val deskripsi: String = "",
    val harga: Double = 0.0,
    val kuantitasStok: Int = 0,
    val dibuatPada: Long = System.currentTimeMillis(),
    val ukuran: String = "",
    val warna: String = "",
    val bahan: String = ""
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "idProduk" to idProduk,
            "nama" to nama,
            "jenis" to jenis,
            "deskripsi" to deskripsi,
            "harga" to harga,
            "kuantitasStok" to kuantitasStok,
            "dibuatPada" to dibuatPada,
            "ukuran" to ukuran,
            "warna" to warna,
            "bahan" to bahan
        )
    }
}

data class KeranjangBarang(
    val idKeranjangItems: String = "",
    val idKeranjang: String = "",
    val idProduk: String = "",
    val kuantitas: Int = 0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "idKeranjangItems" to idKeranjangItems,
            "idKeranjang" to idKeranjang,
            "idProduk" to idProduk,
            "kuantitas" to kuantitas
        )
    }
}

data class Keranjang(
    val idKeranjang: String = "",
    val idPengguna: String = "",
    val dibuatPada: Long = System.currentTimeMillis(),
    val items: List<KeranjangBarang> = listOf()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "idKeranjang" to idKeranjang,
            "idPengguna" to idPengguna,
            "dibuatPada" to dibuatPada,
            "items" to items.map { it.toMap() }
        )
    }
}

data class Pesanan(
    val idPemesanan: String = "",
    val idPengguna: String = "",
    val totalJumlah: Double = 0.0,
    val status: String = "Pending",
    val items: List<BarangPesanan> = listOf(),
    val pembayaran: Pembayaran? = null,
    val pengiriman: Pengiriman? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idPemesanan" to idPemesanan,
            "idPengguna" to idPengguna,
            "totalJumlah" to totalJumlah,
            "status" to status,
            "items" to items.map { it.toMap() },
            "pembayaran" to pembayaran?.toMap(),
            "pengiriman" to pengiriman?.toMap()
        )
    }
}

data class BarangPesanan(
    val idPemesananItems: String = "",
    val idPemesanan: String = "",
    val idProduk: String = "",
    val kuantitas: Int = 0,
    val harga: Double = 0.0
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "idPemesananItems" to idPemesananItems,
            "idPemesanan" to idPemesanan,
            "idProduk" to idProduk,
            "kuantitas" to kuantitas,
            "harga" to harga
        )
    }
}

data class Pembayaran(
    val idPembayaran: String = "",
    val idPemesanan: String = "",
    val jumlah: Double = 0.0,
    val metodePembayaran: String = "",
    val status: String = "Pending",
    val dibuatPada: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "idPembayaran" to idPembayaran,
            "idPemesanan" to idPemesanan,
            "jumlah" to jumlah,
            "metodePembayaran" to metodePembayaran,
            "status" to status,
            "dibuatPada" to dibuatPada
        )
    }
}

data class Pengiriman(
    val idPengiriman: String = "",
    val idPemesanan: String = "",
    val statusPengiriman: String = "Pending",
    val dibuatPada: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "idPengiriman" to idPengiriman,
            "idPemesanan" to idPemesanan,
            "statusPengiriman" to statusPengiriman,
            "dibuatPada" to dibuatPada
        )
    }
}