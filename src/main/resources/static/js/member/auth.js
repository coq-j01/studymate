//비밀번호 토글 설정
const passwordToggles = document.querySelectorAll(".password-toggle");

passwordToggles.forEach(toggle => {

    toggle.addEventListener("click", function() {

        const targetId = this.dataset.target;
        const passwordInput = document.getElementById(targetId);
        const icon = this.querySelector("i");

        if (passwordInput.type === "password") {

            passwordInput.type = "text";

            icon.classList.remove("bi-eye");
            icon.classList.add("bi-eye-slash");

            this.setAttribute("aria-label", "비밀번호 숨기기");

        } else {

            passwordInput.type = "password";

            icon.classList.remove("bi-eye-slash");
            icon.classList.add("bi-eye");

            this.setAttribute("aria-label", "비밀번호 보기");
        }

    });

});