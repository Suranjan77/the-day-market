$(document).ready(function () {
    $('#full-name').text(`${getUser().firstName} ${getUser().lastName}`);
});