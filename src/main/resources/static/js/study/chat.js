const chatPage = document.getElementById("chatPage");
const studyId = chatPage.dataset.studyId;

const chatMessages = document.getElementById("chatMessages");
const chatInput = document.getElementById("chatInput");
const sendButton = document.getElementById("sendButton");

const socket = new SockJS("/ws-study");
const stompClient = Stomp.over(socket);

stompClient.connect({}, function () {

    console.log("WebSocket 연결 성공");

    stompClient.subscribe(`/topic/study/${studyId}`, function (response) {

        const message = JSON.parse(response.body);

        renderMessage(message);
    });
});


function sendMessage() {

    const text = chatInput.value.trim();

    if (!text) {
        return;
    }

    stompClient.send(
        `/app/study/${studyId}/chat`,
        {},
        JSON.stringify({
            message: text
        })
    );

    chatInput.value = "";
}


function renderMessage(message) {

    const messageDiv = document.createElement("div");

    messageDiv.classList.add("chat-message", "other");

    messageDiv.innerHTML = `
        <span class="chat-nickname">${message.nickname}</span>
        <div class="chat-bubble">
            ${message.message}
        </div>
    `;

    chatMessages.appendChild(messageDiv);

    chatMessages.scrollTop = chatMessages.scrollHeight;
}


sendButton.addEventListener("click", sendMessage);

chatInput.addEventListener("keydown", function (event) {

    if (event.key === "Enter") {
        sendMessage();
    }
});