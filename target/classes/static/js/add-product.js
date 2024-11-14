
    // Function to preview the image
    function previewImage(event) {
        const file = event.target.files[0];
        const imagePreview = document.getElementById('imagePreview');
        const reader = new FileReader();

        reader.onload = function(e) {
            imagePreview.src = e.target.result;
            imagePreview.classList.remove('hidden'); // Show the image preview
        };

        if (file) {
            reader.readAsDataURL(file);
        } else {
            imagePreview.classList.add('hidden'); // Hide the image preview if no file is selected
        }
    }

    // Function to add tags
  function addTag(tagValue, tagId) {
         const tagsInputContainer = document.getElementById('tags-input-container');
         const inputField = document.getElementById('tags-input');
         const hiddenInput = document.getElementById('categories');
         const existingTags = hiddenInput.value ? hiddenInput.value.split(',') : [];

         if (!existingTags.includes(tagId)) {
             const tag = document.createElement('span');
             tag.className = 'bg-indigo-100 text-indigo-700 text-sm rounded-full px-2 py-1 mr-2 mb-2 inline-flex items-center';
             tag.textContent = tagValue;

             const removeBtn = document.createElement('button');
             removeBtn.type = 'button';
             removeBtn.className = 'ml-2 text-indigo-500 hover:text-indigo-700';
             removeBtn.innerHTML = '&times;';
             removeBtn.onclick = function() {
                 tagsInputContainer.removeChild(tag);
                 const newTags = existingTags.filter(id => id !== tagId);
                 hiddenInput.value = newTags.join(',');
             };

             tag.appendChild(removeBtn);
             tagsInputContainer.insertBefore(tag, inputField);

             existingTags.push(tagId);
             hiddenInput.value = existingTags.join(',');
         }
     }

     // Handle tag input functionality
     document.getElementById('tags-input').addEventListener('keypress', function(e) {
         if (e.key === 'Enter') {
             e.preventDefault();
             const tagValue = e.target.value.trim();
             const tagId = tagValue.toLowerCase().replace(/\s+/g, '-'); // Create a simple ID for example purposes
             if (tagValue) {
                 addTag(tagValue, tagId);
                 e.target.value = '';
             }
         }
     });

     // Add selected categories from dropdown to tag input
     document.getElementById('dropdown-categories').addEventListener('change', function(e) {
         const selectedOptions = Array.from(e.target.selectedOptions);
         selectedOptions.forEach(option => {
             const tagValue = option.text;
             const tagId = option.value;
             addTag(tagValue, tagId);
         });

         // Clear selected options in dropdown
         e.target.selectedIndex = -1;
     });