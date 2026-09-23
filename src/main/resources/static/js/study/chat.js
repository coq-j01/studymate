const chatPage = document.getElementById("chatPage");
const studyId = chatPage.dataset.studyId;
const memberId = Number(chatPage.dataset.memberId);

const chatMessages = document.getElementById("chatMessages");
const chatInput = document.getElementById("chatInput");
const sendButton = document.getElementById("sendButton");

const socket = new SockJS("/ws-study");
const stompClient = Stomp.over(socket);

let lastDate = null;

// DB에서 가져온 기존 채팅 먼저 출력
initialChatList.forEach(function(message) {
    renderMessage(message);
});

// 기존 채팅 중 오늘 날짜가 없으면 오늘 구분선 추가
const today = formatDate(new Date());

if (lastDate !== today) {
    renderDateDivider(today);
    lastDate = today;
}

stompClient.connect({}, function() {

    console.log("WebSocket 연결 성공");

    stompClient.subscribe(`/topic/study/${studyId}`, function(response) {

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
        JSON.stringify({ //json으로 객체 변환
            message: text
        })
    );

    chatInput.value = "";
}


function renderMessage(message) {
	const currentDate = formatDate(message.sendAt);
	if (lastDate !== currentDate) {
	        renderDateDivider(currentDate);
	        lastDate = currentDate;
	    }

    const messageDiv = document.createElement("div");
    const time = formatTime(message.sendAt);

    if (memberId === message.memberId) {
        messageDiv.classList.add("chat-message", "mine");
        messageDiv.innerHTML = `
				<div class="chat-message-row">
					<div class="chat-bubble">
						 ${message.message}
					</div>
					<span class="chat-time">${time}</span>
				</div>
		    `;
    } else {
        messageDiv.classList.add("chat-message", "other");
        messageDiv.innerHTML = `
		        <span class="chat-nickname">${message.nickname}</span>
				<div class="chat-message-row">
				     <div class="chat-bubble">
				          ${message.message}
				     </div>
				     <span class="chat-time">${time}</span>
				</div>
		    `;
    }


    chatMessages.appendChild(messageDiv);

    chatMessages.scrollTop = chatMessages.scrollHeight;
}


sendButton.addEventListener("click", sendMessage);

chatInput.addEventListener("keydown", function(event) {

    if (event.key === "Enter") {
        sendMessage();
    }
});

function formatDate(sendAt) {

    const date = new Date(sendAt);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
}

function renderDateDivider(date) {

    const divider = document.createElement("div");

    divider.classList.add("chat-date-divider");

    divider.innerHTML = `
        <span>${date}</span>
    `;

    chatMessages.appendChild(divider);
}

function formatTime(sendAt) {

    const date = new Date(sendAt);

    const hour = String(date.getHours()).padStart(2, "0");
    const minute = String(date.getMinutes()).padStart(2, "0");

    return `${hour}:${minute}`;
}