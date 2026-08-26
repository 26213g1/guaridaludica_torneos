document.addEventListener("DOMContentLoaded", function() {
    const descCompleta = document.getElementById('descripcionCompleta');
    const btnLeerMas = document.getElementById('btnLeerMas');
    const descCorta = document.getElementById('descripcionCorta');
    
    if (descCompleta && btnLeerMas) {
        descCompleta.addEventListener('show.bs.collapse', function () {
            if (descCorta) descCorta.style.display = 'none'; // Oculta el resumen corto al desplegar
            btnLeerMas.textContent = 'Leer menos';
        });
        descCompleta.addEventListener('hide.bs.collapse', function () {
            if (descCorta) descCorta.style.display = 'block'; // Vuelve a mostrar el resumen al contraer
            btnLeerMas.textContent = 'Leer más...';
        });
    }
});