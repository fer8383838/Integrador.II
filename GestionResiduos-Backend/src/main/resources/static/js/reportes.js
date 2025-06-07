




// ========================================================================================
// ARCHIVO: reportes.js
// DESCRIPCIÓN: Maneja el registro y visualización de reportes de residuos.
// Estructura basada en el modelo funcional de usuarios.js, con validación desde backend.
// ========================================================================================

// Paso 1: Definición de constantes para los endpoints y Cloudinary
const API_REPORTE = 'http://localhost:8080/reportes';
const API_USUARIOS = 'http://localhost:8080/usuarios';
const API_TIPOS = 'http://localhost:8080/tipos-residuo';
const API_ZONAS = 'http://localhost:8080/zonas';

const CLOUD_NAME = 'dqeip233e';
const UPLOAD_PRESET = 'residuos_upload';

const token = localStorage.getItem("token");

// Paso 2: Inicializamos mapas para relacionar IDs con nombres
let mapaUsuarios = {};
let mapaTipos = {};
let mapaZonas = {};

// Paso 3: Al cargar la página validamos el token y rol desde el backend
document.addEventListener("DOMContentLoaded", async () => {
    try {
        const respuesta = await fetch(`${API_USUARIOS}/info-rol-actual`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!respuesta.ok) throw new Error('Token inválido o sesión expirada');

        const datosUsuario = await respuesta.json();
        const usuarioID = datosUsuario.usuarioID;

        const nombreCompleto = `${datosUsuario.nombre}`;
        const usuarioSelect = document.getElementById('usuarioID');
        if (usuarioSelect) {
            const opcion = new Option(nombreCompleto, usuarioID, true, true);
            usuarioSelect.appendChild(opcion);
            usuarioSelect.disabled = true;
        }

        await cargarTipos();
        await cargarZonas();
        await cargarReportes();

    } catch (error) {
        console.error('Error al cargar datos iniciales:', error);
        Swal.fire('Error', 'No se pudo cargar el formulario. Verifica tu sesión.', 'error');
    }
});

// Paso 5: Subir imagen a Cloudinary cuando se selecciona un archivo
document.getElementById("archivoFoto").addEventListener("change", async function () {
    const file = this.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("upload_preset", UPLOAD_PRESET);

    document.getElementById("mensajeFoto").textContent = "Subiendo imagen...";

    try {
        const res = await fetch('https://api.cloudinary.com/v1_1/dqeip233e/image/upload', {
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

// Paso 6: Registrar nuevo reporte al enviar el formulario
document.addEventListener("DOMContentLoaded", function () {
    const formReporte = document.getElementById("formReporte");

    formReporte.addEventListener("submit", async function (e) {
        e.preventDefault();

        const usuarioID = document.getElementById("usuarioID").value;
        const tipoID = document.getElementById("tipoID").value;
        const zonaID = document.getElementById("zonaID").value;
        const descripcion = document.getElementById("descripcion").value.trim();
        const fotoURL = document.getElementById("fotoURL").value;
        const latitud = document.getElementById("latitud").value;
        const longitud = document.getElementById("longitud").value;

        if (!latitud || !longitud) {
            return Swal.fire("Ubicación requerida", "Debes seleccionar un punto en el mapa.", "warning");
        }

        const nuevoReporte = {
            usuarioID: parseInt(usuarioID),
            tipoID: parseInt(tipoID),
            zonaID: parseInt(zonaID),
            descripcion: descripcion,
            fotoURL: fotoURL,
            latitud: parseFloat(latitud),
            longitud: parseFloat(longitud)
        };

        try {
            const respuesta = await fetch("/reportes/registrar", {
                method: "POST",
                headers: { "Content-Type": "application/json",
                 'Authorization': `Bearer ${token}`},
                body: JSON.stringify(nuevoReporte)
            });

            const resultado = await respuesta.text();

            if (respuesta.ok) {
                Swal.fire("Éxito", resultado, "success");
                formReporte.reset();
                document.getElementById("fotoURL").value = "";
                document.getElementById("latitud").value = "";
                document.getElementById("longitud").value = "";
            } else {
                Swal.fire("Error", resultado, "error");
            }

        } catch (error) {
            console.error("Error al registrar reporte:", error);
            Swal.fire("Error inesperado", "Ocurrió un problema al registrar el reporte.", "error");
        }
    });
});

// === BLOQUE FINAL: Leaflet para capturar coordenadas GPS ===
let mapa;
let marcador;

document.addEventListener("DOMContentLoaded", function () {
    const modalMapa = document.getElementById("modalMapa");

    modalMapa.addEventListener("shown.bs.modal", function () {
        if (!mapa) {
            mapa = L.map("mapaLeaflet").setView([-12.05, -77.04], 13);

            L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                attribution: '© OpenStreetMap contributors'
            }).addTo(mapa);

            L.Control.geocoder().addTo(mapa);

            mapa.on("click", function (e) {
                const lat = e.latlng.lat.toFixed(6);
                const lng = e.latlng.lng.toFixed(6);

                if (marcador) mapa.removeLayer(marcador);

                marcador = L.marker([lat, lng]).addTo(mapa);
                marcador.bindPopup("Ubicación seleccionada").openPopup();

                document.getElementById("latitud").value = lat;
                document.getElementById("longitud").value = lng;
            });
        }

        setTimeout(() => {
            mapa.invalidateSize();
        }, 200);
    });
});

// Paso 7: Cargar los tipos de residuo
async function cargarTipos() {
    const res = await fetch(`${API_TIPOS}/listar`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
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

// Paso 8: Cargar las zonas
async function cargarZonas() {
    const res = await fetch(`${API_ZONAS}/listar`, {
        headers: { 'Authorization': `Bearer ${token}` }
    });
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

// Paso 10: Cargar todos los reportes
async function cargarReportes() {
    try {
        const res = await fetch(`${API_REPORTE}/listar`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!res.ok) {
            const errorTexto = await res.text();
            throw new Error(errorTexto);
        }

        const data = await res.json();
        const tabla = document.getElementById('tablaReportes');
        tabla.innerHTML = '';

        data.forEach(r => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${r.reporteID}</td>
                <td>${r.nombreUsuario}</td>
                <td>${r.tipoResiduo}</td>
                <td>${r.zona}</td>
                <td>${r.descripcion}</td>
                <td><a href="${r.fotoURL}" target="_blank">Ver foto</a></td>
                <td>${r.fechaReporte ? new Date(r.fechaReporte).toLocaleString() : '-'}</td>
                <td>${r.estado}</td>
                <td> ` +
                (r.latitud && r.longitud ? `
                    <a href="https://www.google.com/maps?q=${r.latitud},${r.longitud}" target="_blank">
                    Ver ubicación
                    </a>`
                        : 'No registrada')+
                `</td>
            `;
            document.getElementById('tablaReportes').appendChild(fila);
        });

    } catch (error) {
        console.error("Error al cargar reportes:", error);
        Swal.fire('Error', error.message || 'No se pudieron cargar los reportes.', 'error');
    }
}





