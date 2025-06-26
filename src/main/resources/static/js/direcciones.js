





const API_DIRECCION = 'https://integrador-ii.onrender.com/direcciones';

// Paso 1: Obtener el token almacenado tras el login
const token = localStorage.getItem("token");

// Paso 2: Registrar direccion
document.getElementById('formDireccion').addEventListener('submit', async function (e) {
    e.preventDefault();

    const nuevaDireccion = {
        usuarioID: parseInt(document.getElementById('usuarioID').value),
        distrito: document.getElementById('distrito').value,
        direccion: document.getElementById('direccion').value,
        latitud: document.getElementById('latitud').value || null,
        longitud: document.getElementById('longitud').value || null,
        principal: false
    };

    try {
        const respuesta = await fetch(`${API_DIRECCION}/registrar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(nuevaDireccion)
        });

        if (respuesta.ok) {
            Swal.fire('Éxito', 'Dirección registrada correctamente', 'success');
            document.getElementById('formDireccion').reset();
            await cargarDirecciones();
        } else {
            const errorMsg = await respuesta.text();
            console.error('Error del servidor:', errorMsg);
            Swal.fire('Error', errorMsg || 'No se pudo registrar la dirección', 'error');
        }
    } catch (error) {
        console.error('Error al registrar dirección:', error);
        Swal.fire('Error', 'Error de conexión o del servidor', 'error');
    }
});

// Paso 3: Cargar direcciones con token
async function cargarDirecciones() {
    try {
        const respuesta = await fetch(`${API_DIRECCION}/listar`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (!respuesta.ok) {
            const errorMsg = await respuesta.text();
            console.error('Error al listar direcciones:', errorMsg);
            Swal.fire('Error', errorMsg || 'No se pudieron cargar las direcciones', 'error');
            return;
        }

        const direcciones = await respuesta.json();

        const tabla = document.getElementById('tablaDirecciones');
        tabla.innerHTML = '';

        direcciones.forEach(d => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${d.direccionID}</td>
                <td>${d.usuarioID}</td>
                <td>${d.distrito}</td>
                <td>${d.direccion}</td>
                <td>${d.latitud || '-'}</td>
                <td>${d.longitud || '-'}</td>
                <td>${d.principal ? 'Sí' : 'No'}</td>
            `;
            tabla.appendChild(fila);
        });
    } catch (error) {
        console.error('Error al cargar direcciones:', error);
        Swal.fire('Error', 'Error al conectar con el servidor', 'error');
    }
}

// Paso 4: Buscar direcciones por usuarioID
async function buscarDireccionesPorUsuarioID() {
    const usuarioID = document.getElementById('buscarUsuarioID').value;
    if (!usuarioID) return cargarDirecciones();

    try {
        const res = await fetch(`${API_DIRECCION}/listar/${usuarioID}`, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });
        const direcciones = await res.json();

        const tabla = document.getElementById('tablaDirecciones');
        tabla.innerHTML = '';

        direcciones.forEach(d => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${d.direccionID}</td>
                <td>${d.usuarioID}</td>
                <td>${d.distrito}</td>
                <td>${d.direccion}</td>
                <td>${d.latitud || '-'}</td>
                <td>${d.longitud || '-'}</td>
                <td>${d.principal ? 'Sí' : 'No'}</td>
            `;
            tabla.appendChild(fila);
        });
    } catch (error) {
        console.error('Error al buscar direcciones:', error);
        Swal.fire('Error', 'No se pudo obtener las direcciones', 'error');
    }
}

// Paso 5: Cargar usuarios para el formulario (requiere token también)
async function cargarUsuariosParaFormulario() {
    try {
        const res = await fetch('https://integrador-ii.onrender.com/usuarios/listar', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        const data = await res.json();

        const select = document.getElementById('usuarioID');
        select.innerHTML = '<option value="">Seleccione un usuario</option>';

        data.forEach(u => {
            const option = document.createElement('option');
            option.value = u.usuarioID;
            option.text = `${u.usuarioID} - ${u.nombre} ${u.apellido}`;
            select.appendChild(option);
        });

    } catch (error) {
        console.error('Error al cargar usuarios:', error);
    }
}

// Paso 6: Mostrar direcciones al cambiar usuario
document.getElementById('usuarioID').addEventListener('change', async function () {
    const usuarioID = this.value;
    const selectDirecciones = document.getElementById('direccionesRegistradas');
    selectDirecciones.innerHTML = '';

    if (!usuarioID) {
        selectDirecciones.disabled = true;
        selectDirecciones.innerHTML = '<option>Seleccione un usuario para ver direcciones</option>';
        return;
    }

    try {
        const res = await fetch(`${API_DIRECCION}/listar/${usuarioID}`, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });
        const direcciones = await res.json();

        if (direcciones.length === 0) {
            selectDirecciones.disabled = true;
            selectDirecciones.innerHTML = '<option>Este usuario no tiene direcciones registradas</option>';
            return;
        }

        selectDirecciones.disabled = false;
        direcciones.forEach(d => {
            const option = document.createElement('option');
            option.text = `${d.direccion} - ${d.distrito}`;
            selectDirecciones.appendChild(option);
        });
    } catch (error) {
        console.error('Error al cargar direcciones del usuario:', error);
        selectDirecciones.disabled = true;
        selectDirecciones.innerHTML = '<option>Error al cargar direcciones</option>';
    }
});

// Inicializar todo
cargarDirecciones();
cargarUsuariosParaFormulario();

