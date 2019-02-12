button = document.getElementById('button');
email = document.getElementById('email');
password = document.getElementById('password');
var user = {};

email.onclick = function () {
    email.style.background = "#FFF";
}

button.onclick = function () {
    user.nickname = "dummy";
    user.email = email.value;
    user.password = password.value;

    if ((user.email == "") || (user.password == "")) {
        if (user.email == "") {
            email.style.background = "#ffa2a3";
        }
        if (user.password == "") {
            password.style.background = "#ffa2a3";
        }
        return;
    }

    var json = JSON.stringify(user);
    var request = new XMLHttpRequest();
    request.open("POST", "/");
    request.setRequestHeader('Content-type', 'application/json; charset=utf-8');

    request.addEventListener("load", function () {
        password.value = "";
        var response = JSON.parse(request.responseText);
        console.log(response);
        if (response.status == "WRONG_EMAIL") {
            email.placeholder = "Wrong email adress";
            email.style.background = "#ffa2a3";
            email.value = "";
            return;
        }
        if (response.status == "WRONG_PASSWORD") {
            password.placeholder = "Wrong password";
            password.style.background = "#ffa2a3";
            password.value = "";
            return;
        }
        if (response.status == "AUTHORIZED") {
            console.log("Authorized");
            document.location.href = '/chat'
        }

    });

    request.send(json);
}