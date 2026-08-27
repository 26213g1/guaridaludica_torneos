document.addEventListener("DOMContentLoaded", function() {
    const rangos = document.querySelectorAll('.rango-precio-input');
    
    if (rangos.length === 0) return;

    rangos.forEach(rango => {
        rango.addEventListener('input', function() {
            const valor = this.value;
            // Leemos el símbolo guardado en el atributo data-simbolo del input
            const simbolo = this.getAttribute('simboloMoneda') || '€';
            
            // Actualizamos todos los visores de precio en pantalla (tanto móvil como escritorio)
            document.querySelectorAll('.valorPrecio').forEach(el => {
                el.innerText = valor + ' ' + simbolo;
            });
        });
    });
});