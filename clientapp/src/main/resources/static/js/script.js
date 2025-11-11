// Handler ajax
let buttonText;
$.ajaxSetup({
 beforeSend: function (xhr, settings) {
  const $btn = $("button[type='submit']");
  let buttonText = $btn.text();
  if ($btn.length) {
   $btn.prop("disabled", true).addClass("opacity-70 cursor-not-allowed");
   $btn.data("original-text", $btn.html());
   $btn.html(`
                <svg aria-hidden="true" role="status" class="inline w-4 h-4 me-2 text-white animate-spin" viewBox="0 0 100 101" fill="none">
                    <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="#E5E7EB"/>
                    <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5532C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.51885 51.7191 9.52621 55.5028 10.132C60.8642 10.9639 65.9928 12.7429 70.6331 15.3926C75.2735 18.0423 79.3347 21.5095 82.5849 25.6392C84.9175 28.6517 86.7997 32.0413 88.1628 35.6777C89.083 38.026 91.5421 39.6781 93.9676 39.0409Z" fill="currentColor"/>
                </svg>
                Memproses...
            `);
  }
 },

 complete: function (xhr, status) {
  const $btn = $("button[type='submit'][disabled]");
  if ($btn.length) {
   $btn
    .prop("disabled", false)
    .removeClass("opacity-70 cursor-not-allowed")
    .html($btn.data("original-html") || "Submit");
  }
 },
 error: function (xhr) {
  $(".input-error").remove();

  let response;
  try {
   response = JSON.parse(xhr.responseText);
  } catch (e) {}

  if (xhr.status === 401) {
   Swal.fire({
    icon: "error",
    title: "Gagal",
    text: response?.message || "Pastikan anda memiliki akses yang benar.",
    confirmButtonText: "OK",
   }).then(() => {
    const currentPath = window.location.pathname;

    if (currentPath.includes("/login")) {
     return;
    }

    window.location.href = "/logout";
   });
   return;
  }

  // Jika ada validasi error (422)
  if (response && response.errors) {
   Object.keys(response.errors).forEach(function (key) {
    const input = $(`#${key}`);
    if (input.length) {
     input.addClass("border-red-500").after(`<div class="text-red-500 text-sm mt-1 input-error">${response.errors[key]}</div>`);
    }
   });
  } else {
   Swal.fire("Gagal", response?.message || "Terjadi kesalahan pada server.", "error");
  }
 },
});

document.addEventListener("click", function (event) {
 const modalTargetEl = event.target.closest("[data-modal-target]");
 const modalHideEl = event.target.closest("[data-modal-hide]");

 if (modalTargetEl) {
  const modalId = modalTargetEl.getAttribute("data-modal-target");
  const modalElement = document.getElementById(modalId);
  if (modalElement) {
   new Modal(modalElement).show();
  }
 }

 if (modalHideEl) {
  const modalId = modalHideEl.getAttribute("data-modal-hide");
  const modalElement = document.getElementById(modalId);
  if (modalElement) {
   new Modal(modalElement).hide();
  }
 }
});

function formatDate(isoString) {
  if (!isoString) return '-';
  try {
    const date = new Date(isoString);
    return date.toLocaleString('id-ID', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch {
    return isoString;
  }
}

// User dropdown functionality
document.addEventListener('DOMContentLoaded', function() {
    const userMenuButton = document.getElementById('user-menu-button');
    const userDropdown = document.getElementById('user-dropdown');
    
    if (userMenuButton && userDropdown) {
        userMenuButton.addEventListener('click', function() {
            userDropdown.classList.toggle('hidden');
        });
        
        // Close dropdown when clicking outside
        document.addEventListener('click', function(event) {
            if (!userMenuButton.contains(event.target) && !userDropdown.contains(event.target)) {
                userDropdown.classList.add('hidden');
            }
        });
    }
});
