const backendBaseUrl = "http://localhost:8080/api/v1";

function saveUser(user) {
    localStorage.setItem('USER', JSON.stringify(user));
}

function getUser() {
    return JSON.parse(localStorage.getItem('USER'));
}


function post(path, data, responseCallback) {
    $.ajax({
        url: `${backendBaseUrl}/${path}`,
        type: 'POST',
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: responseCallback,
        error: handleApiError
    })
}

function get(path, responseCallback) {
    $.ajax({
        url: `${backendBaseUrl}/${path}`,
        type: 'GET',
        dataType: 'json',
        success: responseCallback,
        error: handleApiError
    })
}

function put(path, data, responseCallback) {
    $.ajax({
        url: `${backendBaseUrl}/${path}`,
        type: 'PUT',
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: responseCallback,
        error: handleApiError
    })
}

function patch(path, data, responseCallback) {
    $.ajax({
        url: `${backendBaseUrl}/${path}`,
        type: 'PATCH',
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: responseCallback,
        error: handleApiError
    })
}

function handleApiError(jqXHR, status, message) {
    console.log('Request failed: ', status)
    console.log('Error: ', message)
    const dangerMessage = document.getElementById("danger");
    dangerMessage.innerText = message;
}