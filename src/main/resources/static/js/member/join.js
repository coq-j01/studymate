// ==============================
// 요소 가져오기
// ==============================
const joinForm = document.getElementById("joinForm");

const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const passwordConfirmInput = document.getElementById("passwordConfirm");
const nicknameInput = document.getElementById("nickname");

const emailMessage = document.getElementById("emailMessage");
const passwordMessage = document.getElementById("passwordMessage");
const passwordConfirmMessage = document.getElementById("passwordConfirmMessage");
const nicknameMessage = document.getElementById("nicknameMessage");


// ==============================
// 검증 상태
// ==============================
let isEmailValid = false;
let isPasswordValid = false;
let isPasswordConfirmValid = false;
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
// 이메일 중복 확인
// ==============================
async function checkEmail() {

    const email = emailInput.value.trim();

    if (email === "") {
        emailMessage.textContent = "";
        isEmailValid = false;
        return;
    }

    // TODO
    // 서버에 이메일 중복 확인 요청
    // Controller → Service → Mapper

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

    // TODO
    // 서버에 닉네임 중복 확인 요청

}


// ==============================
// 비밀번호 조건 확인
// ==============================
function checkPassword() {

    const password = passwordInput.value;

    if (password === "") {
        passwordMessage.textContent = "";
        isPasswordValid = false;
        return;
    }

    // TODO
    // 비밀번호 규칙 결정 후 검사
    // 예: 8자 이상, 영문 + 숫자

}


// ==============================
// 비밀번호 일치 확인
// ==============================
function checkPasswordConfirm() {

    const password = passwordInput.value;
    const passwordConfirm = passwordConfirmInput.value;

    if (passwordConfirm === "") {
        passwordConfirmMessage.textContent = "";
        isPasswordConfirmValid = false;
        return;
    }

    if (password === passwordConfirm) {

        showMessage(
            passwordConfirmMessage,
            "비밀번호가 일치합니다.",
            true
        );

        isPasswordConfirmValid = true;

    } else {

        showMessage(
            passwordConfirmMessage,
            "비밀번호가 일치하지 않습니다.",
            false
        );

        isPasswordConfirmValid = false;
    }
}


// ==============================
// 이벤트
// ==============================

// 이메일 입력 완료 후 중복 확인
emailInput.addEventListener("blur", checkEmail);

// 닉네임 입력 완료 후 중복 확인
nicknameInput.addEventListener("blur", checkNickname);

// 비밀번호 입력 시 조건 확인
passwordInput.addEventListener("input", function () {

    checkPassword();

    // 비밀번호 확인에 값이 있다면
    // 원래 비밀번호가 변경될 때 다시 비교
    if (passwordConfirmInput.value !== "") {
        checkPasswordConfirm();
    }
});

// 비밀번호 확인 입력 시 바로 비교
passwordConfirmInput.addEventListener(
    "input",
    checkPasswordConfirm
);


// ==============================
// 회원가입 제출
// ==============================
joinForm.addEventListener("submit", function (event) {

    if (
        !isEmailValid ||
        !isPasswordValid ||
        !isPasswordConfirmValid ||
        !isNicknameValid
    ) {

        event.preventDefault();

        alert("입력한 정보를 다시 확인해 주세요.");
    }
});