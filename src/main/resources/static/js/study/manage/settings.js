document.addEventListener("DOMContentLoaded", init);


function init() {
    const endStudyButton =
        document.querySelector("#end-study-button");

    const deleteStudyButton =
        document.querySelector("#delete-study-button");

    endStudyButton.addEventListener("click", endStudyHandler);
    deleteStudyButton.addEventListener("click", deleteStudyHandler);
}

//스터디 종료
async function endStudyHandler() {
    const studyId = getStudyId();

    const result =
        confirm("스터디를 종료하시겠습니까?");


    if (!result) {
        return;
    }

    try {

        const response = await fetch(
            `/study/${studyId}/manage/settings/end`,
            {
                method: "POST",
                headers: getCsrfHeaders()
            }
        );

        if (!response.ok) {
            throw new Error("스터디 종료에 실패했습니다.");
        }

        alert("스터디가 종료되었습니다.");
        window.location.href = response.url;

    } catch (error) {

        alert(error.message);

    }
}


//스터디 삭제
async function deleteStudyHandler() {
    const studyId = getStudyId();

    const result =
        confirm("스터디를 삭제하시겠습니까?");


    if (!result) {
        return;
    }

    try {

        const response = await fetch(
            `/study/${studyId}/manage/settings/delete`,
            {
                method: "POST",
                headers: getCsrfHeaders()
            }
        );

        const message = await response.text();

        if (!response.ok) {
            throw new Error(message);
        }

        alert(message);

        window.location.href = "/study/main";

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