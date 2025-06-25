// -------------------------------------------------------------------------
// ARCHIVO: index.js
// PROPÓSITO: Validar el token antes de permitir el acceso a index.html
// RELACIÓN: Se carga desde index.html como script externo
// -------------------------------------------------------------------------

document.addEventListener('DOMContentLoaded', () => {

    // Paso 1: Obtener token del localStorage
    const token = localStorage.getItem('token');

    // Paso 2: Si no hay token, redirige a login
    if (!token) {
        window.location.href = 'login.html';
        return; // Detiene el resto del código
    }

    // Paso 3: Validar el token haciendo un fetch al backend
    fetch('http://localhost:8080/usuarios/info-rol-actual', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(response => {
        if (!response.ok) {
            // Si el token es inválido o expiró
            localStorage.removeItem('token');
            window.location.href = 'login.html';
        } else {
            return response.json();
        }
    })
    .then(data => {
        // Si quieres, puedes hacer algo según el rol aquí
        console.log('Acceso concedido al usuario:', data.email);
    })
    .catch(error => {
        console.error('Error de validación del token:', error);
        localStorage.removeItem('token');
        window.location.href = 'login.html';
    });

});