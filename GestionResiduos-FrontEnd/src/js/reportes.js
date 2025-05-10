const API_REPORTE = 'http://localhost:8080/reportes';
const API_USUARIOS = 'http://localhost:8080/usuarios';
const API_TIPOS = 'http://localhost:8080/tipos-residuo';
const API_ZONAS = 'http://localhost:8080/zonas';
const API_DIRECCIONES = 'http://localhost:8080/direcciones';

let mapaUsuarios = {};
let mapaDirecciones = {};
let mapaTipos = {};
let mapaZonas = {};

// Registrar reporte
document.getElementById('formReporte').addEventListener('submit', async (e) => {
    e.preventDefault();
    const reporte = {
        usuarioID: document.getElementById('usuarioID').value,
        direccionID: document.getElementById('direccionID').value,
        tipoID: document.getElementById('tipoID').value,
        zonaID: document.getElementById('zonaID').value,
        descripcion: document.getElementById('descripcion').value,
        fotoURL: document.getElementById('fotoURL').value,
        estado: 'Pendiente'
    };

    try {
        const res = await fetch(`${API_REPORTE}/registrar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(reporte)
        });

        if (res.ok) {
            Swal.fire('Éxito', 'Reporte registrado', 'success');
            document.getElementById('formReporte').reset();
            cargarReportes();
        } else {
            Swal.fire('Error', 'No se pudo registrar', 'error');
        }
    } catch (err) {
        console.error('Error al registrar reporte:', err);
    }
});

// Cargar usuarios en dropdown y mapa
async function cargarUsuarios() {
    const res = await fetch(`${API_USUARIOS}/listar`);
    const data = await res.json();
    const select = document.getElementById('usuarioID');
    select.innerHTML = '<option disabled selected>Seleccione usuario</option>';
    data.forEach(u => {
        mapaUsuarios[u.usuarioID] = `${u.nombre} ${u.apellido}`;
        const option = document.createElement('option');
        option.value = u.usuarioID;
        option.textContent = mapaUsuarios[u.usuarioID];
        select.appendChild(option);
    });
}

// Cargar direcciones
async function cargarDirecciones(id) {
    const res = await fetch(`${API_USUARIOS}/obtener-con-direcciones/${id}`);
    const data = await res.json();
    const select = document.getElementById('direccionID');
    select.innerHTML = '<option disabled selected>Seleccione dirección</option>';
    data.direcciones.forEach(d => {
        mapaDirecciones[d.direccionID] = `${d.direccion}, ${d.distrito}`;
        const option = document.createElement('option');
        option.value = d.direccionID;
        option.textContent = mapaDirecciones[d.direccionID];
        select.appendChild(option);
    });
}
document.getElementById('usuarioID').addEventListener('change', () => {
    const id = document.getElementById('usuarioID').value;
    cargarDirecciones(id);
});

// Cargar tipos
async function cargarTipos() {
    const res = await fetch(`${API_TIPOS}/listar`);
    const data = await res.json();
    const select = document.getElementById('tipoID');
    select.innerHTML = '<option disabled selected>Seleccione tipo</option>';
    data.forEach(t => {
        mapaTipos[t.tipoID] = t.nombre;
        const option = document.createElement('option');
        option.value = t.tipoID;
        option.textContent = t.nombre;
        select.appendChild(option);
    });
}

// Cargar zonas
async function cargarZonas() {
    const res = await fetch(`${API_ZONAS}/listar`);
    const data = await res.json();
    const select = document.getElementById('zonaID');
    select.innerHTML = '<option disabled selected>Seleccione zona</option>';
    data.forEach(z => {
        mapaZonas[z.zonaID] = z.nombre;
        const option = document.createElement('option');
        option.value = z.zonaID;
        option.textContent = z.nombre;
        select.appendChild(option);
    });
}

// Mostrar reportes
async function cargarReportes() {
    const res = await fetch(`${API_REPORTE}/listar`);
    const data = await res.json();
    const tabla = document.getElementById('tablaReportes');
    tabla.innerHTML = '';

    data.forEach(r => {
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td>${r.reporteID}</td>
            <td>${mapaUsuarios[r.usuarioID] || r.usuarioID}</td>
            <td>${mapaDirecciones[r.direccionID] || r.direccionID}</td>
            <td>${mapaTipos[r.tipoID] || r.tipoID}</td>
            <td>${mapaZonas[r.zonaID] || r.zonaID}</td>
            <td>${r.descripcion}</td>
            <td>${r.fotoURL || '-'}</td>
            <td>${r.fechaReporte ? new Date(r.fechaReporte).toLocaleString() : '-'}</td>
            <td>${r.estado}</td>
        `;
        tabla.appendChild(fila);
    });
}

// Filtrar
async function filtrarReportes() {
    const id = document.getElementById('filtroUsuarioID').value;
    if (!id) return cargarReportes();

    const res = await fetch(`${API_REPORTE}/listar`);
    const data = await res.json();
    const filtrados = data.filter(r => r.usuarioID == id);
    const tabla = document.getElementById('tablaReportes');
    tabla.innerHTML = '';

    filtrados.forEach(r => {
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td>${r.reporteID}</td>
            <td>${mapaUsuarios[r.usuarioID] || r.usuarioID}</td>
            <td>${mapaDirecciones[r.direccionID] || r.direccionID}</td>
            <td>${mapaTipos[r.tipoID] || r.tipoID}</td>
            <td>${mapaZonas[r.zonaID] || r.zonaID}</td>
            <td>${r.descripcion}</td>
            <td>${r.fotoURL || '-'}</td>
            <td>${r.fechaReporte ? new Date(r.fechaReporte).toLocaleString() : '-'}</td>
            <td>${r.estado}</td>
        `;
        tabla.appendChild(fila);
    });
}

// Inicial
(async function init() {
    await cargarUsuarios();
    await cargarTipos();
    await cargarZonas();
    await cargarReportes();
})();