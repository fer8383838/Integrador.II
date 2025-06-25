




const API_REPORTE = 'http://localhost:8080/reportes';
const API_USUARIOS = 'http://localhost:8080/usuarios';
const API_TIPOS = 'http://localhost:8080/tipos-residuo';
const API_ZONAS = 'http://localhost:8080/zonas';
const API_DIRECCIONES = 'http://localhost:8080/direcciones';

const CLOUD_NAME = 'dqeip233e';
const UPLOAD_PRESET = 'residuos_upload';

let mapaUsuarios = {};
let mapaDirecciones = {};
let mapaTipos = {};
let mapaZonas = {};

document.addEventListener("DOMContentLoaded", () => {

    // Subida de imagen
    document.getElementById("archivoFoto").addEventListener("change", async function () {
        const file = this.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("file", file);
        formData.append("upload_preset", UPLOAD_PRESET);

        document.getElementById("mensajeFoto").textContent = "Subiendo imagen...";

        try {
            const res = await fetch(`https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`, {
                method: "POST",
                body: formData
            });

            const data = await res.json();
            document.getElementById("fotoURL").value = data.secure_url;
            document.getElementById("mensajeFoto").textContent = "Imagen cargada correctamente.";
        } catch (err) {
            console.error("Error al subir imagen:", err);
            document.getElementById("mensajeFoto").textContent = "Error al subir imagen.";
        }
    });

    // Validaciones de formulario
    document.getElementById("formReporte").addEventListener("submit", function (e) {
        const descripcion = document.getElementById("descripcion").value.trim();
        const palabras = descripcion.split(/\s+/);
        const foto = document.getElementById("fotoURL").value;

        if (palabras.length < 10) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: 'Error',
                text: 'La descripción debe tener al menos 10 palabras.'
            });
            return;
        }

        if (!foto) {
            e.preventDefault();
            Swal.fire({
                icon: 'warning',
                title: 'Error',
                text: 'Primero debes subir una imagen.'
            });
        }
    });

    // Cargar combos y reportes
    cargarUsuarios().then(() => cargarReportes());
    cargarTipos();
    cargarZonas();

    document.getElementById('usuarioID').addEventListener('change', () => {
        const id = document.getElementById('usuarioID').value;
        cargarDirecciones(id);
    });
});

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
            document.getElementById('fotoURL').value = '';
            document.getElementById('mensajeFoto').textContent = '';
            cargarReportes();
        } else {
            const errorMsg = await res.text();
            Swal.fire('Error', errorMsg || 'No se pudo registrar', 'error');
        }
    } catch (err) {
        console.error('Error al registrar reporte:', err);
        Swal.fire('Error', 'Error de conexión o del servidor', 'error');
    }
});

// Cargar usuarios y mapa
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

// Cargar direcciones según usuario
async function cargarDirecciones(usuarioID) {
    const res = await fetch(`${API_DIRECCIONES}/listar/${usuarioID}`);
    const data = await res.json();
    const select = document.getElementById('direccionID');
    select.innerHTML = '<option disabled selected>Seleccione dirección</option>';
    data.forEach(d => {
        mapaDirecciones[d.direccionID] = `${d.direccion}, ${d.distrito}`;
        const option = document.createElement('option');
        option.value = d.direccionID;
        option.textContent = mapaDirecciones[d.direccionID];
        select.appendChild(option);
    });
}

// Cargar tipos de residuo
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

// Cargar todos los reportes
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
            <td><a href="${r.fotoURL}" target="_blank">Ver foto</a></td>
            <td>${r.fechaReporte ? new Date(r.fechaReporte).toLocaleString() : '-'}</td>
            <td>${r.estado}</td>
        `;
        tabla.appendChild(fila);
    });
}

// Filtro por usuarioID con Swal
async function filtrarReportes() {
    const id = document.getElementById('filtroUsuarioID').value;
    if (!id) return cargarReportes();

    try {
        const res = await fetch(`${API_REPORTE}/listar`);
        const data = await res.json();

        const filtrados = data.filter(r => r.usuarioID == id);

        if (filtrados.length === 0) {
            Swal.fire('Sin resultados', 'Este usuario no tiene reportes.', 'info');
            console.warn('No se encontraron reportes para el usuario ID:', id);
            return;
        }

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
                <td><a href="${r.fotoURL}" target="_blank">Ver foto</a></td>
                <td>${r.fechaReporte ? new Date(r.fechaReporte).toLocaleString() : '-'}</td>
                <td>${r.estado}</td>
            `;
            tabla.appendChild(fila);
        });

    } catch (error) {
        console.error('Error al filtrar reportes por usuario:', error);
        Swal.fire('Error', 'No se pudieron cargar los reportes.', 'error');
    }
}
