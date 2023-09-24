$(document).ready(function() {
  $('#full-name').text(`${getUser().firstName} ${getUser().lastName}`);

  const isProfilePage = getUrlParameter('profile');
  if (isProfilePage) {
    prefill();
  }

  $('#userdetails-form').submit(function(e) {
    e.preventDefault();
    const role = $('#user-role').val();
    const postCode = $('#postcode').val();
    const town = $('#town').val();
    const streetName = $('#streetname').val();

    let data = {
      role,
      address: {
        addressLine1: streetName,
        city: town,
        postCode,
      },
    };

    if (isProfilePage) {
      data = {
        ...data,
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        password: $('#password').val(),
      };
    }

    const user = getUser();

    post(
        `users/${user.id}/details`,
        data,
        (usr) => {
          const user = getUser();
          saveUser({...user, role: usr.role});

          const uploadedFile = $('#photo');
          if (
              uploadedFile &&
              uploadedFile.length > 0 &&
              uploadedFile[0].files &&
              uploadedFile[0].files.length > 0
          ) {
            const file = uploadedFile[0].files[0];
            postFile(
                `users/${user.id}/image`,
                file,
                (usr) => {
                  saveUser({...user, profileImage: usr.profileImage});
                  location.href = '/web/market';
                },
                true,
            );
          } else {
            location.href = '/web/market';
          }
        },
        true,
    );
  });
});

function prefill() {

  $('#extra-fields').prepend(`<label for="firstName">First Name</label><br/>
                    <input
                            type="text"
                            name="firstName"
                            placeholder="Enter firstname"
                            id="firstName"
                    /><br/>
                    <label for="lastName">Last Name</label><br/>
                    <input
                            type="text"
                            name="lastName"
                            placeholder="Enter lastname"
                            id="lastName"
                    /><br/>
                    <label for="password">Change Password</label>
                    <input
                            type="password"
                            name="password"
                            placeholder="Enter password"
                            id="password"
                    /><br/>`);

  const user = getUser();
  $('#user-role').val(user.role).change();
  $('#postcode').val(user.address.postCode);
  $('#town').val(user.address.city);
  $('#streetname').val(user.address.addressLine1);
  $('#firstName').val(user.firstName);
  $('#lastName').val(user.lastName);

}
