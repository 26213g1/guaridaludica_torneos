document.addEventListener('DOMContentLoaded', function() {
    const selectorDireccion = document.getElementById('selectorDireccion');

    if (selectorDireccion) {
        selectorDireccion.addEventListener('change', function() {
            var opt = this.options[this.selectedIndex];
            
            if (opt.value !== "") {
                document.getElementById("alias").value = opt.getAttribute("data-alias") || "";
                document.getElementById("calle").value = opt.getAttribute("data-calle") || "";
                document.getElementById("numero").value = opt.getAttribute("data-numero") || "";
                document.getElementById("ciudad").value = opt.getAttribute("data-ciudad") || "";
                document.getElementById("codigoPostal").value = opt.getAttribute("data-cp") || "";
            } else {
                document.getElementById("alias").value = "";
                document.getElementById("calle").value = "";
                document.getElementById("numero").value = "";
                document.getElementById("ciudad").value = "";
                document.getElementById("codigoPostal").value = "";
            }
        });
    }
});