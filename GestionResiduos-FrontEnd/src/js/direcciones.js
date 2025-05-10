const API_DIRECCION = 'http://localhost:8080/direcciones';

// Registrar dirección
document.getElementById('formDireccion').addEventListener('submit', async function (e) {
    e.preventDefault();

    const nuevaDireccion = {
        usuarioID: parseInt(document.getElementById('usuarioID').value),
        distrito: document.getElementById('distrito').value,
        direccion: document.getElementById('direccion').value,
        latitud: document.getElementById('latitud').value || null,
        longitud: document.getElementById('longitud').value || null,
        principal: document.getElementById('principal').value === 'true'
    };

    try {
        const respuesta = await fetch(${API_DIRECCION}/registrar, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nuevaDireccion)
        });

        if (respuesta.ok) {
            Swal.fire('Éxito', 'Dirección registrada correctamente', 'success');
            document.getElementById('formDireccion').reset();
            await cargarDirecciones();
        } else {
            Swal.fire('Error', 'No se pudo registrar la dirección', 'error');
        }
    } catch (error) {
        console.error('Error al registrar dirección:', error);
    }
});

// Cargar direcciones
async function cargarDirecciones() {
    try {
        const respuesta = await fetch(${API_DIRECCION}/listar);
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
    }
}

// Inicializar
cargarDirecciones();