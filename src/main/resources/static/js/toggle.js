 document.addEventListener("DOMContentLoaded", function() {
        const dropdownButton = document.getElementById('userDropdownButton');
        const dropdownMenu = document.getElementById('userDropdownMenu');

        dropdownButton.addEventListener('click', function() {
            dropdownMenu.classList.toggle('hidden');
        });

        // Đóng dropdown menu khi click ra ngoài
        document.addEventListener('click', function(event) {
            if (!dropdownButton.contains(event.target) && !dropdownMenu.contains(event.target)) {
                dropdownMenu.classList.add('hidden');
            }
        });
    });