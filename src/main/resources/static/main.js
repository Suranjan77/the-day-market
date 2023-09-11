const backendBaseUrl = 'http://localhost:8080/api/v1';

setInterval(refreshToken, 5 * 60 * 1000); // 10 seconds before expiry

function saveUser(user) {
  localStorage.setItem('USER',
      JSON.stringify({...user, createdAt: Math.floor(Date.now() / 1000)}));
  const expiresInSeconds = user.expiry;
  console.log(`Token valid for ${expiresInSeconds} seconds`);
}

function getUser() {
  return JSON.parse(localStorage.getItem('USER'));
}

function postFile(path, file, responseCallback, shouldAuthenticate) {
  let formData = new FormData();
  formData.append('file', file);
  $.ajax({
    url: `${backendBaseUrl}/${path}`,
    type: 'POST',
    ...shouldAuthenticate &&
    {headers: {'Authorization': `Bearer ${getAuthToken()}`}},
    data: formData,
    contentType: false,
    processData: false,
    success: responseCallback,
    error: handleApiError,
  });
}

function post(path, data, responseCallback, shouldAuthenticate) {
  $.ajax({
    url: `${backendBaseUrl}/${path}`,
    type: 'POST',
    ...shouldAuthenticate &&
    {headers: {'Authorization': `Bearer ${getAuthToken()}`}},
    data: JSON.stringify(data),
    dataType: 'json',
    contentType: 'application/json;charset=UTF-8',
    success: responseCallback,
    error: handleApiError,
  });
}

function get(path, responseCallback, shouldAuthenticate) {
  $.ajax({
    url: `${backendBaseUrl}/${path}`,
    type: 'GET',
    ...shouldAuthenticate &&
    {headers: {'Authorization': `Bearer ${getAuthToken()}`}},
    dataType: 'json',
    success: responseCallback,
    error: handleApiError,
  });
}

function put(path, data, responseCallback, shouldAuthenticate) {
  $.ajax({
    url: `${backendBaseUrl}/${path}`,
    type: 'PUT',
    ...shouldAuthenticate &&
    {headers: {'Authorization': `Bearer ${getAuthToken()}`}},
    data: JSON.stringify(data),
    dataType: 'json',
    contentType: 'application/json;charset=UTF-8',
    success: responseCallback,
    error: handleApiError,
  });
}

function patch(path, data, responseCallback, shouldAuthenticate) {
  $.ajax({
    url: `${backendBaseUrl}/${path}`,
    type: 'PATCH',
    ...shouldAuthenticate &&
    {headers: {'Authorization': `Bearer ${getAuthToken()}`}},
    data: JSON.stringify(data),
    dataType: 'json',
    contentType: 'application/json;charset=UTF-8',
    success: responseCallback,
    error: handleApiError,
  });
}

function getAuthToken() {
  const user = getUser();
  if (user && user.accessToken) {
    return user.accessToken;
  }
  location.href = '/web/login';
}

function refreshToken() {
  const user = getUser();
  if (user && user.accessToken) {
    const isExpired = Math.floor(Date.now() / 1000) >=
        (user.createdAt + user.expiry - 20);
    if (true) {
      console.log('Token expired, refreshing...');
      $.ajax({
        url: `${backendBaseUrl}/auth/refresh-token`,
        type: 'POST',
        headers: {
          'Authorization': `Bearer ${user.accessToken}`,
        },
        success: user => saveUser(user),
        error: handleApiError,
      });
    }
  }
}

function handleApiError(jqXHR, status, message) {
  console.log('Request failed: ', status);
  console.log('Error: ', jqXHR);
  const dangerMessage = document.getElementById('danger');
  if (dangerMessage) {
    dangerMessage.innerText = message;
  }
}

function getImageUrl(imageName) {
  return `${backendBaseUrl}/image?fileName=${imageName}`;
}

function getUrlParameter(name) {
  name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
  const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
  const results = regex.exec(location.search);
  return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};