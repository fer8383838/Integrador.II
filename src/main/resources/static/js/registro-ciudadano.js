

// -------------------------------------------------------------------------
// ARCHIVO: registro-ciudadano.js
// PROPÓSITO: Controla el registro de ciudadanos desde el formulario público
// IMPORTANCIA: Se encarga de capturar los datos, validarlos y enviarlos al backend
// mediante el endpoint /usuarios/registrar-ciudadano-publico
// -------------------------------------------------------------------------

document.addEventListener("DOMContentLoaded", () => {
    // Paso 1: Cargar las zonas desde el backend al cargar la página
    fetch('http://localhost:8080/zonas/listar')
        .then(response => response.json())
        .then(data => {
            const zonaSelect = document.getElementById("zonaID");
            data.forEach(zona => {
                const option = document.createElement("option");
                option.value = zona.zonaID;
                option.textContent = zona.nombre;
                zonaSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Error al cargar zonas:", error);
            Swal.fire("Error", "No se pudieron cargar las zonas", "error");
        });

    // Paso 2: Manejar envío del formulario
    document.getElementById("formRegistroCiudadano").addEventListener("submit", (event) => {
        event.preventDefault();

        const datos = {
            nombre: document.getElementById("nombre").value.trim(),
            apellido: document.getElementById("apellido").value.trim(),
            dni: document.getElementById("dni").value.trim(),
            email: document.getElementById("email").value.trim(),
            claveHash: document.getElementById("claveHash").value,
            telefono: document.getElementById("telefono").value.trim(),
            rol: "Ciudadano",
            distrito: document.getElementById("distrito").value,
            direccion: document.getElementById("direccion").value.trim(),
            latitud: parseFloat(document.getElementById("latitud").value),
            longitud: parseFloat(document.getElementById("longitud").value),
            zonaID: parseInt(document.getElementById("zonaID").value)  // 👈 NUEVO: Captura el valor seleccionado
        };

        // Validación rápida antes de enviar
        if (!datos.zonaID) {
            Swal.fire("Error", "Selecciona una zona válida", "error");
            return;
        }

        fetch("http://localhost:8080/usuarios/registrar-ciudadano-publico", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datos)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(msg => { throw new Error(msg); });
            }
            return response.text();
        })
        .then(msg => {
            Swal.fire("Éxito", msg, "success");
            document.getElementById("formRegistroCiudadano").reset();
        })
        .catch(error => {
            console.error("Error al registrar:", error);
            Swal.fire("Error", error.message, "error");
        });
    });
});