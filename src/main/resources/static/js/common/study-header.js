document.addEventListener("DOMContentLoaded", function() {

    const path = window.location.pathname;

    const studyHomeMenu =
        document.querySelector("#studyHomeMenu");

    const studyNoticeMenu =
        document.querySelector("#studyNoticeMenu");

    const studyAttendanceMenu =
        document.querySelector("#studyAttendanceMenu");

    const studyMembersMenu =
        document.querySelector("#studyMembersMenu");

    // home
    if (path.includes("/home")) {

        studyHomeMenu.classList.add("active");

        return;
    }
    // notice
    if (path.includes("/notice")) {

        studyNoticeMenu.classList.add("active");

        return;
    }
    // attendance
    if (path.includes("/attendance")) {

        studyAttendanceMenu.classList.add("active");

        return;
    }
    // members
    if (path.includes("/members")) {

        studyMembersMenu.classList.add("active");

        return;
    }
});