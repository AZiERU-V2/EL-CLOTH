<?php
$Item_Barang = [
    ['id' => 1, 'nama' => 'Celana', 'harga' => 100000],
    ['id' => 2, 'nama' => 'Baju', 'harga' => 200000],
    ['id' => 3, 'nama' => 'Jaket', 'harga' => 300000],
];

$keranjang_Item = [];

while (true) {
    echo "\nDaftar barang yang tersedia:\n";
    foreach ($Item_Barang as $item) {
        echo $item['id'] . ". " . $item['nama'] . " - RP." . number_format($item['harga'], 0, ',', '.') . "\n";
    }
    echo "Masukkan ID barang untuk ditambahkan ke keranjang (atau ketik 'keluar' untuk selesai): ";
    $input = trim(fgets(STDIN));

    if (strtolower($input) === 'keluar') {
        break;
    }

    $idBarang = intval($input);
    $Ketemu = false;

    foreach ($Item_Barang as $item) {
        if ($item['id'] === $idBarang) {
            $keranjang_Item[] = $item;
            echo $item['nama'] . " telah ditambahkan ke keranjang Anda.\n";
            $Ketemu = true;
            break;
        }
    }

    if (!$Ketemu) {
        echo "Barang dengan ID $idBarang tidak Ketemu.\n";
    }
}

echo "\nKeranjang Anda:\n";
if (empty($keranjang_Item)) {
    echo "Keranjang Anda kosong.\n";
} else {
    foreach ($keranjang_Item as $itemKeranjang) {
        echo $itemKeranjang['nama'] . " - RP." . number_format($itemKeranjang['harga'], 0, ',', '.') . "\n";
    }
}
?>
