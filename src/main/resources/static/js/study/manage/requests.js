document.addEventListener("DOMContentLoaded", init);


function init() {

    const approveButtons =
        document.querySelectorAll(".approve-button");

    const rejectButtons =
        document.querySelectorAll(".reject-button");


    approveButtons.forEach(function(button) {

        button.addEventListener(
            "click",
            approveJoinRequest
        );

    });


    rejectButtons.forEach(function(button) {

        button.addEventListener(
            "click",
            rejectJoinRequest
        );

    });

}


// 가입 승인
async function approveJoinRequest(event) {

    const memberId =
        event.currentTarget.dataset.memberId;

    const studyId =
        getStudyId();


    try {

        const response = await fetch(
            `/study/${studyId}/manage/requests/${memberId}/approve`,
            {
                method: "POST",
                headers: getCsrfHeaders()
            }
        );


        const message =
            await response.text();


        if (!response.ok) {
            throw new Error(message);
        }


        alert(message);

        removeRequest(memberId);


    } catch (error) {

        alert(error.message);

    }

}


// 가입 거절
async function rejectJoinRequest(event) {

    const memberId =
        event.currentTarget.dataset.memberId;

    const studyId =
        getStudyId();


    const result =
        confirm("가입 신청을 거절하시겠습니까?");


    if (!result) {
        return;
    }


    try {

        const response = await fetch(
            `/study/${studyId}/manage/requests/${memberId}/reject`,
            {
                method: "POST",
                headers: getCsrfHeaders()
            }
        );


        const message =
            await response.text();


        if (!response.ok) {
            throw new Error(message);
        }


        alert(message);

        removeRequest(memberId);


    } catch (error) {

        alert(error.message);

    }

}


// studyId 가져오기
function getStudyId() {

    const managePage =
        document.querySelector(".manage-page");

    return managePage.dataset.studyId;

}


// CSRF 헤더
function getCsrfHeaders() {

    const csrfToken =
        document.querySelector(
            'meta[name="_csrf"]'
        ).content;

    const csrfHeader =
        document.querySelector(
            'meta[name="_csrf_header"]'
        ).content;


    return {
        [csrfHeader]: csrfToken
    };

}


// 처리 완료된 신청자 화면에서 제거
function removeRequest(memberId) {

    const requestItem =
        document.querySelector(
            `#request-${memberId}`
        );


    if (requestItem) {
        requestItem.remove();
    }

}