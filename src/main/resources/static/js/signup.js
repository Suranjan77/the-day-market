$(document).ready(function() {
  $('#register-form').submit(function(e) {
    e.preventDefault();
    const firstName = $('#firstName').val();
    const lastName = $('#lastName').val();
    const email = $('#email').val();
    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();
    const agreeTerms = $('#terms-acceptance').is(':checked');
    const dangerMessage = document.getElementById('danger');

    if (password !== confirmPassword) {
      dangerMessage.innerText = 'password and confirm password does not match.';
    } else if (agreeTerms) {
      const data = {
        firstName, lastName, email, password,
      };

      post('auth/register', data, user => {
        console.log(user);
        location.href = '/web/login';
      });
    } else {
      dangerMessage.innerText = 'Read and agree the terms and conditions.';
    }
  });
});