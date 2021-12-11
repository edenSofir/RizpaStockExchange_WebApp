var HEADING_STOCK_DATA_URL = buildUrlWithContextPath("stockHeadingName");
var STOCK_TABLE_DATA_URL = buildUrlWithContextPath("dataTab");
var TRANSACTIONS_STOCK_DATA_URL = buildUrlWithContextPath("transactionsTab");
var HOLDINGS_USER_IN_STOCK_URL = buildUrlWithContextPath("userHoldings");
var SELL_TABLES_URL = buildUrlWithContextPath("sellTable");
var BUY_TABLE_URL = buildUrlWithContextPath("buyTable");
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

function DisplayNameAccount(user) {
    var UserName = user.nameOfUser;
    var Role = user.Role;
    document.getElementById('userNameSpan').innerHTML = ' ' + UserName;
    document.getElementById('RoleSpan').innerHTML = Role
}

function refreshStockHeading(stockCompanyName) {
    //console.log(stockCompanyName);

    document.getElementById('stockHeadingName').textContent += stockCompanyName;
}

function refreshStockTableData(stock) {

    var dataOfStock = $('.dataTab tbody');
    dataOfStock.empty();
    //console.log( stock , stock.CompanyName);
    var tr = $(document.createElement('tr'));
    var tdSymbol = $(document.createElement('td')).text(stock.Symbol);
    var tdComoanyName = $(document.createElement('td')).text(stock.CompanyName);
    var tdRate = $(document.createElement('td')).text(stock.stockPrice);
    var tdTradeCycle = $(document.createElement('td')).text(stock.TransactionsCycle);

    tdSymbol.appendTo(tr);
    tdComoanyName.appendTo(tr);
    tdRate.appendTo(tr);
    tdTradeCycle.appendTo(tr);
    tr.appendTo(dataOfStock);
}

function refreshStockTransactionsTable(transactions) {
    var transactionsOfStock = $('.transactionsTab tbody');
    transactionsOfStock.empty();

    $.each(transactions, function (index, Transaction) {
        var tr = $(document.createElement('tr'));
        var tdTransactionDate = $(document.createElement('td')).text(Transaction.transactionDate);
        var tdTransactionAmount = $(document.createElement('td')).text(Transaction.amount);
        var tdTransactionRate = $(document.createElement('td')).text(Transaction.price);

        tdTransactionDate.appendTo(tr);
        tdTransactionAmount.appendTo(tr);
        tdTransactionRate.appendTo(tr);
        tr.appendTo(transactionsOfStock);
    });
}

function refreshHoldingsUserStock(item) {
    //console.log(item);
    //console.log(item.quantity)
    var quantity = item.quantity.toString();
    $(".holdings").empty();
    document.getElementById("userHoldings").innerHTML = quantity;
    //document.getElementById("holdings").innerText = item.quantity ;
}

function refreshSellTablesStock(sellCommandsList) {
    console.log(sellCommandsList);

    var sellTableStock = $('.sellTable tbody');
    sellTableStock.empty();

    $.each(sellCommandsList, function (index, Command) {
        var sellCycle = (Command.price) * (Command.stockAmount);
        var trSell = $(document.createElement('tr'));
        var tdSellDate = $(document.createElement('td')).text(Command.commandDate);
        var tdSellAmount = $(document.createElement('td')).text(Command.stockAmount);
        var tdSellPrice = $(document.createElement('td')).text(Command.price);
        var tdSellCycle = $(document.createElement('td')).text(sellCycle);
        var tdSellInitiator = $(document.createElement('td')).text(Command.InitiatorName);

        tdSellDate.appendTo(trSell);
        tdSellAmount.appendTo(trSell);
        tdSellPrice.appendTo(trSell);
        tdSellCycle.appendTo(trSell);
        tdSellInitiator.appendTo(trSell);
        trSell.appendTo(sellTableStock);
    });
}

function refreshBuyTablesStock(buyCommandsList) {
    console.log(buyCommandsList);
    var buyTableStock = $('.buyTable tbody');
    buyTableStock.empty();

    $.each(buyCommandsList, function (index, Command) {
        var buyCycle = (Command.price) * (Command.stockAmount);
        var trBuy = $(document.createElement('tr'));
        var tdBuyDate = $(document.createElement('td')).text(Command.commandDate);
        var tdBuyAmount = $(document.createElement('td')).text(Command.stockAmount);
        var tdBuyPrice = $(document.createElement('td')).text(Command.price);
        var tdBuyCycle = $(document.createElement('td')).text(buyCycle);
        var tdBuyInitiator = $(document.createElement('td')).text(Command.InitiatorName);

        tdBuyDate.appendTo(trBuy);
        tdBuyAmount.appendTo(trBuy);
        tdBuyPrice.appendTo(trBuy);
        tdBuyCycle.appendTo(trBuy);
        tdBuyInitiator.appendTo(trBuy);
        trBuy.appendTo(buyTableStock);
    });
}

function ajaxStockHeading() {
    $.ajax({
        method: 'GET',
        url: HEADING_STOCK_DATA_URL,
        success: function (stockCompanyName) {
            refreshStockHeading(stockCompanyName);
        }
    });
}

function ajaxStockTableData() {
    $.ajax({
        method: 'GET',
        url: STOCK_TABLE_DATA_URL,
        success: function (stock) {
            refreshStockTableData(stock);
        }
    });
}

function ajaxStockTransactionsTable() {
    $.ajax({
        method: 'GET',
        url: TRANSACTIONS_STOCK_DATA_URL,
        success: function (transactions) {
            refreshStockTransactionsTable(transactions);
        }
    });
}

function ajaxHoldingsUserStock() {
    $.ajax({
        method: 'GET',
        url: HOLDINGS_USER_IN_STOCK_URL,
        success: function (item) {
            refreshHoldingsUserStock(item);
        }
    });
}

function ajaxSellTableStock() {
    $.ajax({
        method: 'GET',
        url: SELL_TABLES_URL,
        success: function (sellCommandsList) {
            refreshSellTablesStock(sellCommandsList);
        }
    });
}

function ajaxBuyTableStock() {
    $.ajax({
        method: 'GET',
        url: BUY_TABLE_URL,
        success: function (buyCommandsList) {
            refreshBuyTablesStock(buyCommandsList);
        }
    });
}

$(function () {
    $('input[name="tradingTypeChoose"]').click(function () {
        var $radio = $(this);
        // if this was previously checked
        if ($radio.data('waschecked') === true) {
            $radio.prop('checked', false);
            $radio.data('waschecked', false);
        } else
            $radio.data('waschecked', true);
        // remove was checked from other radios
        $radio.siblings('input[name="tradingTypeChoose"]').data('waschecked', false);
    });
});
$(function () {
    $('input[name="commandTypeChoose"]').click(function () {
        var $radio = $(this);
        // if this was previously checked
        if ($radio.data('waschecked') === true) {
            $radio.prop('checked', false);
            $radio.data('waschecked', false);
        } else
            $radio.data('waschecked', true);
        // remove was checked from other radios
        $radio.siblings('input[name="commandTypeChoose"]').data('waschecked', false);
    });
});

$(function () {
    $("#CreateNewCommand").submit(function () {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 4000,
            error: function (e) {
                console.error("Failed to create new command");
            },
            success: function (e) {
                //alert(e.toString());
                document.getElementById('messageToUser').textContent = e.toString();
                //ajaxStocksTable();
                $("#CreateNewCommand")[0].reset();
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
    ajaxStockHeading();
    ajaxStockTableData();
    ajaxStockTransactionsTable();
    ajaxHoldingsUserStock();
    ajaxSellTableStock();
    ajaxBuyTableStock();
    ajaxUserName();
    ajaxUserMessages();
    setInterval(ajaxStockTableData, refreshRate);
    setInterval(ajaxStockTransactionsTable, refreshRate);
    setInterval(ajaxHoldingsUserStock, refreshRate);
    setInterval(ajaxSellTableStock, refreshRate);
    setInterval(ajaxBuyTableStock, refreshRate);
    setInterval(ajaxUserName, refreshRate);
    setInterval(ajaxUserMessages, refreshRate);
});