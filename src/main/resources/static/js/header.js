$(document).ready(function() {
  const user = getUser();
  if (user) {
    refreshUserPoints(user);

    $('#header-username').empty().text(user.firstName);

    $('#edit-user').on('click', function() {
      location.href='/web/user-details?profile=true';
    });

    $('#logout').on('click', function() {
      removeUser();
      location.href = '/web';
    });

  }
});

function refreshUserPoints(user) {
  get(`users/${user.id}/points`,
      points => $('#user-points').empty().text(points.points), true);
}