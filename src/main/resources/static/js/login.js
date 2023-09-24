$(document).ready(function () {
  $("#login-form").submit(function (e) {
    e.preventDefault();
    const email = $("#email").val();
    const password = $("#password").val();

    if (email && password) {
      post("auth/login", { email, password }, (user) => {
        console.log(user);
        saveUser(user);
        location.href = user.isFirstLogin ? "/web/user-details" : "/web/market";
      });
    }
  });
});
