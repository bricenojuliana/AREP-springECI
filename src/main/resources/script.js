document.getElementById('imageForm').addEventListener('submit', async (event) => {
    event.preventDefault(); // Evita el envío tradicional del formulario

    const imageName = document.getElementById('imageName').value;
    const imageElement = document.getElementById('resultImage');

    try {
        const response = await fetch(`${imageName}`);
        if (response.ok) {
            // Obtiene la URL de la imagen y la muestra
            const imageBlob = await response.blob();
            const imageUrl = URL.createObjectURL(imageBlob);
            imageElement.src = imageUrl;
            imageElement.style.display = 'block'; // Muestra la imagen
        } else {
            // Maneja el error si la imagen no se encuentra
            alert('Image not found.');
            imageElement.style.display = 'none'; // Oculta la imagen en caso de error
        }
    } catch (error) {
        console.error('Error fetching image:', error);
    }
});

document.getElementById('nameForm').addEventListener('submit', async (event) => {
    event.preventDefault(); // Evita el envío tradicional del formulario

    const name = document.getElementById('nameInput').value;
    const resultDiv = document.getElementById('result');

    if (name.trim()) {
        resultDiv.innerHTML = `Hello, ${name}!`;
    } else {
        resultDiv.innerHTML = `Hello World!`;
    }

    // Enviar el nombre al servidor si es necesario
    // Puedes agregar aquí una solicitud fetch para enviar el nombre si es necesario.
});

document.getElementById('piButton').addEventListener('click', () => {
    const piValueElement = document.getElementById('piValue');
    piValueElement.textContent = `The value of Pi is approximately ${Math.PI.toFixed(5)}`;
});
