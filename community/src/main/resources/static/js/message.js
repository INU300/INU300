$(document).ready(function() {


    $("#messageSendForm").submit(function(event) {
        event.preventDefault(); // 폼 제출을 중지

        // 폼 데이터를 직렬화하고 JavaScript 객체로 변환
        const formData = $(this).serializeArray().reduce(function(obj, item) {
            obj[item.name] = item.value;
            return obj;
        }, {});

        // AJAX 요청으로 데이터를 서버에 전송
        $.ajax({
            url: "/api/message",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(formData),
            success: function(response) {
                if (response.status === "CREATED") {
                    alert(response.data);
                    // 뒤로 갈 히스토리가 있으면 뒤로가기, 없으면 홈 화면으로 이동
                    document.referrer ? history.back() : window.location.replace("/home");
                }
            },
            error: function(error) {
                alert("오류 발생: [" + error.status + "] " + error.statusText);
            }
        });
    })
});