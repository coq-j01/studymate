document.addEventListener("DOMContentLoaded", function() {

    const path = window.location.pathname;

    const studyFindMenu =
        document.querySelector("#studyFindMenu");

    const myStudyMenu =
        document.querySelector("#myStudyMenu");


    // 내 스터디 페이지
    if (path.startsWith("/study/my-study")) {

        myStudyMenu.classList.add("active");

        return;
    }
    // 스터디  페이지
    if (path.startsWith("/study/main")) {

        studyFindMenu.classList.add("active");

        return;
    }
});