$(document).ready(function() {
  const auctionId = getUrlParameter('auctionId');
  get(`auctions/${auctionId}`, populateAuction, true);
  streamBids(auctionId);
});

let maxBid = {};
let bidPageSize = 8;
let auctionType = '';

function streamBids(auctionId) {
  get(`stream/auctions/${auctionId}/bids`, (streamBid) => {
    if (streamBid.status === 'COMPLETED') {
      if (auctionType !== 'Sealed') {
        if (!maxBid) {
          maxBid = streamBid.data;
        }
        if (Number(maxBid.amount) < Number(streamBid.data.amount)) {
          maxBid = streamBid.data;
        }
      }
      populateBids(1, auctionId);
    }
    streamBids(auctionId);
  });
}

let currentTimer;
let auctionStateIntervalId;

function updateBasedOnAuctionState(auctionState, isFirstTime) {
  if (isFirstTime) {
    currentTimer = auctionState.timerSeconds;
  } else {
    if (Number(auctionState.timerSeconds) < Number(currentTimer)) {
      currentTimer = auctionState.timerSeconds;
    } else {
      currentTimer = currentTimer - 1;
    }
  }
  setDutchTimer(currentTimer);
  if (auctionState.expired) {
    clearInterval(auctionStateIntervalId)
    $('#dutch-timer').empty().text('EXPIRED').attr('style', 'color: red');
    $('#bidAmount').val(auctionState.currentPoints);
    $('#place-bid-btn').
        attr('class', 'disabled-button').
        prop('disabled', true);
  } else {
    $('#bidAmount').val(auctionState.currentPoints);
  }
}

function populateAuction(auction) {
  $('#auction-type').
      empty().
      append(`<h4>${auction.type.toUpperCase()} Auction</h4>`);

  auctionType = auction.type;
  let categories = [auction.categoriesResponse.tag];
  let parent = auction.categoriesResponse.parent;
  while (parent) {
    categories.unshift(parent.tag);
    parent = parent.parent;
  }

  $('#category-line').empty().append(`<h5>${categories.join(' > ')}</h5>`);

  $('#auction-details').empty().append(
      ` ${
          auction.status === 'SOLD' ? '<div class="sold-stamp">SOLD</div>' : ''
      } <div class="auction-description">
                        <h5>${auction.title}</h5>
                        <div class="product-details">
                            <img src="${getImageUrl(
          auction.imageName,
      )}" alt="product image"/>
                            <p>
                                ${auction.description}
                            </p>
                        </div> </div>
                        <div class="bid-amount">
                          <div class="min-amount">
                              <h5>${
          auctionType === 'Dutch'
              ? 'Reverse Timer'
              : 'MIN BID AMOUNT'
      }</h5>
                              <h5><span id="dutch-timer">${
          auctionType === 'Dutch'
              ? '00:00:00'
              : auction.minAskPrice
      }</span></h5>
                          </div>
                          <form action="" id="place-bid-form">
                              <input
                                      type="number"
                                      name="bidAmount"
                                      value = ""
                                      placeholder = ""
                                      id="bidAmount"
                                      ${
          auctionType === 'Dutch' ||
          auction.status === 'SOLD'
              ? 'disabled'
              : ''
      }
                              />
                              <button type="submit" ${
          auction.status === 'SOLD'
              ? 'class="disabled-button" disabled'
              : ''
      } id="place-bid-btn">Place a bid</button>
                          </form>
                      </div>`,
  ).on('submit', '#place-bid-form', function(e) {
    e.preventDefault();
    const bidAmount = $('#bidAmount').val();
    const user = getUser();

    post(
        `auctions/${auction.id}/bids`,
        {amount: bidAmount, bidderId: user.id},
        (bid) => {
          refreshUserPoints(user);
        },
        true,
    );
  }).ready(function() {
    if (auctionType === 'Dutch') { //handle dutch auction timer
      get(`auctions/${auction.id}/state`,
          state => updateBasedOnAuctionState(state, true), true);
      auctionStateIntervalId = setInterval(
          () => get(`auctions/${auction.id}/state`,
              state => updateBasedOnAuctionState(state, false), true),
          1000,
      );
    }
  });

  populateBids(1, auction.id);
}

function setDutchTimer(timer) {
  if(Number(timer) <= 0) {
    timer = 0;
  }
  let hours = Math.floor(timer / 3600);
  let minutes = Math.floor((timer % 3600) / 60);
  let seconds = timer % 3600 % 60;
  let formatted = `${String(hours).padStart(2, '0')}:${String(minutes).
      padStart(2, '0')}:${String(
      seconds).padStart(2, '0')}`;

  $('#dutch-timer').empty().text(formatted);
}

function renderMaxBid() {
  if (maxBid && auctionType !== 'Sealed') {
    $('#max-bid').empty().append(`
            ${getBidItemHTML(maxBid)}
    `);
  }
}

function populateBids(page, auctionId) {
  get(
      `auctions/${auctionId}/bids?page=${page}&size=${bidPageSize}`,
      (bids) => {
        if (auctionType !== 'Sealed') {
          maxBid = bids.max;
        }

        renderMaxBid();
        if (bids.data) {
          $('#bid-list').empty().append(bids.data.map(getBidItemHTML).join(''));
        }

        initializePagination(
            bids.totalSize,
            bids.pageSize,
            bids.currentPage,
            populateBids,
            [auctionId],
        );
      },
      true,
  );
}

function getBidItemHTML(bid) {
  return `<div class="list">
          <div class="list-username">
    <p>${maskEmail(bid.user.email)}</p>
  </div>
  <div class="list-bid-time">
    <p>${extractTimeFromInstant(bid.createdAt)}</p>
  </div>
  <div class="list-bid-count">
    <p>${bid.amount ? bid.amount + ' points' : '--'}</p>
  </div>
</div>`;
}
