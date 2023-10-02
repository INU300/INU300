$(document).ready(function () {
    console.log("Document is ready");
    console.log($("#classifyButton").length);

    function classifyContent(title, content, callback) {
        $.ajax({
            type: 'POST',
            url: '/api/externalClassify',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({ title: title, content: content }),
            success: function (response) {
                if (response && response.length > 0) {
                    callback(null, response);
                } else {
                    console.error("Server responded without a valid category:", response);
                    callback(new Error('Classification failed.'));
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error("Error when calling /api/externalClassify:", textStatus, errorThrown);
                callback(new Error('Classification failed.'));
            }
        });
    }

    $(document).on('click', '#classifyButton', function() {

        console.log("classifyButton clicked");

        const title = $("input[name='title']").val();
        const content = $("textarea[name='content']").val();

        classifyContent(title, content, function(err, category) {
            if (err) {
                alert('Classification failed.');
            } else {
                $("input[name='category']").val(category);
            }
        });
    });

    $('#boardForm').on('submit', function (event) {
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
            success: function (response) {
                if (response.status === 200) {
                    alert('게시글이 저장되었습니다.');
                    window.location.href = '/detail.html';
                } else {
                    alert('오류가 발생했습니다.');
                }
            }
        });
    });

    $(document).on('click', '#saveCommentButton', function() {
        const boardId = $('.post-container').data('id');
        const commentContent = $("#commentContent").val();

        $.ajax({
            type: 'POST',
            url: '/api/board/' + boardId + '/reply',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({
                boardId: boardId,
                content: commentContent
            }),
            success: function (response) {
                if (response.status === 200) {
                    // 댓글을 목록에 추가
                    $('#commentsList').append(
                        '<li>' + commentContent + ' <button class="deleteCommentButton" data-id="'
                        + response.data.replyId + '">삭제</button></li>'
                    );
                    $("#commentContent").val('');  // 댓글 입력란 초기화
                } else {
                    alert('오류가 발생했습니다.');
                }
            }
        });
    });

    $(document).on('click', '.deleteCommentButton', function() {
        const replyId = $(this).data('id');
        const $thisComment = $(this).parent();

        $.ajax({
            type: 'DELETE',
            url: '/api/board/reply/' + replyId,
            success: function (response) {
                if (response.status === 200) {
                    alert('댓글이 삭제되었습니다.');
                    $thisComment.remove();
                } else {
                    alert('오류가 발생했습니다.');
                }
            }
        });
    });

});
