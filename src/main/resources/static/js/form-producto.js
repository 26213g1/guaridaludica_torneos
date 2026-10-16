document.addEventListener('DOMContentLoaded', function() {
    const inputImagenes = document.getElementById('inputImagenes');
    
    if (inputImagenes) {
        inputImagenes.addEventListener('change', function(event) {
            const container = document.getElementById('preview-container');
            container.innerHTML = ''; // Limpiamos las previas si vuelve a elegir otras

            const files = event.target.files;
            
            if (files) {
                Array.from(files).forEach((file, index) => {
                    if (!file.type.startsWith('image/')) return;

                    const reader = new FileReader();
                    
                    reader.onload = function(e) {
                        const wrapper = document.createElement('div');
                        wrapper.classList.add('card', 'p-1', 'text-center');
                        wrapper.style.width = '90px';

                        const img = document.createElement('img');
                        img.src = e.target.result;
                        img.classList.add('img-fluid', 'rounded');
                        img.style.height = '70px';
                        img.style.objectFit = 'cover';

                        const small = document.createElement('small');
                        small.classList.add('text-muted', 'd-block', 'mt-1');
                        small.style.fontSize = '10px';
                        small.textContent = (index === 0) ? 'Principal*' : 'Nueva';

                        wrapper.appendChild(img);
                        wrapper.appendChild(small);
                        container.appendChild(wrapper);
                    }
                    
                    reader.readAsDataURL(file);
                });
            }
        });
    }
});