<?php
$users = [
    ['username' => 'admin', 'password' => password_hash('admin123', PASSWORD_DEFAULT)],
    ['username' => 'Azeeru', 'password' => password_hash('Azeeru', PASSWORD_DEFAULT)]
];

function authenticate($username, $password) {
    global $users;

    foreach ($users as $user) {
        if ($user['username'] === $username && password_verify($password, $user['password'])) {
            return true;
        }
    }
    return false;
}

echo "input username: ";
$username = trim(fgets(STDIN));
echo "input password: ";
$password = trim(fgets(STDIN));

if (authenticate($username, $password)) {
    echo "Login successful. Welcome, " . htmlspecialchars($username) . "!";
} else {
    echo "Login failed. Please try again.";
}
?>
