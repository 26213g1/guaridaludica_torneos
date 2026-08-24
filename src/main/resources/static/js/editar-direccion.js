document.addEventListener('DOMContentLoaded', function () {
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
});