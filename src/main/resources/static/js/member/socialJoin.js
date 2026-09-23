// ==============================
// 요소 가져오기
// ==============================

const socialJoinForm = document.getElementById("socialJoinForm");
const nicknameInput = document.getElementById("nickname");
const nicknameMessage = document.getElementById("nicknameMessage");

// 닉네임 검증 상태
let isNicknameValid = false;


// ==============================
// 메시지 출력 함수
// ==============================

function showMessage(element, message, isSuccess) {

    element.textContent = message;
    element.classList.remove("success", "error");

    if (isSuccess) {
        element.classList.add("success");
    } else {
        element.classList.add("error");
    }
}


// ==============================
// 닉네임 중복 확인
// ==============================

async function checkNickname() {

    const nickname = nicknameInput.value.trim();

    if (nickname === "") {
        nicknameMessage.textContent = "";
        isNicknameValid = false;
        return;
    }

    const response = await fetch(
        `/check/nickname?nickname=${encodeURIComponent(nickname)}`
    );

    const isDuplicate = await response.json();

    if (isDuplicate) {

        showMessage(
            nicknameMessage,
            "이미 사용 중인 닉네임입니다.",
            false
        );

    } else {

        showMessage(
            nicknameMessage,
            "사용 가능한 닉네임입니다.",
            true
        );
    }

    isNicknameValid = !isDuplicate;
}


// ==============================
// 이벤트
// ==============================

nicknameInput.addEventListener("blur", checkNickname);


// ==============================
// 가입 제출
// ==============================

socialJoinForm.addEventListener("submit", function (event) {

    if (!isNicknameValid) {

        event.preventDefault();

        alert("닉네임 중복 확인이 필요합니다.");
    }
});