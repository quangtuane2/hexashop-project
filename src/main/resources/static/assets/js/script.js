// Hiệu ứng Sticky Header khi cuộn
window.addEventListener("scroll", function() {
    var header = document.querySelector("header");
    // Nếu cuộn quá 100px thì thêm class 'sticky' (bạn có thể style thêm trong CSS)
    header.classList.toggle("sticky", window.scrollY > 0);
});

// Hiệu ứng Click vào menu thì Active
var navLinks = document.querySelectorAll(".nav li a");

navLinks.forEach(link => {
    link.addEventListener("click", function() {
        // Xóa class active ở tất cả thẻ a
        navLinks.forEach(nav => nav.classList.remove("active"));
        // Thêm class active vào thẻ vừa click
        this.classList.add("active");
    });
});

//  XỬ LÝ NÚT TĂNG GIẢM SỐ LƯỢNG 
const plusBtns = document.querySelectorAll('.plus');
const minusBtns = document.querySelectorAll('.minus');

plusBtns.forEach(btn => {
    btn.addEventListener('click', function() {
        let input = this.previousElementSibling; // Lấy thẻ input ngay trước nút +
        let value = parseInt(input.value);
        input.value = value + 1;
    });
});

minusBtns.forEach(btn => {
    btn.addEventListener('click', function() {
        let input = this.nextElementSibling; // Lấy thẻ input ngay sau nút -
        let value = parseInt(input.value);
        if (value > 1) { // Không cho giảm dưới 1
            input.value = value - 1;
        }
    });
});

/* SLIDER CONFIGURATION  */

function initProductSlider(sliderId, arrowId) {
    // Kiểm tra xem element có tồn tại không trước khi chạy slick để tránh lỗi
    if ($(sliderId).length) {
        $(sliderId).slick({
            dots: false,
            infinite: true,
            speed: 500,
            autoplay: true,
            autoplaySpeed: 3000,
            slidesToShow: 4, 
            slidesToScroll: 2,
            arrows: true,
            appendArrows: $(arrowId), // Gắn nút vào div cụ thể của từng mục
            
            // Icon mũi tên 
            nextArrow: '<button type="button" class="slick-next dev-slick-next"><i class="fa fa-angle-right"></i></button>',
            prevArrow: '<button type="button" class="slick-prev dev-slick-prev"><i class="fa fa-angle-left"></i></button>',
            
            // Cấu hình cho máy tính bảng và điện thoại
            responsive: [
                {
                    breakpoint: 1024,
                    settings: {
                        slidesToShow: 2,
                    }
                },
                {
                    breakpoint: 600,
                    settings: {
                        slidesToShow: 1,
                    }
                }
            ]
        });
    }
}

// Gọi hàm khi trang web tải xong
$(document).ready(function(){
    

    // 1. Slider cho Men
    if ($('#men-slider').length > 0) {
        initProductSlider('#men-slider', '#men-arrows');
    }
    
    // 2. Slider cho Women
    if ($('#women-slider').length > 0) {
        initProductSlider('#women-slider', '#women-arrows');
    }
    
    // 3. Slider cho Kids
    if ($('#kids-slider').length > 0) {
        initProductSlider('#kids-slider', '#kids-arrows');
    }

    if ($('#flash-slider').length > 0) {
        initProductSlider('#flash-slider', '#flash-arrows');
    }


});

$(document).ready(function(){
    // 2. Chức năng Đồng hồ đếm ngược (Countdown)
    function startCountdown(durationInSeconds) {
        let timer = durationInSeconds, days, hours, minutes, seconds;
        
        setInterval(function () {
            // Tính toán thời gian
            days    = parseInt(timer / (3600 * 24), 10);
            hours   = parseInt((timer % (3600 * 24)) / 3600, 10);
            minutes = parseInt((timer % 3600) / 60, 10);
            seconds = parseInt(timer % 60, 10);

            // Thêm số 0 đằng trước nếu nhỏ hơn 10
            days    = days < 10 ? "0" + days : days;
            hours   = hours < 10 ? "0" + hours : hours;
            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;

            // Hiển thị ra HTML
            $('#days').text(days);
            $('#hours').text(hours);
            $('#minutes').text(minutes);
            $('#seconds').text(seconds);

            // Giảm thời gian, nếu hết thì reset lại (hoặc dừng)
            if (--timer < 0) {
                timer = durationInSeconds;
            }
        }, 1000);
    }
    if ($('#days').length > 0) {
        startCountdown(259200); 
    }
});

function loadWomenLatestPage(pageIndex) {
    if (pageIndex < 0) return; // Chặn nếu số trang < 0
    
    fetch('/women/latest-ajax?page=' + pageIndex)
        .then(response => response.text()) // Nhận về cục HTML
        .then(html => {
            // Đập bỏ nguyên cái khối div cũ, thay bằng khối div mới vừa lấy về
            document.getElementById('women-latest-section').outerHTML = html;
			document.getElementById('women-latest-section').scrollIntoView({ behavior: 'smooth' });
        })
        .catch(error => console.error('Lỗi khi tải trang Women Latest:', error));
}

function loadWomenCasualPage(pageIndex) {
    if (pageIndex < 0) return; 
    
    fetch('/women/casual-ajax?page=' + pageIndex)
        .then(response => response.text()) 
        .then(html => {
            document.getElementById('women-casual-section').outerHTML = html;
			document.getElementById('women-casual-section').scrollIntoView({ behavior: 'smooth' });
        })
        .catch(error => console.error('Lỗi khi tải trang Women Casual:', error));
}

function loadMenLatestPage(pageIndex) {
    if (pageIndex < 0) return; 
    
    fetch('/men/latest-ajax?page=' + pageIndex)
        .then(response => response.text()) 
        .then(html => { 
            document.getElementById('men-latest-section').outerHTML = html;
			document.getElementById('men-latest-section').scrollIntoView({ behavior: 'smooth' });
			})
        .catch(error => console.error('Lỗi khi tải trang Men Latest:', error));
}

function loadMenCasualPage(pageIndex) {
    if (pageIndex < 0) return; 
    
    fetch('/men/casual-ajax?page=' + pageIndex)
        .then(response => response.text()) 
        .then(html => { 
            document.getElementById('men-casual-section').outerHTML = html;
			document.getElementById('men-casual-section').scrollIntoView({ behavior: 'smooth' });
			})
        .catch(error => console.error('Lỗi khi tải trang Men Casual:', error));
}

function loadKidsLatestPage(pageIndex) {
    if (pageIndex < 0) return; 
    
    fetch('/kids/latest-ajax?page=' + pageIndex)
        .then(response => response.text()) 
        .then(html => { 
            document.getElementById('kids-latest-section').outerHTML = html;
			document.getElementById('kids-latest-section').scrollIntoView({ behavior: 'smooth' });
			})
        .catch(error => console.error('Lỗi khi tải trang Kids Latest:', error));
}

function loadKidsCasualPage(pageIndex) {
    if (pageIndex < 0) return; 
    
    fetch('/kids/casual-ajax?page=' + pageIndex)
        .then(response => response.text()) 
        .then(html => { 
            document.getElementById('kids-casual-section').outerHTML = html;
			document.getElementById('kids-casual-section').scrollIntoView({ behavior: 'smooth' });
			})
        .catch(error => console.error('Lỗi khi tải trang Kids Casual:', error));
}

document.addEventListener("DOMContentLoaded", function() {
    /* 1. XỬ LÝ GALLERY (Thumbnails và Arrows) */
    const mainImage = document.getElementById('mainImage');
    const thumbnails = document.querySelectorAll('.thumbnail-item');
    const prevBtn = document.getElementById('prevImage');
    const nextBtn = document.getElementById('nextImage');
    
    let currentImageIndex = 0;
    
    function updateMainImage(index) {
        currentImageIndex = index;
        mainImage.src = thumbnails[index].src;
        thumbnails.forEach(t => t.classList.remove('active'));
        thumbnails[index].classList.add('active');
    }
    
    if (prevBtn && nextBtn && thumbnails.length > 0) {
        prevBtn.addEventListener('click', () => {
            let newIndex = currentImageIndex - 1;
            if (newIndex < 0) newIndex = thumbnails.length - 1; 
            updateMainImage(newIndex);
        });
        
        nextBtn.addEventListener('click', () => {
            let newIndex = currentImageIndex + 1;
            if (newIndex >= thumbnails.length) newIndex = 0; 
            updateMainImage(newIndex);
        });
        
        thumbnails.forEach((thumb, index) => {
            thumb.addEventListener('click', () => updateMainImage(index));
        });
    }
    
    /* 2. XỬ LÝ CHỌN MÀU / SIZE */
    function handleSelection(selector) {
        document.querySelectorAll(selector).forEach(box => {
            box.addEventListener('click', function() {
                document.querySelectorAll(selector).forEach(b => b.classList.remove('active'));
                this.classList.add('active');
            });
        });
    }
    handleSelection('.color-box');
    handleSelection('.size-box');
    
    /* 3. XỬ LÝ SỐ LƯỢNG (Qty Selector) */
    const qtyMinus = document.getElementById('qtyMinus');
    const qtyPlus = document.getElementById('qtyPlus');
    const qtyInput = document.getElementById('qtyInput');
    
    if(qtyMinus && qtyPlus && qtyInput) {
        qtyMinus.addEventListener('click', () => {
            let val = parseInt(qtyInput.value);
            if (val > 1) qtyInput.value = val - 1;
        });
        qtyPlus.addEventListener('click', () => {
            let val = parseInt(qtyInput.value);
            qtyInput.value = val + 1;
        });
    }
});

function switchTab(element, tabId) {
    // 1. Gỡ viền đen của tất cả các chữ Tab
    document.querySelectorAll('.tab-item').forEach(el => el.classList.remove('active'));
    // Thêm viền đen vào Tab vừa bấm
    element.classList.add('active');

    // 2. Giấu tất cả các nội dung đi
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    // Chỉ hiện nội dung của Tab vừa bấm
    document.getElementById(tabId).classList.add('active');
}