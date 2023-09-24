$(document).ready(function() {
  const isBuyer = getUser().role === 'BUYER';
  $('#bid-listing-nav').empty().append(isBuyer ? 'My Bids' : 'My Listings');
  $('#dashboard-link').
      attr('href', isBuyer ? '/web/buyer-dashboard' : '/web/seller-dashboard');
  $('#bid-listing-link').
      attr('href', isBuyer ? '/web/my-bids' : '/web/my-listings');
});