<?php
$produk = [
    ['id' => 1, 'nama' => 'Celana', 'harga' => 100000],
    ['id' => 2, 'nama' => 'Baju', 'harga' => 200000],
    ['id' => 3, 'nama' => 'Jaket', 'harga' => 300000],
];

$Keranjang_Items = [];
$Keranjang_Items[] = $produk[1];
$Keranjang_Items[] = $produk[2];
$Keranjang_Items[] = $produk[0];
$Keranjang_Items[] = $produk[2];
$Keranjang_Items[] = $produk[1];

echo "\nKeranjang Anda:\n";
if (empty($Keranjang_Items)) {
    echo "Keranjang Anda kosong.\n";
} else {
    $totalHarga = 0;
    foreach ($Keranjang_Items as $itemKeranjang) {
        echo $itemKeranjang['nama'] . " - RP." . number_format($itemKeranjang['harga'], 0, ',', '.') . "\n";
        $totalHarga += $itemKeranjang['harga'];
    }
    
    echo "Total harga: RP." . number_format($totalHarga, 0, ',', '.') . "\n";
    echo "Apakah Anda ingin menyelesaikan pembelian? (ya/tidak): ";
    $konfirmasi = trim(fgets(STDIN));
    
    if (strtolower($konfirmasi) === 'ya') {
        echo "Pembelian Anda berhasil diselesaikan.\n";
        echo "Rincian Pembelian:\n";
        foreach ($Keranjang_Items as $itemKeranjang) {
            echo "- " . $itemKeranjang['nama'] . " - RP." . number_format($itemKeranjang['harga'], 0, ',', '.') . "\n";
        }
        echo "Total pembayaran: RP." . number_format($totalHarga, 0, ',', '.') . "\n";
        echo "Terima kasih atas belanja Anda!\n";
    } else {
        echo "Pembelian dibatalkan.\n";
    }
}
?>
