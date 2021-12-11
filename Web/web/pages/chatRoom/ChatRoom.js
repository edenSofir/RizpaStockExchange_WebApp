var chatVersion = 0;
var refreshRate = 2000;
var CHAT_LIST_URL = buildUrlWithContextPath("Chat");
var USER_NAME = buildUrlWithContextPath("userNameSpan");
var USER_MESSAGES_URL = buildUrlWithContextPath("usersMessages");

function appendToChatArea(entries) {

    $.each(entries || [], appendChatEntry);

    var scroller = $("#chatarea");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height }, "slow");
}

function appendChatEntry(index, entry){
    var entryElement = createChatEntry(entry);
    $("#chatarea").append(entryElement).append("<br>");
}

function createChatEntry (entry){
    entry.chatString = entry.chatString.replace (":)", "<img class='smiley-image' src='../../common/images/smiley.png'/>");
    return $("<span class=\"success\">").append(entry.username + "> " + entry.chatString);
}

function ajaxChatContent() {
    $.ajax({
        url: CHAT_LIST_URL,
        data: "chatversion=" + chatVersion,
        dataType: 'json',
        success: function(data) {
            console.log("Server chat version: " + data.version + ", Current chat version: " + chatVersion);
            if (data.version !== chatVersion) {
                chatVersion = data.version;
                appendToChatArea(data.entries);
            }
            triggerAjaxChatContent();
        },
        error: function(error) {
            triggerAjaxChatContent();
        }
    });
}

$(function() {
    $("#chatform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
            }
        });
        $("#userstring").val("");
        return false;
    });
});

function triggerAjaxChatContent() {
    setTimeout(ajaxChatContent, refreshRate);
}

function DisplayNameAccount(user) {
    var UserName = user.nameOfUser;
    var Role = user.Role;
    document.getElementById('userNameSpan').innerHTML = ' ' + UserName;
    document.getElementById('RoleSpan').innerHTML = Role
}

function ajaxUserName() {
    $.ajax({
        url: USER_NAME,
        success: function (user) {
            DisplayNameAccount(user);
        }
    });
}

function refreshUserMassages(messages) {
    $.each(messages || [], function (index, message) {
        alert(message);
    });
}

function ajaxUserMessages() {
    $.ajax({
        url: USER_MESSAGES_URL,
        success: function (messages) {
            refreshUserMassages(messages);
        }
    });
}

$(function() {
    triggerAjaxChatContent();
    ajaxUserName();
    ajaxUserMessages();
});