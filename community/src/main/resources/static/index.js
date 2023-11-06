var main = {
    init: function () {
        var _this = this;
        $('#btn-join').on('click', function () {
            _this.join();
        });
    },
    join: function () {
        var selectedCheckboxes = $('.category-checkbox:checked');
        if (selectedCheckboxes.length !== 5) {
            alert('5개의 카테고리를 선택해야 합니다.');
            return false; // 폼 제출을 중지
        }
        var selectedCategories = $('input[name="categories"]:checked').map(function () {
            return $(this).val();
        }).get();

        var data = {
            email: $('#email').val(),
            password: $('#password').val(),
            name: $('#name').val(),
            nickname: $('#nickname').val(),
            school: $('#school').val(),
            department: $('#department').val(),
            categories: selectedCategories
        };

        $.ajax({
            type: 'POST',
            url: '/api/join',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('회원이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
main.init();