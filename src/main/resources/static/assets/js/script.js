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

/* XỬ LÝ TRANG CHI TIẾT SẢN PHẨM (Single Product) */

// Giả lập Cơ sở dữ liệu (Database) sản phẩm
// thông tin các sản phẩm  như bên trang men.html 
const products = [
    {
        id: 1,
        name: "Classic Spring White",
        price: 120.00,
        image: "images/men-01.png",
        description: "Đây là chiếc áo sơ mi trắng cổ điển, phong cách lịch lãm dành cho quý ông."
    },
    {
        id: 2,
        name: "Classic Spring Jacket",
        price: 90.00,
        image: "images/men-02.png",
        description: "Áo khoác nhẹ mùa xuân, thoáng mát và thời trang."
    },
    {
        id: 3,
        name: "Classic Spring Red",
        price: 150.00,
        image: "images/men-03.png",
        description: "Màu đỏ nổi bật, chất liệu cotton 100% thấm hút mồ hôi."
    },
   
    {
        id: 4, 
        name: "Men's Special Edition",
        price: 200.00,
        image: "images/men-04.png", 
        description: "Phiên bản đặc biệt giới hạn."
    },
    //women
    {
        id: 5,
        name: "Classic Spring White",
        price: 120.00,
        image: "images/women-01.png",
        description: "Đây là chiếc áo sơ mi trắng cổ điển, phong cách lịch lãm dành cho quý ba."
    },
    {
        id: 6,
        name: "Classic Spring Jacket",
        price: 90.00,
        image: "images/women-02.png",
        description: "Áo khoác nhẹ mùa xuân, thoáng mát và thời trang."
    },
    {
        id: 7,
        name: "Classic Spring Red",
        price: 150.00,
        image: "images/women-03.png",
        description: "Màu đỏ nổi bật, chất liệu cotton 100% thấm hút mồ hôi."
    },
   
    {
        id: 8, 
        name: "Men's Special Edition",
        price: 200.00,
        image: "images/women-04.png", 
        description: "Phiên bản đặc biệt giới hạn."
    },
    //kids
    {
        id: 9,
        name: "Classic Spring White",
        price: 120.00,
        image: "images/kids-01.png",
        description: "Đây là chiếc áo sơ mi trắng cổ điển."
    },
    {
        id: 10,
        name: "Classic Spring Jacket",
        price: 90.00,
        image: "images/kids-02.png",
        description: "Áo khoác nhẹ mùa xuân, thoáng mát và thời trang."
    },
    {
        id: 11,
        name: "Classic Spring Red",
        price: 150.00,
        image: "images/kids-03.png",
        description: "Màu đỏ nổi bật, chất liệu cotton 100% thấm hút mồ hôi."
    },
   
    {
        id: 12, 
        name: "Men's Special Edition",
        price: 200.00,
        image: "images/kids-04.png", 
        description: "Phiên bản đặc biệt giới hạn."
    }

];

// Hàm lấy tham số ID từ URL (Ví dụ: ?id=1)
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

//  Hàm hiển thị dữ liệu lên màn hình
function loadProductDetail() {
    // Chỉ chạy code này nếu đang ở trang single-product.html (kiểm tra xem có thẻ product-name không)
    const nameElement = document.getElementById('product-name');
    
    if (nameElement) {
        // Lấy ID từ trên thanh địa chỉ
        const productId = getQueryParam('id');
        
        // Tìm sản phẩm trong mảng 'products' có id trùng với id trên url
        // Lưu ý: productId lấy từ URL là chuỗi, cần so sánh lỏng (==) hoặc ép kiểu
        const product = products.find(p => p.id == productId);

        if (product) {
            // Nếu tìm thấy -> Gán dữ liệu vào HTML
            document.getElementById('product-name').innerText = product.name;
            document.getElementById('product-price').innerText = '$' + product.price;
            document.getElementById('product-desc').innerText = product.description;
            document.getElementById('product-img').src = product.image;
        } else {
            // Nếu không tìm thấy (ví dụ id=999)
            document.getElementById('product-name').innerText = "Sản phẩm không tồn tại";
            document.getElementById('product-img').style.display = 'none';
        }
    }
}

// Gọi hàm khi trang tải xong
$(document).ready(function() {
    loadProductDetail();
});