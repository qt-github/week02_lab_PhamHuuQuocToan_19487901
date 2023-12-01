function displayImages(images) {
    document.getElementById('image_id').innerText = images.image_id;
    document.getElementById('path').src = images.path;
    document.getElementById('alternative').innerText = images.alternative;
}

function formatArrayDate(dateArray) {
    const date = new Date(...dateArray);
    const formattedDate = date.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
    });
    return formattedDate;
}

function displayPrices(prices) {
    prices.sort((a, b) => new Date(b.price_date_time) - new Date(a.price_date_time));
    const priceTableBody = document.getElementById('priceTableBody');
    priceTableBody.innerHTML = '';
    prices.forEach(price => {
        const row = priceTableBody.insertRow();
        row.insertCell(0).innerText = formatArrayDate(price.price_date_time);
        row.insertCell(1).innerText = price.price.toFixed(2);
        row.insertCell(2).innerText = price.note;
    });
}

function fetchProductDetails(productId) {
    fetch(`http://localhost:8080/api/productImages/${productId}`)
        .then(response => response.json())
        .then(images => displayImages(images))
        .catch(error => console.error('Error fetching product images:', error));

    fetch(`http://localhost:8080/api/productPrices/${productId}`)
        .then(response => response.json())
        .then(prices => displayPrices(prices))
        .catch(error => console.error('Error fetching product prices:', error));
}

const urlParams = new URLSearchParams(window.location.search);
const productId = urlParams.get('id');

if (productId) {
    fetchProductDetails(productId);
} else {
    console.error('Product ID not provided in the URL');
}

function goBack() {
    window.history.back();
}