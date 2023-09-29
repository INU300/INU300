$(document).ready(function() {
    $('#boardForm').on('submit', function(event) {
        event.preventDefault();
        const formData = {
            title: $("input[name='title']").val(),
            content: $("textarea[name='content']").val(),
            category: $("input[name='category']").val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/board',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(formData),
            dataType: 'json',
            success: function(response) {
                if (response.status === 200) {
                    alert('게시글이 저장되었습니다.');
                    window.location.href = '/';  // 게시글 목록 페이지로 리다이렉트
                } else {
                    alert('오류가 발생했습니다.');
                }
            }
        });
    });
});
