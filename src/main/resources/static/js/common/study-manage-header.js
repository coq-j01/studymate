document.addEventListener("DOMContentLoaded", function() {

    const path = window.location.pathname;

    const requestMenu =
        document.querySelector("#requestMenu");

    const memberMenu =
        document.querySelector("#memberMenu");

    const settingMenu =
        document.querySelector("#settingMenu");

    // 신청서
    if (path.includes("/manage/requests")) {

        requestMenu.classList.add("active");

        return;
    }
    // 스터디원
    if (path.includes("/manage/members")) {

        memberMenu.classList.add("active");

        return;
    }
    // 세팅
    if (path.includes("/manage/settings")) {

        settingMenu.classList.add("active");

        return;
    }
});