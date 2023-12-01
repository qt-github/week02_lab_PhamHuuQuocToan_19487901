const urlParams = new URLSearchParams(window.location.search);
var product_id = urlParams.get('id');
console.log('productId:', product_id);
function getCurrentDateTime() {
    const now = new Date();
    return now.toISOString().slice(0,16);
}

document.getElementById('price_date_time').value = getCurrentDateTime();

function addPrice() {
    const price = document.getElementById('price').value;
    const note = document.getElementById('note').value;
    const price_date_time = document.getElementById('price_date_time').value;

    if (!product_id) {
        alert('Product ID is required.');
        return;
    }

    const priceData = {
        price: price,
        note: note,
        price_date_time: price_date_time,
        product_id: parseFloat(product_id)
    };

    if (!priceData.product_id) {
        alert('Product ID cannot be null.');
        return;
    }

    fetch('http://localhost:8080/api/productPrices', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(priceData),
    }) .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
            console.log('Success:', data);
            alert('Price added successfully');
            window.location.href = `detailsProduct.html?id=${product_id}`;
        })
        .catch(error => {
            console.log('productId:', product_id);
            console.error('Error:', error);
            alert('Failed to add price');
        });
}