





const API_URL = 'http://localhost:8080/usuarios';


// Verificar si el usuario está logueado
window.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    const form = document.getElementById('registro-usuario');

    if (!token) {
        Swal.fire('Acceso denegado', 'Debe iniciar sesión primero', 'warning').then(() => {
            window.location.href = 'login.html';
        });
        return;
    }

    // Ocultar formulario si ya está logueado
    if (form) {
        form.style.display = 'none';
    }
});





// Ocultar formulario si el usuario ya está logueado
window.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    const form = document.getElementById('registro-usuario');
    if (token) {
        form.style.display = 'none';
    }
});

// Registrar usuario con dirección
document.getElementById('formUsuario').addEventListener('submit', async function (e) {
    e.preventDefault();

    const dto = {
        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        dni: document.getElementById('dni').value,
        email: document.getElementById('email').value,
        claveHash: document.getElementById('claveHash').value,
        telefono: document.getElementById('telefono').value,
        rol: document.getElementById('rol').value,
        distrito: document.getElementById('distrito').value,
        direccion: document.getElementById('direccion').value,
        latitud: document.getElementById('latitud').value,
        longitud: document.getElementById('longitud').value,
        principal: true
    };

    try {
        const res = await fetch(`${API_URL}/registrar-completo`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });

        if (res.ok) {
            Swal.fire('Éxito', 'Usuario registrado', 'success');
            document.getElementById('formUsuario').reset();
            cargarUsuarios();
        } else {
            const errorMsg = await res.text();
            console.error('Error del servidor:', errorMsg);
            Swal.fire('Error', errorMsg || 'No se pudo registrar', 'error');
        }
    } catch (err) {
        console.error('Error al registrar:', err);
        Swal.fire('Error', 'Error de conexión o del servidor', 'error');
    }
});

// Listar todos (con token si está logueado)
async function cargarUsuarios() {
    try {
        const token = localStorage.getItem('token');
        const res = await fetch(`${API_URL}/listar`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!res.ok) {
            const errorMsg = await res.text();
            console.error('Error al listar usuarios:', errorMsg);
            Swal.fire('Error', errorMsg || 'No se pudieron cargar los usuarios', 'error');
            return;
        }

        const contentType = res.headers.get("content-type");
        if (!contentType || !contentType.includes("application/json")) {
            console.error("Respuesta no es JSON");
            Swal.fire('Error', 'Respuesta no válida del servidor', 'error');
            return;
        }

        const data = await res.json();
        const tabla = document.getElementById('tablaUsuarios');
        tabla.innerHTML = '';

        data.forEach(u => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${u.usuarioID}</td>
                <td>${u.nombre}</td>
                <td>${u.apellido}</td>
                <td>${u.dni}</td>
                <td>${u.email}</td>
                <td>${u.telefono || '-'}</td>
                <td>${u.rol}</td>
                <td>${u.activo ? 'Sí' : 'No'}</td>
            `;
            tabla.appendChild(fila);
        });
    } catch (err) {
        console.error('Error al cargar usuarios:', err);
        Swal.fire('Error', 'Error al conectar con el servidor', 'error');
    }
}

// Buscar por ID (o por token si está logueado y no hay ID)
async function buscarPorID() {
    const id = document.getElementById('buscarID').value;
    const token = localStorage.getItem('token');
    let res;

    try {
        if (!id && token) {
            res = await fetch(`${API_URL}/mi-perfil`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
        } else {
            res = await fetch(`${API_URL}/obtener-principal/${id}`, {
            headers: {'Authorization': `Bearer ${token}`}
            }
            );
        }

        if (!res.ok) {
            const errorMsg = await res.text();
            console.error('Error al buscar usuario:', errorMsg);
            Swal.fire('Error', errorMsg || 'Usuario no encontrado', 'error');
            return;
        }

        const u = await res.json();

        const tabla = document.getElementById('tablaUsuarios');
        tabla.innerHTML = `
            <tr>
                <td>${u.usuarioID}</td>
                <td>${u.nombre}</td>
                <td>${u.apellido}</td>
                <td>${u.dni}</td>
                <td>${u.email}</td>
                <td>${u.telefono || '-'}</td>
                <td>${u.rol}</td>
                <td>${u.activo ? 'Sí' : 'No'}</td>
            </tr>
        `;

        const tablaDir = document.getElementById('tablaDirecciones');
        tablaDir.innerHTML = `
            <tr>
                <td>${u.distrito}</td>
                <td>${u.direccion}</td>
                <td>${u.latitud}</td>
                <td>${u.longitud}</td>
                <td>${u.principal ? 'Sí' : 'No'}</td>
            </tr>
        `;
        document.getElementById('contenedorDirecciones').style.display = 'block';

    } catch (err) {
        console.error('Error en la búsqueda:', err);
        Swal.fire('Error', err.message || 'No se pudo buscar el usuario', 'error');
    }
}

cargarUsuarios();

