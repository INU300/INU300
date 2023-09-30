$(document).ready(function () {
    console.log("Document is ready");
    console.log($("#classifyButton").length);  // 1이 출력되어야 합니다.

    // Function to classify the content using external API
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

        console.log("classifyButton clicked");  // 이 부분 추가

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
                    window.location.href = '/';
                } else {
                    alert('오류가 발생했습니다.');
                }
            }
        });
    });
});
