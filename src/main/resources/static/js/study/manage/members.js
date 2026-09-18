document.addEventListener("DOMContentLoaded", init);


function init() {

    const kickButtons =
        document.querySelectorAll(".kick-button");

    kickButtons.forEach(function(button) {

        button.addEventListener(
            "click",
            kickMember
        );

    });

}


// 강퇴하기
async function kickMember(event) {

    const memberId =
        event.currentTarget.dataset.memberId;

    const studyId =
        getStudyId();

    const result =
        confirm("해당 스터디원을 강퇴시키겠습니까?");

    if (!result) {
        return;
    }

    try {

        const response = await fetch(
            `/study/${studyId}/manage/members/${memberId}/kick`,
            {
                method: "POST",
                headers: getCsrfHeaders()
            }
        );

        if (!response.ok) {
            throw new Error("강퇴 처리 실패");
        }

        const message = await response.text();

        alert(message);

        removeMember(memberId);

    } catch (error) {

        console.error(error);

        alert("스터디원 강퇴 처리 중 오류가 발생했습니다.");
    }

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


// studyId 가져오기
function getStudyId() {

    const managePage =
        document.querySelector(".manage-page");

    return managePage.dataset.studyId;
}


// 강퇴한 스터디원 화면에서 제거
function removeMember(memberId) {

    const memberItem =
        document.querySelector(
            `#member-${memberId}`
        );

    if (memberItem) {
        memberItem.remove();
    }
}