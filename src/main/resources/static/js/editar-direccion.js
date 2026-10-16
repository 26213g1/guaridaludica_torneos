document.addEventListener('DOMContentLoaded', function () {
    
    // 1. Lógica para rellenar el modal de edición de direcciones
    const editarModal = document.getElementById('editarDireccionModal');
    if (editarModal) {
        editarModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            if (button) {
                document.getElementById('edit-id').value = button.getAttribute('data-id') || '';
                document.getElementById('edit-alias').value = button.getAttribute('data-alias') || '';
                document.getElementById('edit-calle').value = button.getAttribute('data-calle') || '';
                document.getElementById('edit-numero').value = button.getAttribute('data-numero') || '';
                document.getElementById('edit-ciudad').value = button.getAttribute('data-ciudad') || '';
                document.getElementById('edit-cp').value = button.getAttribute('data-cp') || '';
                document.getElementById('edit-pais').value = button.getAttribute('data-pais') || '';
                document.getElementById('edit-principal').checked = button.getAttribute('data-principal') === 'true';
            }
        });
    }

    // 2. Lógica para recordar la pestaña activa
    const triggerTabList = document.querySelectorAll('#cuentaTabs button');
    triggerTabList.forEach(button => {
        button.addEventListener('click', event => {
            // Guardamos el selector de la pestaña al hacer clic
            sessionStorage.setItem('activeTab', event.target.getAttribute('data-bs-target'));
        });
    });

    // Al recargar la página, recuperamos y activamos la última pestaña guardada
    const activeTab = sessionStorage.getItem('activeTab');
    if (activeTab) {
        const tabTrigger = document.querySelector(`[data-bs-target="${activeTab}"]`);
        if (tabTrigger) {
            const tab = new bootstrap.Tab(tabTrigger);
            tab.show();
        }
    }

    // 3. Limpiar la pestaña activa si se hace clic en el botón/enlace de cerrar sesión
    const logoutBtn = document.querySelector('form[action*="/logout"] button, a[href*="/logout"]');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            sessionStorage.removeItem('activeTab');
        });
    }
});