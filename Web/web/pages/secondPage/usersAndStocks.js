var USER_LIST_URL = buildUrlWithContextPath("usersList");
var STOCKS_LIST_URL = buildUrlWithContextPath("StockTable");
var USER_NAME = buildUrlWithContextPath("userNameSpan");
var USER_MESSAGES_URL = buildUrlWithContextPath("usersMessages");
var refreshRate = 2000

function ajaxUserName() {
    $.ajax({
        url: USER_NAME,
        success: function (user) {
            DisplayNameAccount(user);
        }
    });
}

function refreshUsersList(users) {
    var usersList = $('.usersList tbody');
    usersList.empty();
    $.each(users, function (key, element) {
        console.log('key: ' + key + '\n' + 'value: ' + element.Role);
        var tr = $(document.createElement('tr'));
        var td = $(document.createElement('td')).text(key + ' ' + element.Role);
        td.appendTo(tr);
        tr.appendTo(usersList);
    });
}

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function (users) {
            refreshUsersList(users);
        }
    });
}

function refreshStocksTable(stocks) {
    var stocksTable = $('.StockTable tbody');
    stocksTable.empty();

    $.each(stocks, function (key, element) {
        console.log('value:' + element.compnyName + 'key: ' + key + '\n' + 'value: ' + element.stockPrice + 'value:' + element.transactionsCycle);
        var tr = $(document.createElement('tr'));
        var tdCompanyName = $(document.createElement('td')).text(element.CompanyName);
        var tdSymbolStock = $(document.createElement('td')).text(key);
        var tdCurrentRate = $(document.createElement('td')).text(element.stockPrice);
        var tdTradingCycle = $(document.createElement('td')).text(element.TransactionsCycle);

        var inputItem1 = $(document.createElement('input'));
        inputItem1.attr('value' , 'view details');
        inputItem1.attr('type' , 'Submit');
        inputItem1.attr('name', 'stockName');

        var inputItem2 = $(document.createElement('input'));
        inputItem2.attr('name', 'singleStock');
        inputItem2.attr('value', element.CompanyName);
        inputItem2.attr('type' , 'hidden');

        var dataStockButton = $(document.createElement('form')).append(inputItem1);
        dataStockButton.append(inputItem2);
        dataStockButton.attr('id', element.CompanyName);
        dataStockButton.attr('action', 'loadDataPage')
        dataStockButton.attr('method', 'POST');

        var tdDataStock = $(document.createElement('td')).append(dataStockButton);

        tdCompanyName.appendTo(tr);
        tdSymbolStock.appendTo(tr);
        tdCurrentRate.appendTo(tr);
        tdTradingCycle.appendTo(tr);
        tdDataStock.appendTo(tr);
        tr.appendTo(stocksTable);
    });
}

function ajaxStocksTable() {
    $.ajax({
        url: STOCKS_LIST_URL,
        success: function (stocks) {
            refreshStocksTable(stocks);
        }
    });
}

function DisplayNameAccount(user) {
    var UserName = user.nameOfUser;
    var Role = user.Role;
    document.getElementById('userNameSpan').innerHTML = ' ' + UserName;
    document.getElementById('RoleSpan').innerHTML = Role
}

function ClickLoad() {
    var file = this[0].files[0];
    if (file !== undefined) {
        var formData = new FormData();
        formData.append("fake-key-1", file);
        $.ajax({
            method: 'POST',
            data: formData,
            url: this.action,
            processData: false,
            contentType: false,
            timeout: 4000,
            error: function (e) {
                console.error("Failed to submit");
                alert(e.responseText);
            },
            success: function (e) {
                ajaxStocksTable();
                ajaxUsersList();
                alert(e);
            }
        });
    } else {
        alert("Please choose a file to load first!");
    }
    return false;
}

$(function () {
    $("#CreateNewCompany").submit(function () {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout:4000,
            error: function (e) {
                console.error("Failed to load company");
            },
            success: function (e) {
                alert(e.toString());
                ajaxStocksTable();
                $("#CreateNewCompany")[0].reset();
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

$(function () {
    ajaxUsersList();
    ajaxStocksTable();
    ajaxUserName();
    ajaxUserMessages();
    $("#uploadForm").submit(ClickLoad);
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxStocksTable, refreshRate);
    setInterval(ajaxUserName,refreshRate);
    setInterval(ajaxUserMessages,refreshRate);
});






