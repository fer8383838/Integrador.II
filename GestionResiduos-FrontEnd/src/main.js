const API_URL = 'http://localhost:8080/usuarios';

// Registrar usuario
document.getElementById('formUsuario').addEventListener('submit', async function(event) {
    event.preventDefault();

    const usuario = {
        nombre: document.getElementById('nombre').value,
        correo: document.getElementById('correo').value,
        contrasena: document.getElementById('contrasena').value,
        rol: document.getElementById('rol').value
    };

    try {
        const respuesta = await fetch(`${API_URL}/registrar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(usuario)
        });

        if (respuesta.ok) {
            Swal.fire('¡Éxito!', 'Usuario registrado correctamente', 'success');
            document.getElementById('formUsuario').reset();
            await cargarUsuarios();
        } else {
            Swal.fire('Error', 'No se pudo registrar el usuario', 'error');
        }
    } catch (error) {
        Swal.fire('Error', 'Error de conexión', 'error');
        console.error(error);
    }
});

// Cargar usuarios
async function cargarUsuarios() {
    try {
        const respuesta = await fetch(`${API_URL}/listar`);
        const usuarios = await respuesta.json();

        const tabla = document.getElementById('tablaUsuarios');
        tabla.innerHTML = '';

        usuarios.forEach(usuario => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${usuario.idUsuario}</td>
                <td>${usuario.nombre}</td>
                <td>${usuario.correo}</td>
                <td>${usuario.rol}</td>
            `;
            tabla.appendChild(fila);
            console.log('Usuarios recibidos:', usuarios);

        });
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
    }
}

// Cargar usuarios al iniciar
cargarUsuarios();
