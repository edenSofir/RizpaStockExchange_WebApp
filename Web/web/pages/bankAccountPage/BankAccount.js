var HISTORY_BANK_ACCOUNT = buildUrlWithContextPath("HistoryTable");
var USER_NAME_ACCOUNT = buildUrlWithContextPath("userNameSpan");
var refreshRate = 2000;


function ajaxUserName() {
    $.ajax({
        url: USER_NAME_ACCOUNT,
        success: function (user) {
            DisplayNameAccount(user);
        }
    });
}

function ajaxHisList() {
    $.ajax({
        url: HISTORY_BANK_ACCOUNT,
        success: function (list) {
            DisplayHistoryTable(list);
        }
    });
}

function DisplayNameAccount(user) {
    var UserName = user.nameOfUser;
    var balance = user.bankAccount.balance.toString();
    document.getElementById('userNameSpan').innerHTML = UserName + ' ';
    document.getElementById('currAmountMoney').innerHTML = balance
}

function DisplayHistoryTable(listHis) {
    var HistoryTable = $('.HistoryTable tbody');
    HistoryTable.empty()
    $.each(listHis || [], function (index, history) {
        if(history.tradingType === 3)
            var tradingType = 'Load'
        if(history.tradingType === 2)
            var tradingType = 'Buy'
        if(history.tradingType === 1)
            var tradingType = 'Sell'
        var tr = $(document.createElement('tr'));
        var tdDate = $(document.createElement('td')).text(history.date);
        var tdAmount = $(document.createElement('td')).text(history.amount);
        var tdTradingType = $(document.createElement('td')).text(tradingType);
        var tdBalanceBeforeTrade = $(document.createElement('td')).text(history.balanceBeforeTrade);
        var tdBalanceAfterTrade = $(document.createElement('td')).text(history.balanceAfterTrade);
        var tdSymbol = $(document.createElement('td')).text(history.symbol);
        tdTradingType.appendTo(tr);
        tdDate.appendTo(tr);
        tdAmount.appendTo(tr);
        tdBalanceBeforeTrade.appendTo(tr);
        tdBalanceAfterTrade.appendTo(tr);
        tdSymbol.appendTo(tr);
        tr.appendTo(HistoryTable);
    });
}


$(function () {
    ajaxUserName();
    ajaxHisList();
    ajaxUserMessages();
    setInterval(ajaxUserName, refreshRate);
    setInterval(ajaxHisList, refreshRate);
    setInterval(ajaxUserMessages,refreshRate);
});

$(function () {
    $("#LoadMoney").submit(function () {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout:4000,
            error: function (e) {
                console.error("Failed to load money");
            },
            success: function (e) {
                alert(e);
                ajaxUserName();
                ajaxHisList();
                $("#LoadMoney")[0].reset();
            }
        });
       return false;
    });
    return false;
});

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



