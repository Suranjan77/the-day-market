$(document).ready(function() {
  const auctionId = getUrlParameter('auctionId');
  console.log(`This is auction id: ${auctionId} page`);

  get(`auctions/${auctionId}`, populateAuction(), true);
});

function populateAuction(auction) {

}