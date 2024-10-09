<?php

define('MAKS_Item_', 100);
define('MAKS_NAMA_LENGTH', 50);

$List_Barang = [
    ['id' => 1, 'nama' => 'Kaos', 'harga' => 15999],
    ['id' => 2, 'nama' => 'Celana Jeans', 'harga' => 29999],
    ['id' => 3, 'nama' => 'Sweater', 'harga' => 24999],
];

$Jumlah = count($List_Barang);

function cariItem_($kataKunci, $List_Barang) {
    echo "Hasil pencarian untuk '$kataKunci':\n";
    $Ketemu = false;

    foreach ($List_Barang as $Item_) {
        if (stripos($Item_['nama'], $kataKunci) !== false) {
            printf("ID: %d, Nama: %s, Harga: Rp.%s\n", 
                   $Item_['id'], 
                   $Item_['nama'], 
                   number_format($Item_['harga'], 0, ',', '.'));
            $Ketemu = true;
        }
    }

    if (!$Ketemu) {
        echo "Tidak ada Item_ yang cocok dengan kata kunci.\n";
    }
}

$kataKunci = readline("Masukkan kata kunci pencarian: ");
cariItem_($kataKunci, $List_Barang);

?>
