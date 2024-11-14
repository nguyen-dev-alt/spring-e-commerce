async function searchProducts() {
    const query = document.getElementById('searchInput').value;
    const searchResults = document.getElementById('searchResults');

    if (query.length < 2) {
        searchResults.classList.add('hidden');
        return;
    }

    const response = await fetch(`/find?query=${query}`);
    const products = await response.json();

    searchResults.innerHTML = ''; // Clear previous results
    if (products.length > 0) {
        products.forEach(product => {
            const item = document.createElement('a');
            item.href = `/products/{id}(id=${product.id})`;
            item.classList.add('block', 'px-4', 'py-2', 'hover:bg-gray-100');
            item.innerHTML = `<div class="flex items-center">
                                    <img src="/images/${product.image}" alt="Product Image" class="avatar mr-3"/>
                                    <span>${product.name}</span>
                                  </div>`;
            searchResults.appendChild(item);
        });
        searchResults.classList.remove('hidden');
    } else {
        searchResults.classList.add('hidden');
    }
}

// Hide search results when clicking outside
document.addEventListener('click', (event) => {
    const searchResults = document.getElementById('searchResults');
    if (!document.getElementById('searchInput').contains(event.target)) {
        searchResults.classList.add('hidden');
    }
});