




// URL base de la API para las operaciones de usuarios
const API_URL = 'https://integrador-ii.onrender.com/usuarios';

// Agrega un listener al formulario de login, que se ejecutará cuando el usuario haga submit
document.getElementById('formLogin').addEventListener('submit', async function (e) {
    // Previene que el formulario recargue la página por defecto
    e.preventDefault();

    // Se capturan los valores del input de email y clave, eliminando espacios
    const email = document.getElementById('email').value.trim();
    const clave = document.getElementById('clave').value.trim();

    try {
        // Se hace la petición POST al endpoint /login enviando email y clave
        const res = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, clave }) // cuerpo de la petición en formato JSON
        });

        // Si el servidor responde con un código distinto a 2xx, se muestra un mensaje de error
        if (!res.ok) {
            const errorMsg = await res.text();
            Swal.fire('Error', errorMsg || 'Credenciales incorrectas', 'error');
            return;
        }

        // Se convierte la respuesta en JSON para acceder a los datos (token, usuarioID, etc.)
        const data = await res.json();

        // Verifica si el token está presente en la respuesta
        if (!data.token) {
            Swal.fire('Error', 'Token no recibido del servidor', 'error');
            return;
        }

        // Se guarda el token en localStorage para futuras validaciones del usuario
        localStorage.setItem('token', data.token);
        // También se guarda el ID, el rol y el nombre del usuario para uso en otras páginas
        localStorage.setItem('usuarioID', data.usuarioID);
        localStorage.setItem('rol', data.rol);
        localStorage.setItem('nombre', data.nombre);

        // Se guarda el token también en las cookies como medida redundante (no es obligatoria)
        document.cookie = `token=${data.token}; path=/; SameSite=Lax`;

        // Se muestra mensaje de bienvenida, y luego se redirige a la página principal del sistema
        Swal.fire('Bienvenido', `Hola, ${data.nombre}`, 'success').then(() => {
            // Redirección controlada desde aquí, pero el acceso a index.html será validado desde backend
            window.location.href = 'index.html';
        });

        // Imprime la cookie en consola para verificar (puedes eliminar esta línea en producción)
        console.log(document.cookie);

    } catch (err) {
        // Si ocurre un error (ej: servidor caído), se informa al usuario
        console.error('Error al iniciar sesión:', err);
        Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
    }
});
