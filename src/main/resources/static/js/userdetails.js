$(document).ready(function() {
  $('#full-name').text(`${getUser().firstName} ${getUser().lastName}`);

  $('#userdetails-form').submit(function(e) {
    e.preventDefault();
    const role = $('#user-role').val();
    const postCode = $('#postcode').val();
    const town = $('#town').val();
    const streetName = $('#streetname').val();

    const user = getUser();

    const data = {
      role,
      address: {
        addressLine1: streetName,
        city: town,
        postCode,
      },
    };

    post(`users/${user.id}/details`, data, usr => {
      const user = getUser();
      saveUser({...user, role: usr.role});

      const uploadedFile = $('#photo');
      if (uploadedFile && uploadedFile.length > 0 && uploadedFile[0].files &&
          uploadedFile[0].files.length > 0) {
        const file = uploadedFile[0].files[0];
        postFile(`users/${user.id}/image`, file,
            usr => saveUser({...user, profileImage: usr.profileImage}), true,
        );
      }
      location.href = '/web/market';
    }, true);
  });
});