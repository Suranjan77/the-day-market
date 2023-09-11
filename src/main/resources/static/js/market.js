$(document).ready(function() {
  // const marketStatus = get("")
  // $("#market-status").

  populateAuctions(1);

  $('#auctions').on('click', '.individual-items', function() {
    const auctionId = $(this).data('auction-id');
    location.href = `/web/auction?auctionId=${auctionId}`;
  });
});

function populateAuctions(page) {
  get(`auctions/today?page=${page}&size=6`, auctionPage => {
    $('#match-count').text(auctionPage.totalSize);
    let auctions = auctionPage.data.map(auction =>
        `<div class="individual-items" data-auction-id="${auction.id}">
           ${auction.status === 'SOLD' ?
            '<div class="sold-stamp">SOLD</div>' :
            ''}
          <div class="image">
            <img src="${getImageUrl(auction.imageName)}" alt="thumbnail"/>
       </div>
       <div class="details">
            <h5>${auction.title}</h5>
            <p id="auction-type">${auction.type} Auction</p>
            <p id="bid-amount">
                <img src="/images/bid.png" alt="auction"/>
                <span>${auction.minAskPrice ? auction.minAskPrice : '-'} Points</span>
            </p>
       </div> </div>`,
    ).join('');

    $('#auctions').html(auctions);

    $('#pagination-nav').pagination({
      dataSource: Array(auctionPage.totalSize).
          fill().
          map((_, index) => index + 1),
      totalNumber: auctionPage.totalSize,
      pageSize: auctionPage.pageSize,
      pageNumber: Number(auctionPage.currentPage) + 1,
      autoHidePrevious: true,
      autoHideNext: true,
      afterPageOnClick: function(event, page) {
        populateAuctions(page);
      },
      afterPreviousOnClick: function(event, page) {
        populateAuctions(page);
      },
      afterNextOnClick: function(event, page) {
        populateAuctions(page);
      },
    });
  }, true);
}