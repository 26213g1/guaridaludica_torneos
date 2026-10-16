document.addEventListener('DOMContentLoaded', function () {
    const collapseElement = document.getElementById('collapseDestacados');
    const botonDestacados = document.querySelector('[data-bs-target="#collapseDestacados"]');
    const storageKey = 'guarida_destacados_oculto';
    
    if (!collapseElement) return;

    // 1. Al cargar, comprobamos si el usuario lo guardó como oculto
    const estaOculto = localStorage.getItem(storageKey) === 'true';
    
    if (estaOculto) {
        collapseElement.classList.remove('show');
        if (botonDestacados) {
            botonDestacados.querySelector('span').textContent = 'Ver';
            const icono = botonDestacados.querySelector('i');
            if (icono) icono.className = 'bi bi-chevron-down';
        }
    }

    // 2. Evento al ocultar
    collapseElement.addEventListener('hidden.bs.collapse', function () {
        localStorage.setItem(storageKey, 'true');
        if (botonDestacados) {
            botonDestacados.querySelector('span').textContent = 'Ver';
            const icono = botonDestacados.querySelector('i');
            if (icono) icono.className = 'bi bi-chevron-down';
        }
    });

    // 3. Evento al mostrar
    collapseElement.addEventListener('shown.bs.collapse', function () {
        localStorage.setItem(storageKey, 'false');
        if (botonDestacados) {
            botonDestacados.querySelector('span').textContent = 'Ocultar';
            const icono = botonDestacados.querySelector('i');
            if (icono) icono.className = 'bi bi-chevron-up';
        }
    });
});