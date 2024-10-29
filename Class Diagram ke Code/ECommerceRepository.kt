package com.example.el_cloth

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference

class ECommerceRepository {
    private val database: DatabaseReference = Firebase.database.reference

    data class Pengguna(
        val idPengguna: String,
        val email: String,
        val nama: String,
        val alamat: String,
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
        val idProduk: String,
        val nama: String,
        val jenis: String,
        val deskripsi: String,
        val harga: Double,
        val kuantitasStok: Int,
        val ukuran: String,
        val warna: String,
        val bahan: String
    ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "idProduk" to idProduk,
                "nama" to nama,
                "jenis" to jenis,
                "deskripsi" to deskripsi,
                "harga" to harga,
                "kuantitasStok" to kuantitasStok,
                "ukuran" to ukuran,
                "warna" to warna,
                "bahan" to bahan
            )
        }
    }

    data class Keranjang(
        val idKeranjang: String,
        val penggunaId: String,
        val items: List<KeranjangBarang> = listOf()
    ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "idKeranjang" to idKeranjang,
                "penggunaId" to penggunaId,
                "items" to items.map { it.toMap() }
            )
        }
    }

    data class KeranjangBarang(
        val idKeranjangItems: String,
        val produkId: String,
        val kuantitas: Int
    ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "idKeranjangItems" to idKeranjangItems,
                "produkId" to produkId,
                "kuantitas" to kuantitas
            )
        }
    }

    data class Pesanan(
        val idPemesanan: String,
        val penggunaId: String,
        val items: List<KeranjangBarang>,
        val totalHarga: Double,
        val status: String
    ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "idPemesanan" to idPemesanan,
                "penggunaId" to penggunaId,
                "items" to items.map { it.toMap() },
                "totalHarga" to totalHarga,
                "status" to status
            )
        }
    }

    data class Pembayaran(
        val idPembayaran: String,
        val penggunaId: String,
        val pesananId: String,
        val jumlah: Double,
        val status: String
    ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "idPembayaran" to idPembayaran,
                "penggunaId" to penggunaId,
                "pesananId" to pesananId,
                "jumlah" to jumlah,
                "status" to status
            )
        }
    }

    data class Pengiriman(
        val idPengiriman: String,
        val pesananId: String,
        val alamatPengiriman: String,
        val statusPengiriman: String
    ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "idPengiriman" to idPengiriman,
                "pesananId" to pesananId,
                "alamatPengiriman" to alamatPengiriman,
                "statusPengiriman" to statusPengiriman
            )
        }
    }

    fun tambahPengguna(pengguna: com.example.el_cloth.Pengguna, callback: (Boolean) -> Unit) {
        database.child("pengguna").child(pengguna.idPengguna).setValue(pengguna.toMap())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener {
                Log.e("FirebaseError", "Failed to add pengguna", it)
                callback(false)
            }
    }

    fun getPengguna(idPengguna: String, callback: (Pengguna?) -> Unit) {
        database.child("pengguna").child(idPengguna).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(snapshot.getValue(Pengguna::class.java))
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseError", "Error retrieving pengguna", it)
                callback(null)
            }
    }

    fun tambahProduk(produk: Produk, callback: (Boolean) -> Unit) {
        database.child("produk").child(produk.idProduk).setValue(produk.toMap())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener {
                Log.e("FirebaseError", "Failed to add produk", it)
                callback(false)
            }
    }

    fun getProduk(idProduk: String, callback: (Produk?) -> Unit) {
        database.child("produk").child(idProduk).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(snapshot.getValue(Produk::class.java))
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseError", "Error retrieving produk", it)
                callback(null)
            }
    }

    fun createKeranjang(keranjang: Keranjang) {
        database.child("keranjang").child(keranjang.idKeranjang).setValue(keranjang.toMap())
            .addOnFailureListener {
                Log.e("FirebaseError", "Error creating keranjang", it)
            }
    }

    fun addItemToKeranjang(idKeranjang: String, item: KeranjangBarang) {
        database.child("keranjang").child(idKeranjang).child("items")
            .child(item.idKeranjangItems).setValue(item.toMap())
            .addOnFailureListener {
                Log.e("FirebaseError", "Error adding item to keranjang", it)
            }
    }

    fun createPesanan(pesanan: Pesanan) {
        database.child("pesanan").child(pesanan.idPemesanan).setValue(pesanan.toMap())
            .addOnFailureListener {
                Log.e("FirebaseError", "Error creating pesanan", it)
            }
    }

    fun getPesanan(idPemesanan: String, callback: (Pesanan?) -> Unit) {
        database.child("pesanan").child(idPemesanan).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(snapshot.getValue(Pesanan::class.java))
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseError", "Error retrieving pesanan", it)
                callback(null)
            }
    }

    fun createPembayaran(pembayaran: Pembayaran) {
        database.child("pembayaran").child(pembayaran.idPembayaran).setValue(pembayaran.toMap())
            .addOnFailureListener {
                Log.e("FirebaseError", "Error creating pembayaran", it)
            }
    }

    fun updatePembayaranStatus(idPembayaran: String, status: String) {
        database.child("pembayaran").child(idPembayaran).child("status").setValue(status)
            .addOnFailureListener {
                Log.e("FirebaseError", "Error updating pembayaran status", it)
            }
    }

    fun createPengiriman(pengiriman: Pengiriman) {
        database.child("pengiriman").child(pengiriman.idPengiriman).setValue(pengiriman.toMap())
            .addOnFailureListener {
                Log.e("FirebaseError", "Error creating pengiriman", it)
            }
    }

    fun updatePengirimanStatus(idPengiriman: String, status: String) {
        database.child("pengiriman").child(idPengiriman).child("statusPengiriman").setValue(status)
            .addOnFailureListener {
                Log.e("FirebaseError", "Error updating pengiriman status", it)
            }
    }
}
