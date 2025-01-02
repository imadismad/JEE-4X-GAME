window.onload = function() {
    const username = new URLSearchParams(window.location.search).get('username');
    if (username) {
        document.getElementById('nomUtilisateur').value = username;
    }
};

function togglePassword() {
    const passwordField = document.getElementById('motDePasse');
    passwordField.type = passwordField.type === 'password' ? 'text' : 'password';
}
