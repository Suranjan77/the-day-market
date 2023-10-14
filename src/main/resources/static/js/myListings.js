$(document).ready(function() {
  // const marketStatus = get("")
  // $("#market-status").

  $('#add-listing').on('click', function() {
    location.href = '/web/list-item';
  });

  populateAuctions(1);
});

function getControlButtons(auction) {
  if (auction.status === 'DRAFT' || auction.status === 'SCHEDULED') {
    return `<div id="delete-publish-button"> 
      <span class="material-symbols-outlined" id="delete-auction">delete</span> 
      <span class="material-symbols-outlined" id="publish-auction">publish</span>  
      <span class="material-symbols-outlined" id="edit-auction">edit</span>  
    </div>`;
  }
  return '';
}

function populateAuctions(page) {
  get(
      `sellers/${getUser().id}/auctions?includeDrafts=true&page=${page}&size=6`,
      (auctionPage) => {
        $('#match-count').text(auctionPage.totalSize);
        let auctions = auctionPage.data.map(
            (auction) =>
                `<div class="individual-items" data-auction-id="${auction.id}">
 ${auction.status === 'SOLD' ? '<div class="sold-stamp">SOLD</div>' : ''}
 ${auction.status === 'DRAFT' ? '<div class="draft-stamp">DRAFT</div>' : ''}
<div class="image">
            <img src="${getImageUrl(auction.imageName)}" alt="thumbnail"/>
       </div>
       <div class="details">
            <h5>${auction.title}</h5>
            <p id="auction-type">${auction.type} Auction</p>
            <p id="bid-amount">
                <img src="/images/bid.png" alt="auction"/>
                <span>${
                    auction.minAskPrice ? auction.minAskPrice : '-'
                } Points</span>
                <br>
            </p>
            ${getControlButtons(auction)}
       </div> 
      </div>`).join('');

        $('#auctions').
            html(auctions).
            on('click', '.individual-items', function() {
              const auctionId = $(this).data('auction-id');
              // open auction details update page
            }).
            on('click', '#delete-auction', function() {
              // delete auction
            }).
            on('click', '#publish-auction', function() {
              // publish auction
            }.on('click', '#edit-auction', function() {
              // edit auction
            }));

        initializePagination(
            auctionPage.totalSize,
            auctionPage.pageSize,
            auctionPage.currentPage,
            populateAuctions,
        );
      },
      true,
  );
}
