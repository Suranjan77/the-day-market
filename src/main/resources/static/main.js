const backendBaseUrl = `http://${window.location.host}/api/v1`;

setInterval(refreshToken, 5 * 60 * 1000); // 10 seconds before expiry

function saveUser(user) {
  localStorage.setItem('USER',
      JSON.stringify({...user, createdAt: Math.floor(Date.now() / 1000)}));
  const expiresInSeconds = user.expiry;
  console.log(`Token valid for ${expiresInSeconds} seconds`);
}

function removeUser() {
  return localStorage.removeItem('USER');
}

function getUser() {
  const user = localStorage.getItem('USER');
  if (user) {
    return JSON.parse(user);
  } else {
    location.href = '/web/login';
  }
}

function postFile(path, file, responseCallback, shouldAuthenticate) {
  let formData = new FormData();
  formData.append('file', file);
  $.ajax({
    url: `${backendBaseUrl}/${path}`,
    type: 'POST', ...(shouldAuthenticate && {
      headers: {Authorization: `Bearer ${getAuthToken()}`},
    }),
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
    type: 'POST', ...(shouldAuthenticate && {
      headers: {Authorization: `Bearer ${getAuthToken()}`},
    }),
    data: JSON.stringify(data),
    dataType: 'json',
    contentType: 'application/json;charset=UTF-8',
    success: responseCallback,
    error: handleApiError,
  });
}

function get(path, responseCallback, shouldAuthenticate) {
  $.ajax({
    url: `${backendBaseUrl}/${path}`, type: 'GET', ...(shouldAuthenticate && {
      headers: {Authorization: `Bearer ${getAuthToken()}`},
    }), dataType: 'json', success: responseCallback, error: handleApiError,
  });
}

function put(path, data, responseCallback, shouldAuthenticate) {
  $.ajax({
    url: `${backendBaseUrl}/${path}`,
    type: 'PUT', ...(shouldAuthenticate && {
      headers: {Authorization: `Bearer ${getAuthToken()}`},
    }),
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
    type: 'PATCH', ...(shouldAuthenticate && {
      headers: {Authorization: `Bearer ${getAuthToken()}`},
    }),
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
    const isExpired = Math.floor(Date.now() / 1000) >= user.createdAt +
        user.expiry - 20;
    if (true) {
      console.log('Token expired, refreshing...');
      $.ajax({
        url: `${backendBaseUrl}/auth/refresh-token`, type: 'POST', headers: {
          Authorization: `Bearer ${user.accessToken}`,
        }, success: (user) => saveUser(user), error: handleApiError,
      });
    }
  }
}

function handleApiError(jqXHR, status, message) {
  console.log('Request failed: ', jqXHR.status, status);
  console.log('Error: ', jqXHR);
  if (jqXHR.status === 401) {
    location.href = '/web/login';
  } else {
    alert(jqXHR.responseJSON.error);
  }
}

function getImageUrl(imageName) {
  return `${backendBaseUrl}/image?fileName=${imageName}`;
}

function getUrlParameter(name) {
  name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
  const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
  const results = regex.exec(location.search);
  return results === null ?
      '' :
      decodeURIComponent(results[1].replace(/\+/g, ' '));
}

function maskEmail(email) {
  const localPart = email.split('@')[0];
  const firstChar = localPart[0];
  const lastChar = localPart[localPart.length - 1];
  return `${firstChar}***${lastChar}`;
}

function extractTimeFromInstant(instantString) {
  const date = new Date(instantString);
  const hours = String(date.getUTCHours()).padStart(2, '0');
  const minutes = String(date.getUTCMinutes()).padStart(2, '0');
  const seconds = String(date.getUTCSeconds()).padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
}

function formatDate(date) {
  const year = date.toLocaleString('default', {year: 'numeric'});
  const month = date.toLocaleString('default', {month: '2-digit'});
  const day = date.toLocaleString('default', {day: '2-digit'});
  return `${year}-${month}-${day}`;
}

function formatTime(date) {
  const hours = String(date.getUTCHours()).padStart(2, '0');
  const minutes = String(date.getUTCMinutes()).padStart(2, '0');
  const seconds = String(date.getUTCSeconds()).padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
}

function initializePagination(totalSize, pageSize, currentPage,
                              populateFunction, defaultParams) {
  $('#pagination-nav').pagination({
    dataSource: Array(totalSize).fill().map((_, index) => index + 1),
    totalNumber: totalSize,
    pageSize: pageSize,
    pageNumber: Number(currentPage) + 1,
    autoHidePrevious: true,
    autoHideNext: true,
    afterPageOnClick: function(event, page) {
      populateFunction(page, ...(defaultParams || []));
    },
    afterPreviousOnClick: function(event, page) {
      populateFunction(page, ...(defaultParams || []));
    },
    afterNextOnClick: function(event, page) {
      populateFunction(page, ...(defaultParams || []));
    },
  });
}

function getReputationIcon(reputation) {
  switch (reputation) {
    case 'HIGH':
      return '/images/smiling-face.png';
    case 'LOW':
      return '/images/sad.png';
    case 'NEUTRAL':
    default:
      return '/images/neutral.png';
  }
}

function getRatingStars(starCount, id) {
  let openStars = '';
  let closedStars = '';

  const totalStars = Math.min(5, starCount);

  for (let i = 0; i < totalStars; i++) {
    closedStars += '<i class="fa fa-star"></i> ';
  }

  for (let j = 5 - totalStars; j > 0; j--) {
    openStars += '<i class="fa fa-star-o"></i>';
  }

  return `
            <p id="${id}" class="stars" data-receiver-id="${id}">
               ${closedStars}
               ${openStars}
            </p>
        `;
}

function addStarEventListener(receiverType) {
  $('.stars').each(function() {
    let $stars = $(this).find('i');
    let receiverId = $(this).data("receiver-id");

    $stars.each(function(index1, star) {
      $(star).on('click', function() {
        $stars.each(function(index2, innerStar) {
          if (index1 >= index2) {
            $(innerStar).attr('class', 'fa fa-star');
          } else {
            $(innerStar).attr('class', 'fa fa-star-o');
          }
        });

        const activeCount = $(this).siblings('.fa-star').length + 1;

        const ratingData = {
          'raterId': getUser().id,
          'receiverId': receiverId,
          'stars': activeCount,
          'type': receiverType,
        };

        post('ratings', ratingData, data => {
        }, true);
      });
    });
  });
}
