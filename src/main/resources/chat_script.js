var logoutButton = document.getElementById('logoutButton');
var sendButton = document.getElementById('sendButton');
var messagesArea = document.getElementById("messages");
var messageBox = document.getElementById("message");
var onlineUsers = document.getElementById("onlineUsers");
var userNickname;
var onlineUsersList;
var lastid=0;


var newMessage = function (text, type) {
    date = new Date();
    return {
        text: text,
        nickname: userNickname,
        date: date.getDay() + "." + date.getMonth() + "." + date.getFullYear() + " " +
            date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds(),
        type: type,
    }
}

var sendMessage = function (message) {
    json = JSON.stringify(message);
    request = new XMLHttpRequest();
    request.open("POST", "/post_message");
    request.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    request.addEventListener("load", function () {
        lastid = JSON.parse(request.response);
    });
    request.send(json);
}

var addMessage = function (message) {
    nick = message.nickname;
    time = message.date.substr(message.date.indexOf(" "));
    text = message.text;
    type = message.type;
    messageBlock = document.createElement("div");
    messageBlock.classList.add("message");
    metaBlock = document.createElement("div");
    metaBlock.classList.add("meta");
    nickElem = document.createElement("div");
    nickElem.classList.add("nick");
    nickElem.innerHTML = nick;
    timeElem = document.createElement("div");
    timeElem.classList.add("time");
    timeElem.innerHTML = time;
    textBlock = document.createElement("div");
    textElem = document.createElement("p");
    switch(type){
        case "MESSAGE":textElem.innerHTML = text;break;
        case "USER_LOGIN":textElem.innerHTML ="join to our chat";textElem.classList.add("user_login");getOnlineUsers();break;
        case "USER_LOGOUT":textElem.innerHTML ="leave to our chat"+nick+"!";textElem.classList.add("user_logout");getOnlineUsers();break
    }
    textElem.classList.add("text");
    messageBlock.appendChild(metaBlock);
    messageBlock.appendChild(textBlock);
    metaBlock.appendChild(nickElem);
    metaBlock.appendChild(timeElem);
    textBlock.appendChild(textElem);
    messagesArea.appendChild(messageBlock);
    messagesArea.scrollTop = messagesArea.scrollHeight;
}

var getOnlineUsers = function () {
    usersRequest = new XMLHttpRequest();
    usersRequest.open("GET", "/online_users");
    usersRequest.addEventListener("load", function () {
        while (onlineUsers.firstChild) {
            onlineUsers.removeChild(onlineUsers.firstChild);
        }
        onlineUsersList = JSON.parse(usersRequest.responseText);
        userNickname = onlineUsersList[0];
        for (i = 1; i < onlineUsersList.length; i++) {
            div = document.createElement("div");
            div.classList.add("nickname");
            div.innerHTML = onlineUsersList[i];
            onlineUsers.appendChild(div);
        }
    });
    usersRequest.send();
}

var getLastMessages = function(){
    request = new XMLHttpRequest();
    request.open("POST", "/get_messages");
    request.addEventListener("load", function () {
        messages = JSON.parse(request.response);
        lastid+=messages.length;
        for(i = 0; i <messages.length;i++){
            addMessage(messages[i]);
        }
    });
    request.send(lastid);
}

logoutButton.onclick = function () {
    document.cookie = 'userID=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    document.location.href = '/';
}

sendButton.onclick = function () {
    text = messageBox.value;
    if (text.length == 0) {
        return;
    }
    message = newMessage(text, "MESSAGE");
    addMessage(message);
    sendMessage(message);
    messageBox.value = "";
}

getOnlineUsers();
getLastMessages();
setInterval(getLastMessages, 1000);




