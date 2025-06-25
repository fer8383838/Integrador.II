




const API_URL = 'http://localhost:8080/usuarios';

document.getElementById('formLogin').addEventListener('submit', async function (e) {
    e.preventDefault();

    const email = document.getElementById('email').value.trim();
    const clave = document.getElementById('clave').value.trim();

    try {
        const res = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, clave })
        });

        if (!res.ok) {
            const errorMsg = await res.text();
            Swal.fire('Error', errorMsg || 'Credenciales incorrectas', 'error');
            return;
        }

        const data = await res.json();

        // Validar que el token exista
        if (!data.token) {
            Swal.fire('Error', 'Token no recibido del servidor', 'error');
            return;
        }

        // Guardar en localStorage

        document.cookie = `token=${data.token}; path=/; SameSite=Lax`;

        localStorage.setItem('token', data.token);



        localStorage.setItem('usuarioID', data.usuarioID);
        localStorage.setItem('rol', data.rol);
        localStorage.setItem('nombre', data.nombre);

        Swal.fire('Bienvenido', `Hola, ${data.nombre}`, 'success').then(() => {

            window.location.href = 'usuarios.html'; // Redirige
        });

        console.log(document.cookie);

    } catch (err) {
        console.error('Error al iniciar sesión:', err);
        Swal.fire('Error', 'No se pudo conectar con el servidor', 'error');
    }
});
