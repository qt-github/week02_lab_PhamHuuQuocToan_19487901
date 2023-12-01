fetch('http://localhost:8080/api/products/all')
    .then(response => response.json())
    .then(data => {
        const productList = document.getElementById('productList');
        data.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                    <td>${product.product_id}</td>
                    <td>${product.name}</td>
                    <td>${product.description}</td>
                    <td>${product.unit}</td>
                    <td>${product.manufacturer}</td>
                    <td>${product.status}</td>
                    <td>
                            <a href="addPrice.html?id=${product.product_id}">Add Price</a>
                            <a href="detailsProduct.html?id=${product.product_id}">Details</a>
                            <a href="updateProduct.html?id=${product.product_id}">Update</a>
                            <a href="javascript:void(0);" onclick="deletePro(${product.product_id})">Delete</a>
                    </td>
                `;
            productList.appendChild(row);
        });
    });

function deletePro(proId) {
    const confirmDelete = confirm("Are you sure you want to delete this product?");

    if (confirmDelete) {
        fetch(`http://localhost:8080/api/products/${proId}`, {
            method: 'DELETE',
        })
            .then(response => response.json())
            .then(data => {
                alert("Delete Success");
                location.reload();
            })
            .catch(error => {
                alert('Failed to delete product');
            });
    }
}