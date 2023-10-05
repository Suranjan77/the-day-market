$(document).ready(function () {
  const auctionId = getUrlParameter("auctionId");
  get(`auctions/${auctionId}`, populateAuction, true);
  streamBids(auctionId);
});

let maxBid = {};
let bidPageSize = 8;
let auctionType = "";

function streamBids(auctionId) {
  get(`stream/auctions/${auctionId}/bids`, (streamBid) => {
    if (streamBid.status === "COMPLETED") {
      if (auctionType !== "Sealed") {
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

function populateAuction(auction) {
  $("#auction-type")
    .empty()
    .append(`<h4>${auction.type.toUpperCase()} Auction</h4>`);

  auctionType = auction.type;
  let categories = [auction.categoriesResponse.tag];
  let parent = auction.categoriesResponse.parent;
  while (parent) {
    categories.unshift(parent.tag);
    parent = parent.parent;
  }

  $("#category-line")
    .empty()
    .append(`<h5>${categories.join(" > ")}</h5>`);

  $("#auction-details")
    .empty()
    .append(
      ` ${
        auction.status === "SOLD" ? '<div class="sold-stamp">SOLD</div>' : ""
      } <div class="auction-description">
                        <h5>${auction.title}</h5>
                        <div class="product-details">
                            <img src="${getImageUrl(
                              auction.imageName
                            )}" alt="product image"/>
                            <p>
                                ${auction.description}
                            </p>
                        </div> </div>
                        <div class="bid-amount">
                          <div class="min-amount">
                              <h5>${
                                auctionType === "Dutch"
                                  ? "Reverse Timer"
                                  : "MIN BID AMOUNT"
                              }</h5>
<!--                              todo: timer for dutch auction-->
                              <h5>${
                                auctionType === "Dutch"
                                  ? "20:10:30"
                                  : auction.minAskPrice
                              } Points</h5>
                          </div>
                          <form action="" id="place-bid-form">
                              <input
                                      type="number"
                                      name="bidAmount"
                                      value = "${
                                        auctionType === "Dutch" ? "200" : ""
                                      }"
                                      placeholder = ${
                                        auctionType !== "Dutch"
                                          ? "Bid Amount"
                                          : "200 timer-points"
                                      }
                                      id="bidAmount"
                                      ${
                                        auctionType === "Dutch" ||
                                        auction.status === "SOLD"
                                          ? "disabled"
                                          : ""
                                      }
                              />
                              <button type="submit" ${
                                auction.status === "SOLD"
                                  ? 'class="disabled-button" disabled'
                                  : ""
                              }>Place a bid</button>
                          </form>
                      </div>`
    )
    .on("submit", "#place-bid-form", function (e) {
      e.preventDefault();
      const bidAmount = $("#bidAmount").val();
      const user = getUser();

      post(
        `auctions/${auction.id}/bids`,
        { amount: bidAmount, bidderId: user.id },
        (bid) => {
          refreshUserPoints(user);
        },
        true
      );
    });

  populateBids(1, auction.id);
}

function renderMaxBid() {
  if (maxBid && auctionType !== "Sealed") {
    $("#max-bid").empty().append(`
            ${getBidItemHTML(maxBid)}
    `);
  }
}

function populateBids(page, auctionId) {
  get(
    `auctions/${auctionId}/bids?page=${page}&size=${bidPageSize}`,
    (bids) => {
      if (auctionType !== "Sealed") {
        maxBid = bids.max;
      }

      renderMaxBid();

      $("#bid-list").empty().append(bids.data.map(getBidItemHTML).join(""));

      initializePagination(
        bids.totalSize,
        bids.pageSize,
        bids.currentPage,
        populateBids,
        [auctionId]
      );
    },
    true
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
    <p>${bid.amount ? bid.amount + " points" : "--"}</p>
  </div>
</div>`;
}
