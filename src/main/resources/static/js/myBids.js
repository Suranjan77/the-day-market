$(document).ready(function() {
  populateMyBids(1);
});

function populateMyBids(page) {
  get(`buyers/${getUser().id}/my-bids?page=${page}&size=6`, myBids => {

    let bids = myBids.data.map(bid => `
        <div class="individual-bids">
        <input id="bid-id" type="hidden" value="${bid.id}"/>
        <div class="image-result">
            ${
        bid.status === 'WON' || bid.status === 'LIVE' ?
            `<p> <i class = "fa fa-circle"></i> ${bid.status === 'WON' ?
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
           
            ${bid.status === 'WON' ? getRatingStars(bid.stars, bid.id) : ''}
        </div>
    </div>
  `);

    $('#my-bids').
        empty().
        append(bids).
        ready(
            () => addStarEventListener( 'AUCTION'));

    initializePagination(
        myBids.totalSize,
        myBids.pageSize,
        myBids.currentPage,
        populateMyBids,
    );
  }, true);
}