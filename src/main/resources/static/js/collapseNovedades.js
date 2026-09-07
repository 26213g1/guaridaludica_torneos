document.addEventListener('DOMContentLoaded', function () {
    const collapseElement = document.getElementById('collapseNovedades');
    const botonNovedades = document.querySelector('[data-bs-target="#collapseNovedades"]');
    
    if (!collapseElement) return;

    // 1. Al cargar la página, comprobamos si el usuario lo guardó como oculto
    const estaOculto = localStorage.getItem('guarida_novedades_oculto') === 'true';
    
    if (estaOculto) {
        // Si estaba oculto, removemos la clase 'show' de Bootstrap y cambiamos el texto/icono del botón
        collapseElement.classList.remove('show');
        if (botonNovedades) {
            botonNovedades.querySelector('span').textContent = 'Ver';
            const icono = botonNovedades.querySelector('i');
            if (icono) icono.className = 'bi bi-chevron-down';
        }
    }

    // 2. Escuchamos cuando el usuario hace clic para ocultarlo
    collapseElement.addEventListener('hidden.bs.collapse', function () {
        localStorage.setItem('guarida_novedades_oculto', 'true');
        if (botonNovedades) {
            botonNovedades.querySelector('span').textContent = 'Ver';
            const icono = botonNovedades.querySelector('i');
            if (icono) icono.className = 'bi bi-chevron-down';
        }
    });

    // 3. Escuchamos cuando el usuario hace clic para mostrarlo
    collapseElement.addEventListener('shown.bs.collapse', function () {
        localStorage.setItem('guarida_novedades_oculto', 'false');
        if (botonNovedades) {
            botonNovedades.querySelector('span').textContent = 'Ocultar';
            const icono = botonNovedades.querySelector('i');
            if (icono) icono.className = 'bi bi-chevron-up';
        }
    });
});