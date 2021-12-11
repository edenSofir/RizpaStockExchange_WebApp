function calculateContextPath() {
    var pathWithoutLeadingSlash = window.location.pathname.substring(1);
    var contextPathEndIndex = pathWithoutLeadingSlash.indexOf('/');
    return pathWithoutLeadingSlash.substr(0, contextPathEndIndex)
}
function wrapBuildingURLWithContextPath() {
    var contextPath = calculateContextPath();
    return function (resource) {
        return "/" + contextPath + "/" + resource;
    };
}

var buildUrlWithContextPath = wrapBuildingURLWithContextPath();