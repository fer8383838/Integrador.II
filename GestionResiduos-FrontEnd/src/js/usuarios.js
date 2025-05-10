




const API_URL = 'http://localhost:8080/usuarios';

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
        direcciones: [{
            distrito: document.getElementById('distrito').value,
            direccion: document.getElementById('direccion').value,
            latitud: document.getElementById('latitud').value,
            longitud: document.getElementById('longitud').value,
            principal: true
        }]
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
            Swal.fire('Error', 'No se pudo registrar', 'error');
        }
    } catch (err) {
        console.error('Error al registrar:', err);
    }
});

// Listar todos
async function cargarUsuarios() {
    try {
        const res = await fetch(`${API_URL}/listar`);
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
    }
}

// Buscar por ID
async function buscarPorID() {
    const id = document.getElementById('buscarID').value;
    if (!id) return cargarUsuarios();

    try {
        const res = await fetch(`${API_URL}/obtener-con-direcciones/${id}`);
        if (!res.ok) throw new Error('Usuario no encontrado');

        const u = await res.json();

        // Mostrar usuario
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
                <td>${u.direcciones && u.direcciones.length > 0 ? u.direcciones[0].direccion : '-'}</td>
            </tr>
        `;

        // Mostrar direcciones
        const tablaDir = document.getElementById('tablaDirecciones');
        tablaDir.innerHTML = '';

        if (u.direcciones && u.direcciones.length > 0) {
            u.direcciones.forEach(d => {
                const fila = document.createElement('tr');
                fila.innerHTML = `
                    <td>${d.distrito}</td>
                    <td>${d.direccion}</td>
                    <td>${d.latitud}</td>
                    <td>${d.longitud}</td>
                    <td>${d.principal ? 'Sí' : 'No'}</td>
                `;
                tablaDir.appendChild(fila);
            });
            document.getElementById('contenedorDirecciones').style.display = 'block';
        } else {
            document.getElementById('contenedorDirecciones').style.display = 'none';
        }

    } catch (err) {
        Swal.fire('Error', err.message, 'error');
    }
}

// Inicial
cargarUsuarios();
