button = document.getElementById('button');
nickname = document.getElementById('nickname');
email = document.getElementById('email');
password = document.getElementById('password');
var user={};


nickname.onclick = function(){
    nickname.style.background = "#FFF";
}

email.onclick = function(){
    email.style.background = "#FFF";
}

password.onclick = function(){
    password.style.background = "#FFF";
}

button.onclick = function(){

     user.nickname = nickname.value;
     user.email = email.value;
     user.password = password.value;
    if ((user.nickname == "") || (user.email == "") || (user.password == "")) {
        if (user.nickname == "") {
            nickname.style.background = "#ffa2a3";
        }
        if (user.email == "") {
            email.style.background = "#ffa2a3";
        }
        if (user.password == "") {
            password.style.background = "#ffa2a3";
        }
        return;
    }

    if(user.nickname.length>15){
        nickname.style.background = "#ffa2a3";
        nickname.placeholder = "Nickname is too long";
        nickname.value = "";
        return;
    }

     var json = JSON.stringify(user);
     var request = new XMLHttpRequest();
     request.open("POST", "/register");
     request.setRequestHeader('Content-type', 'application/json; charset=utf-8');

     request.addEventListener("load", function() {
         password.value = "";
         var response = JSON.parse(request.responseText);
         if(response.status=="NICK_IS_ALREADY_IN_USE"){
             nickname.placeholder = "Nick is already used";
             nickname.style.background = "#ffa2a3";
             nickname.value = "";
             return;
         }
         if(response.status=="EMAIL_IS_ALREADY_IN_USE"){
            email.placeholder ="Email is already used";
            email.style.background = "#ffa2a3";
            email.value = "";
            return;
         }
         if(response.status=="AUTHORIZED"){
             console.log("Authorized");
             document.location.href='/'
         }

       });

     request.send(json);
 }