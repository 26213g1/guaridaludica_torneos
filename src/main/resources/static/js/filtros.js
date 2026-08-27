document.addEventListener("DOMContentLoaded", function() {
    const formFiltros = document.querySelector('form[th\\:action*="/"]'); // O selecciona el form por su clase/acción
    
    // Si no lo encuentra por el atributo de thymeleaf, podemos seleccionarlo de forma más directa:
    const form = document.querySelector('form.d-flex.flex-column.gap-3');
    if (!form) return;

    // 1. Automatizar los selects y elementos de tipo cambio (Jugadores, Duración, etc.)
    const selects = form.querySelectorAll('select');
    selects.forEach(select => {
        select.addEventListener('change', function() {
            form.submit(); // Envía el formulario automáticamente al cambiar la opción
        });
    });

    // 2. Automatizar el slider de precio (con un pequeño retardo opcional para no saturar mientras se arrastra)
    const rangoPrecio = form.querySelector('.rango-precio-input');
    if (rangoPrecio) {
        let timeoutId;
        
        // Primero, mantenemos vivo el visor de texto en tiempo real que ya teníamos
        rangoPrecio.addEventListener('input', function() {
            const valor = this.value;
            const simbolo = this.getAttribute('data-simbolo') || '€';
            document.querySelectorAll('.valorPrecio').forEach(el => {
                el.innerText = valor + ' ' + simbolo;
            });
        });

        // Segundo, cuando el usuario suelta o termina de mover el slider, enviamos el formulario
        rangoPrecio.addEventListener('change', function() {
            form.submit();
        });
    }
});