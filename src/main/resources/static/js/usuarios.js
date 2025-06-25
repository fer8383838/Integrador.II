




// ---------------------------------------------
// Archivo: usuarios.js
// Descripción: Controla toda la lógica de la vista usuarios.html.
// Incluye: validación de rol, registro, listado, búsqueda y cierre de sesión.
// ---------------------------------------------

// URL base de los endpoints del backend para la entidad Usuario
const API_URL = 'http://localhost:8080/usuarios';

const ZONAS_API_URL = 'http://localhost:8080/zonas'

// Recupera el token JWT almacenado localmente al momento del login
const token = localStorage.getItem('token');










// Evento principal: se ejecuta cuando el DOM ya ha sido cargado
document.addEventListener('DOMContentLoaded', async () => {
    try {

        // Si no hay token, redirigir inmediatamente al login
                if (!token) {
                    window.location.href = 'login.html';
                    return;
                }


        // Solicita al backend la información del usuario actual y sus permisos visuales
        const res = await fetch(`${API_URL}/info-rol-actual`, {
            headers: {
                Authorization: `Bearer ${token}` // Se envía el token como encabezado
            }
        });

        // Si falla, se lanza error visual y por consola
        if (!res.ok) throw new Error('No se pudo obtener permisos');

        const permisos = await res.json();
        const usuarioID = permisos.usuarioID;

        // El backend define si se debe mostrar el formulario de registro
        if (!permisos.mostrarFormularioRegistro) {
            document.getElementById('formUsuario')?.classList.add('d-none');
            document.getElementById('registro-usuario')?.classList.add('d-none');
        }

        //Mostrar campode seleccion rol si esta permitido: admin
        if (permisos.mostrarFormularioRegistro) {
            // Por diseño: se oculta el campo de selección de rol para evitar manipulación
            document.getElementById('rol')?.closest('.col-md-6')?.classList.remove('d-none');
            cargarZonas();
        }


        // Si no tiene permiso para buscar, se oculta el input y botón de búsqueda
        if (!permisos.mostrarBuscarPorID) {
            document.getElementById('buscadorPorID')?.classList.add('d-none');
            document.getElementById('contenedorBusqueda')?.classList.add('d-none');
        }

        // El botón "Ver Todos" también es controlado por permisos del backend
        if (!permisos.mostrarBotonVerTodos) {
            document.getElementById('btnVerTodos')?.classList.add('d-none');
        }

        // La tabla general de usuarios también se oculta según el rol
        if (!permisos.mostrarTablaUsuarios) {
            document.getElementById('tablaCompletaUsuarios')?.classList.add('d-none');
            document.getElementById('contenedorTablaUsuario')?.classList.add('d-none');
        }

        // Si el rol es administrador, carga todos los usuarios
        // Caso contrario, solo carga su propio perfil con dirección
        if (permisos.mostrarBotonVerTodos) {
            cargarUsuarios();
        } else {

            //Muestra perfil del usuario logueado con su direccion
            buscarPorID(usuarioID);
        }

    } catch (err) {
        console.error('Error en validación de rol:', err);
        Swal.fire('Error', 'No se pudo validar el rol actual', 'error');
    }
});





// ----------------------------------------------------------
// Evento para enviar el formulario de registro de usuarios.
// Se usa para registrar ciudadanos o, si el rol es admin, también operarios/supervisores.
// ----------------------------------------------------------
document.getElementById('formUsuario')?.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Arma el objeto DTO que será enviado al backend
    const dto = {
        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        dni: document.getElementById('dni').value,
        email: document.getElementById('email').value,
        claveHash: document.getElementById('claveHash').value,
        telefono: document.getElementById('telefono').value,
        rol: document.getElementById('rol').value,
        zonaID: document.getElementById('zonaID').value,
        distrito: document.getElementById('distrito').value,
        direccion: document.getElementById('direccion').value,
        latitud: document.getElementById('latitud').value,
        longitud: document.getElementById('longitud').value,
        principal: true
    };

    // ------------------------------------------------------------------
    // Si el rol seleccionado es Supervisor u Operario, también enviamos zonaID
    // Esto se hace para respetar la estructura del DTO y la base de datos actual.
    // ------------------------------------------------------------------
    const rolSeleccionado = dto.rol;

    if (rolSeleccionado === "Supervisor" || rolSeleccionado === "Operario") {
        const zonaID = document.getElementById('zonaID')?.value;
        if (zonaID) {
            dto.zonaID = parseInt(zonaID); // Se agrega como entero, como está definido en la base de datos
        }
    }

    try {
        // Envío del formulario al backend usando el endpoint registrar-completo
        const res = await fetch(`${API_URL}/registrar-completo`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify(dto)
        });

        const respuesta = await res.text();

        if (res.ok) {
            Swal.fire('Éxito', respuesta, 'success');
            document.getElementById('formUsuario').reset();
            cargarUsuarios(); // Refresca la lista si está visible
        } else {
            console.error('Error:', respuesta);
            Swal.fire('Error', respuesta, 'error');
        }

    } catch (err) {
        console.error('Error al registrar:', err);
        Swal.fire('Error', 'Error de conexión con el servidor', 'error');
    }
});



// ------------------------------------------------------------------
// Función que obtiene todos los usuarios desde el backend
// Solo es utilizada por usuarios con rol Administrador
// ------------------------------------------------------------------
async function cargarUsuarios() {
    try {
        const res = await fetch(`${API_URL}/listar`, {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!res.ok) {
            const msg = await res.text();
            console.error('Error al listar usuarios:', msg);
            Swal.fire('Error', msg, 'error');
            return;
        }

        const data = await res.json();
        const tabla = document.getElementById('tablaUsuarios');
        if (!tabla) return;

        tabla.innerHTML = '';

        // Genera dinámicamente cada fila de usuario
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
                <td>${u.nombreZona || '-'}</td>
                <td>${u.activo ? 'Sí' : 'No'}</td>
            `;
            tabla.appendChild(fila);
        });

    } catch (err) {
        console.error('Error al conectar con el servidor:', err);
        Swal.fire('Error', 'Error de red o conexión', 'error');
    }
}



// Variable global que indica si se ha realizado una búsqueda por ID exitosa.
// Se utiliza para decidir si se debe mostrar el botón "Ver Todos" tras una búsqueda fallida.
let tablaFiltrada = false;


// --------------------------------------------------------------------
// Función para obtener datos de un solo usuario y su dirección principal.
// Se usa cuando el administrador filtra por ID o cuando un ciudadano ve su propio perfil.
// Esta función también decide si se debe mostrar el botón "Ver Todos".
// --------------------------------------------------------------------
async function buscarPorID(id = null) {
    // Si no se pasa un ID directamente, se intenta obtener desde el input del formulario
    if (!id) {
        const campo = document.getElementById('buscarID');
        if (!campo) return;
        id = campo.value;
    }

    // Si el campo está vacío, se lanza una advertencia al usuario
    if (!id) {
        Swal.fire('Advertencia', 'Debe ingresar un ID', 'warning');
        return;
    }

    try {
        // Llamada al backend usando el endpoint que devuelve el usuario por ID
        const res = await fetch(`${API_URL}/obtener-principal/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
        });

        // El backend siempre devuelve texto, por lo tanto se almacena así primero
        const content = await res.text();

        // Si hubo error (por ejemplo, ID inválido o sin permisos)
        if (!res.ok) {
            console.error('Error al buscar:', content);
            Swal.fire('Error', content || 'Usuario no encontrado', 'error');

            // Si aún no se había hecho ningún filtro, se oculta el botón "Ver Todos"
            if (!tablaFiltrada) {
                document.getElementById('btnVerTodos').style.display = 'none';
            }
            return;
        }

        // Convertir el contenido JSON recibido en un objeto
        const u = JSON.parse(content);

        // Mostrar los datos personales del usuario en la tabla principal
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
                <td>${u.nombreZona || '-'}</td>
                <td>${u.activo ? 'Sí' : 'No'}</td>
            </tr>
        `;

        document.getElementById('contenedorTablaUsuario').style.display = 'block';



        // ----------------------------------------------------------
        // Si el usuario es un Supervisor, mostrar los operarios asignados a su zona
        // ----------------------------------------------------------
        if (u.rol === 'Supervisor' && u.operariosACargo && u.operariosACargo.length > 0) {
            const cuerpoTabla = document.getElementById('cuerpoTablaOperarios');
            cuerpoTabla.innerHTML = '';

            u.operariosACargo.forEach(op => {
                const fila = document.createElement('tr');
                fila.innerHTML = `
                    <td>${op.usuarioID}</td>
                    <td>${op.nombre}</td>
                    <td>${op.apellido}</td>
                    <td>${op.dni}</td>
                    <td>${op.email}</td>
                    <td>${op.telefono || '-'}</td>
                    <td>${op.rol}</td>
                `;
                cuerpoTabla.appendChild(fila);
            });

            document.getElementById('tablaOperariosAsignados').style.display = 'block';
        } else {
            // Si no hay operarios o no es supervisor, se oculta la tabla
            document.getElementById('tablaOperariosAsignados').style.display = 'none';
        }


        // Mostrar la dirección principal asociada al usuario
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

        // Si la búsqueda fue exitosa, se activa el botón "Ver Todos"
        tablaFiltrada = true;
        document.getElementById('btnVerTodos').style.display = 'inline-block';

    } catch (err) {
        console.error('Error en búsqueda:', err);
        Swal.fire('Error', 'No se pudo procesar la búsqueda', 'error');

        // Si no se había filtrado antes, se oculta el botón para evitar confusión
        if (!tablaFiltrada) {
            document.getElementById('btnVerTodos').style.display = 'none';
        }
    }
}




// ----------------------------------------------------
// Función auxiliar para restaurar la tabla completa de usuarios
// Solo se usa luego de aplicar un filtro por ID y querer volver a ver todos
// También limpia la sección de direcciones del usuario filtrado anterior
// ----------------------------------------------------
function VerTodosUsuarios() {

    cargarUsuarios(); // Carga la lista completa de usuarios desde el backend


    // Oculta el botón una vez restaurada la lista completa
    document.getElementById('btnVerTodos').style.display = 'none';


    // Limpia y oculta la tabla de direcciones del usuario anteriormente filtrado
    document.getElementById('tablaDirecciones').innerHTML = '';
    document.getElementById('contenedorDirecciones').style.display = 'none';


    // ----------------------------------------------------------
    // También se oculta la tabla de operarios asignados si estaba visible
    // ----------------------------------------------------------
    document.getElementById('cuerpoTablaOperarios').innerHTML = '';
    document.getElementById('tablaOperariosAsignados').style.display = 'none';

}













// -------------------------------------------------------------
// Función que obtiene las zonas del backend y llena el select
// -------------------------------------------------------------
async function cargarZonas() {
    try {
        const res = await fetch(`${ZONAS_API_URL}/listar`, {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (!res.ok) {
            console.error('Error al obtener zonas:', await res.text());
            return;
        }

        const zonas = await res.json();
        const selectZona = document.getElementById('zonaID');

        // Limpiar opciones anteriores
        selectZona.innerHTML = '<option value="">Seleccione una zona</option>';

        // Agrega cada zona como opción
        zonas.forEach(z => {
            const opcion = document.createElement('option');
            opcion.value = z.zonaID;
            opcion.textContent = z.nombre;
            selectZona.appendChild(opcion);
        });

    } catch (err) {
        console.error('Error al cargar zonas:', err);
    }
}




// ------------------------------------------------------------------
// Evento que se activa cuando el usuario hace clic en "Cerrar sesión"
// Llama al backend para invalidar el token y limpia los datos del cliente
// ------------------------------------------------------------------
document.getElementById('btnLogout')?.addEventListener('click', async () => {
    if (!token) {
        Swal.fire('Error', 'No hay sesión activa', 'error');
        return;
    }

    try {
        const res = await fetch(`${API_URL}/cerrar-sesion`, {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (res.ok) {
            // Limpieza de datos locales y redirección al login
            localStorage.removeItem('token');
            localStorage.removeItem('usuarioID');
            localStorage.removeItem('rol');

            // Elimina la clave 'nombre' del localStorage si existe
            localStorage.removeItem('nombre');

            Swal.fire('Sesión cerrada', 'Vuelva pronto', 'success').then(() => {
                window.location.href = 'login.html';
            });

        } else {
            const msg = await res.text();
            Swal.fire('Error al cerrar sesión', msg, 'error');
        }

    } catch (err) {
          console.error('Error en logout:', err);
          localStorage.removeItem('token');
          localStorage.removeItem('usuarioID');
          localStorage.removeItem('rol');


          // Elimina la clave 'nombre' del localStorage si existe
          localStorage.removeItem('nombre');


          Swal.fire('Error', 'Error de conexión con el servidor', 'error');
          window.location.href = 'login.html';
      }
});
