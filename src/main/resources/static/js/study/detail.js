document.addEventListener("DOMContentLoaded", init);


function init() {

    const applyMessage =
        document.querySelector("#applyMessage");

    const applyMessageLength =
        document.querySelector("#applyMessageLength");

    if (applyMessage && applyMessageLength) {

        applyMessage.addEventListener(
            "input",
            function () {
                updateApplyMessageLength(
                    applyMessage,
                    applyMessageLength
                );
            }
        );
    }
}


function updateApplyMessageLength(
    applyMessage,
    applyMessageLength
) {
    applyMessageLength.textContent = applyMessage.value.length;
}