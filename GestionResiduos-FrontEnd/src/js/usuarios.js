const API_URL = 'http://localhost:8080/usuarios';

// Registrar usuario
document.getElementById('formUsuario').addEventListener('submit', async function (event) {
    event.preventDefault();

    const usuario = {
        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        dni: document.getElementById('dni').value,
        email: document.getElementById('email').value,
        claveHash: document.getElementById('claveHash').value,
        telefono: document.getElementById('telefono').value,
        rol: document.getElementById('rol').value,
        activo: true // se mantiene por defecto
    };

    try {
        const respuesta = await fetch(`${API_URL}/registrar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });

        if (respuesta.ok) {
            Swal.fire('Éxito', 'Usuario registrado correctamente', 'success');
            document.getElementById('formUsuario').reset();
            await cargarUsuarios();
        } else {
            Swal.fire('Error', 'No se pudo registrar el usuario', 'error');
        }
    } catch (error) {
        console.error('Error al registrar:', error);
    }
});

// Cargar usuarios
async function cargarUsuarios() {
    try {
        const respuesta = await fetch(`${API_URL}/listar`);
        const usuarios = await respuesta.json();

        const tabla = document.getElementById('tablaUsuarios');
        tabla.innerHTML = '';

        usuarios.forEach(u => {
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
                <td>${u.fechaRegistro ? new Date(u.fechaRegistro).toLocaleString() : '-'}</td>
            `;
            tabla.appendChild(fila);
            console.log("Usuario cargado", usuarios);
        });
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
    }
}

// Inicializar
cargarUsuarios();