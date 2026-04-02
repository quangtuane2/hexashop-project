function deleteProductAjax(buttonElement, productId) {
    if (confirm('Bạn có chắc chắn muốn khóa sản phẩm này?')) {
        
        // Dùng Fetch API gửi request ngầm lên Server
        fetch('/admin/product/delete-ajax/' + productId, {
            method: 'DELETE'
        })
        .then(async response => {
            if (response.ok) {
				let message = await response.text(); 
				            
	            // Xóa thông báo thành công cũ (nếu có) 
	            let oldSuccessMsg = document.getElementById('ajax-success-msg');
	            if (oldSuccessMsg) oldSuccessMsg.remove();

	            // Tạo một thẻ div thông báo màu xanh 
	            let successDiv = document.createElement('div');
	            successDiv.id = 'ajax-success-msg';
	            successDiv.className = 'alert alert-success alert-dismissible fade show';
	            successDiv.innerHTML = `
	                <i class="fa fa-check-circle me-1"></i> <strong>${message}</strong>
	                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
	            `;
	            
				let mainContainer = document.querySelector('.main-content') || document.body;
                mainContainer.prepend(successDiv);

				//Xóa dòng chứa sản phẩm khỏi bảng giao diện
                let row = buttonElement.closest('tr');
                if (row) {
                    row.remove();
                }
								
	            // (Tùy chọn UX) Tự động làm biến mất thông báo sau 3 giây cho gọn gàng
	            setTimeout(() => {
	                let msgEl = document.getElementById('ajax-success-msg');
	                if(msgEl) {
	                    msgEl.classList.remove('show'); // Hiệu ứng mờ dần của Bootstrap
	                    setTimeout(() => msgEl.remove(), 150); // Xóa hẳn khỏi HTML
	                }
	            }, 3000);
				
            } else {
				let errorMsg = await response.text();
                alert("⚠️ Lỗi: " + errorMsg);
            }
        })
		.catch(error => {
            console.error('Error:', error);
            alert("⚠️ Lỗi kết nối đến máy chủ!");
        });
    }
}

function submitEditAjax(event, productId, returnPage) {
    event.preventDefault(); 
	
    let formElement = document.getElementById('formEditProduct');

    // Chặn ngay từ Frontend nếu form có lỗi cơ bản (như required)
    if (!formElement.checkValidity()) {
        formElement.reportValidity(); 
        return; 
    }
            
    let formData = new FormData(formElement);
    let submitBtn = formElement.querySelector('button[type="submit"]');
    let originalBtnText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fa fa-spinner fa-spin"></i> Đang cập nhật...';
    submitBtn.disabled = true;

    formElement.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    formElement.querySelectorAll('.ajax-error').forEach(el => el.remove());

    fetch('/admin/product/edit-ajax/' + productId, {
        method: 'POST',
        body: formData 
    })
    .then(async response => {
		if (response.ok) {
            // NẾU THÀNH CÔNG
            let message = await response.text(); 
            
            // Xóa thông báo thành công cũ (nếu có) để tránh bị rác màn hình nếu user bấm lưu nhiều lần
            let oldSuccessMsg = document.getElementById('ajax-success-msg');
            if (oldSuccessMsg) oldSuccessMsg.remove();

            // Tạo một thẻ div thông báo màu xanh tuyệt đẹp của Bootstrap
            let successDiv = document.createElement('div');
            successDiv.id = 'ajax-success-msg';
            successDiv.className = 'alert alert-success alert-dismissible fade show';
            successDiv.innerHTML = `
                <i class="fa fa-check-circle me-1"></i> <strong>${message}</strong>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            `;
            
            // Chèn cái thông báo này vào ngay đầu Form
            formElement.prepend(successDiv);

            // (Tùy chọn UX) Tự động làm biến mất thông báo sau 3 giây cho gọn gàng
            setTimeout(() => {
                let msgEl = document.getElementById('ajax-success-msg');
                if(msgEl) {
                    msgEl.classList.remove('show'); // Hiệu ứng mờ dần của Bootstrap
                    setTimeout(() => msgEl.remove(), 150); // Xóa hẳn khỏi HTML
                }
            }, 5000);

            // Cập nhật "nóng" lại cái Tên sản phẩm trên Tiêu đề (thẻ h1) nếu Admin có sửa tên
            let newName = formElement.querySelector('[name="name"]').value;
            let headerTitle = document.querySelector('h1 span.text-primary');
            if(headerTitle) headerTitle.innerText = newName;
			
			window.location.href = '/admin/product?page=' + returnPage;
        } else {
            // NẾU CÓ LỖI (Mã 400)
            const contentType = response.headers.get("content-type");
            
            // Nếu Server trả về JSON (Lỗi từng ô nhập liệu)
            if (contentType && contentType.indexOf("application/json") !== -1) {
                let errors = await response.json();
                
                // Quét từng lỗi và bôi đỏ ô tương ứng
                for (const [field, message] of Object.entries(errors)) {
                    let inputEl = document.querySelector(`[name="${field}"]`); // Tìm ô input (vd: name="price")
                    if (inputEl) {
                        inputEl.classList.add('is-invalid'); // Cung cấp viền đỏ cho thẻ input
                        
                        // Tạo một cái <div> chứa chữ màu đỏ dán ngay bên dưới ô input
                        let errorDiv = document.createElement('div');
                        errorDiv.className = 'invalid-feedback ajax-error';
                        errorDiv.innerText = message;
                        inputEl.parentNode.insertBefore(errorDiv, inputEl.nextSibling);
                    }
                }
            } else {
                // Nếu Server trả về lỗi chung chung (vd: "Sản phẩm không tồn tại")
                let message = await response.text();
                alert("⚠️ Lỗi: " + message);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("⚠️ Lỗi kết nối đến máy chủ!");
    })
    .finally(() => {
        submitBtn.innerHTML = originalBtnText;
        submitBtn.disabled = false;
    });
}

function deleteCategoryAjax(buttonElement, categoryId) {
    if (confirm('Bạn có chắc chắn muốn khóa/xóa danh mục này?')) {
        
        fetch('/admin/category/delete-ajax/' + categoryId, {
            method: 'DELETE'
        })
        .then(async response => {
            if (response.ok) {
                let message = await response.text(); 
                
                // Hiển thị thông báo màu xanh (Dùng chung cấu trúc với Product)
                let oldSuccessMsg = document.getElementById('ajax-success-msg');
                if (oldSuccessMsg) oldSuccessMsg.remove();

                let successDiv = document.createElement('div');
                successDiv.id = 'ajax-success-msg';
                successDiv.className = 'alert alert-success alert-dismissible fade show mb-3';
                successDiv.innerHTML = `
                    <i class="fa fa-check-circle me-1"></i> <strong>${message}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                `;
                
                let mainContainer = document.querySelector('.main-content') || document.body;
                mainContainer.prepend(successDiv);

                // Xóa dòng chứa danh mục
                buttonElement.closest('tr').remove();

                setTimeout(() => {
                    let msgEl = document.getElementById('ajax-success-msg');
                    if(msgEl) {
                        msgEl.classList.remove('show'); 
                        setTimeout(() => msgEl.remove(), 150); 
                    }
                }, 3000);
            } else {
                let errorMsg = await response.text();
                alert("⚠️ Lỗi: " + errorMsg);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("⚠️ Lỗi kết nối đến máy chủ!");
        });
    }
}

function submitEditCategoryAjax(event, categoryId, returnPage) {
    event.preventDefault(); 
    
    let formElement = document.getElementById('formEditCategory'); // Tìm đúng ID form category

    if (!formElement.checkValidity()) {
        formElement.reportValidity(); 
        return; 
    }
            
    let formData = new FormData(formElement);
    let submitBtn = formElement.querySelector('button[type="submit"]');
    let originalBtnText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fa fa-spinner fa-spin"></i> Đang cập nhật...';
    submitBtn.disabled = true;

    // Xóa cảnh báo đỏ cũ
    formElement.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    formElement.querySelectorAll('.ajax-error').forEach(el => el.remove());

    // CHÚ Ý CHỖ NÀY: Gửi đến URL của category
    fetch('/admin/category/edit-ajax/' + categoryId, {
        method: 'POST',
        body: formData 
    })
    .then(async response => {
        if (response.ok) {
            let message = await response.text(); 
            
            let oldSuccessMsg = document.getElementById('ajax-success-msg');
            if (oldSuccessMsg) oldSuccessMsg.remove();

            let successDiv = document.createElement('div');
            successDiv.id = 'ajax-success-msg';
            successDiv.className = 'alert alert-success alert-dismissible fade show';
            successDiv.innerHTML = `
                <i class="fa fa-check-circle me-1"></i> <strong>${message}</strong>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            `;
            
            formElement.prepend(successDiv);

            // Tự động load lại trang danh sách Category sau khi thành công
            setTimeout(() => {
                window.location.href = '/admin/category?page=' + returnPage;
            }, 2000); // Cho trễ 1 giây để user kịp nhìn thấy thông báo xanh nhấp nháy 

        } else {
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                let errors = await response.json();
                for (const [field, message] of Object.entries(errors)) {
                    let inputEl = document.querySelector(`[name="${field}"]`); 
                    if (inputEl) {
                        inputEl.classList.add('is-invalid'); 
                        let errorDiv = document.createElement('div');
                        errorDiv.className = 'invalid-feedback ajax-error';
                        errorDiv.innerText = message;
                        inputEl.parentNode.insertBefore(errorDiv, inputEl.nextSibling);
                    }
                }
            } else {
                let message = await response.text();
                alert("⚠️ Lỗi: " + message);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("⚠️ Lỗi kết nối đến máy chủ!");
    })
    .finally(() => {
        submitBtn.innerHTML = originalBtnText;
        submitBtn.disabled = false;
    });
}