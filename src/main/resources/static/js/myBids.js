$(document).ready(function() {
  populateMyBids(1);
});

function showUnsoldBidButtons() {
  return `<div class="checkout">
            <section class="confirm-btn" id="purchase-btn">
                <h3><span>Buy <br/>Auction</span></h3>
            </section>
            <section class="reject-btn" id="reject-btn">
                <h3><span>Reject Auction</span></h3>
            </section>
        </div>`;
}

function populateMyBids(page) {
  get(`buyers/${getUser().id}/my-bids?page=${page}&size=6`, myBids => {

    let bids = myBids.data.map(bid => `
        <div class="individual-bids">
        <input id="bid-id" type="hidden" value="${bid.id}"/>
        <div class="image-result">
            ${
        bid.status === 'WON' || bid.status === 'LIVE' || bid.amIWinner ?
            `<p> <i class = "fa fa-circle"></i> ${bid.status === 'WON' || bid.amIWinner ?
                'You Won' :
                'Live'} </p>` : ''
    }
            <img src="${getImageUrl(bid.image)}" alt="selected-product"/>
        </div>
        
        <div class="items-details">
            <h4>${bid.title}</h4>
            <p>${bid.description}</p>
        </div>
        
        <div class="user-bids">
            <div class="your-bids">
                <h5>Your Bid</h5>
                <span> ${bid.bidAmount} </span>
            </div>

            <div class="your-bids">
                <h5>Winning Bid</h5>
                <span> ${bid.status === 'LIVE' ? '---' : bid.winningBidAmount} </span>
            </div>
           
            ${bid.status === 'WON' || bid.amIWinner ? getRatingStars(bid.stars, bid.id) : ''}
        </div>
        ${bid.amIWinner && bid.status !== 'CLOSED' ? showUnsoldBidButtons() : ''} 
    </div>
  `);

    $('#my-bids').
        empty().
        append(bids).
        on('click', '#purchase-btn', function() {
          const bidId = $('#bid-id').val();
          const userId = getUser().id;
          const data = {
            bidId, isPurchase: true,
          };
          post(`buyers/${userId}/my-bid-action`, data, d => {
            console.log(d);
            location.reload();
          }, true);
        }).
        on('click', '#reject-btn', function() {
          const bidId = $('#bid-id').val();
          const userId = getUser().id;
          const data = {
            bidId, isPurchase: false,
          };
          post(`buyers/${userId}/my-bid-action`, data, d => {
            console.log(d);
            location.reload();
          }, true);
        }).
        ready(
            () => addStarEventListener('BID'));

    initializePagination(
        myBids.totalSize,
        myBids.pageSize,
        myBids.currentPage,
        populateMyBids,
    );
  }, true);
}