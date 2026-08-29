// 1. Actualizar el visor de precio en tiempo real del formulario exacto que se está moviendo
document.addEventListener("input", function(event) {
    if (event.target && event.target.classList.contains('rango-precio-input')) {
        const input = event.target;
        const valor = input.value;
        const simbolo = input.getAttribute('data-simbolo') || '€';
        
        // Buscamos el formulario padre del input que se está moviendo
        const form = input.closest('form');
        if (form) {
            const visor = form.querySelector('.valorPrecio');
            if (visor) {
                visor.innerText = valor + ' ' + simbolo;
            }
        }
    }
});

// 2. Enviar automáticamente el formulario correcto al cambiar un select o soltar el slider
document.addEventListener("change", function(event) {
    const target = event.target;
    
    // Si el cambio ocurre en un select o en el input range de nuestros filtros
    if (target.matches('select') || target.matches('.rango-precio-input')) {
        const form = target.closest('form');
        // Nos aseguramos de que sea el formulario de filtros (el que tiene el rango de precio)
        if (form && form.querySelector('.rango-precio-input')) {
            form.submit();
        }
    }
});