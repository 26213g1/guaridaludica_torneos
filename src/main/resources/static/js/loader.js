// /js/loader.js

document.addEventListener("DOMContentLoaded", function() {
    const loader = document.getElementById('global-loader');
    if (!loader) return;

    // Aplicamos los estilos críticos por código para garantizar que siempre esté bien centrado y oculto al inicio
    loader.style.cssText = "display: none; position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0, 0, 0, 0.5); z-index: 99999; justify-content: center; align-items: center;";

    // Activar el loader al hacer clic en enlaces válidos
    document.querySelectorAll('a').forEach(link => {
        link.addEventListener('click', function(e) {
            const href = this.getAttribute('href');
            const target = this.getAttribute('target');
            if (href && href !== '#' && !href.startsWith('#') && target !== '_blank' && !this.dataset.bsToggle) {
                loader.style.display = 'flex';
            }
        });
    });

    // Activar el loader al enviar formularios
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function() {
            loader.style.display = 'flex';
            
            const btn = this.querySelector('button[type="submit"]');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = `<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span> Procesando...`;
            }
        });
    });
});

// Limpiar al volver atrás en el navegador
window.addEventListener('pageshow', function(event) {
    const loader = document.getElementById('global-loader');
    if (loader) {
        loader.style.display = 'none';
    }
});