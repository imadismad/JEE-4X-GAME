// Function to validate that passwords match
function validatePasswords() {
    const password = document.getElementById("motDePasse");
    const confirmPassword = document.getElementById("confirmMotDePasse");

    if (password.value !== confirmPassword.value) {
        alert("Les mots de passe ne correspondent pas. Veuillez réessayer.");
        confirmPassword.classList.add("error");
        return false;
    }
    confirmPassword.classList.remove("error"); // Remove error styling if fixed
    return true;
}
