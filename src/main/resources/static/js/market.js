$(document).ready(function () {
  // const marketStatus = get("")
  // $("#market-status").

  populateAuctions(1);

  $("#auctions").on("click", ".individual-items", function () {
    const auctionId = $(this).data("auction-id");
    location.href = `/web/auction?auctionId=${auctionId}`;
  });

  get(
    "categories",
    (categoryList) => {
      const categories = categoryList
        .map(
          (category) => `<div class="individual-list">
                            <input
                                    type="checkbox"
                                    name="${replaceSpaceByUnderScore(
                                      category.tag
                                    )}"
                                    value="${replaceSpaceByUnderScore(
                                      category.tag
                                    )}"
                                    id="${category.id}"
                            /><label for="${category.id}">${
            category.tag
          }</label>
                        </div>`
        )
        .join(" ");

      $("#categories-list").append(categories);
    },
    true
  );
});

function replaceSpaceByUnderScore(value) {
  return value.replace(/ /g, "_");
}

function populateAuctions(page) {
  get(
    `auctions/today?page=${page}&size=6`,
    (auctionPage) => {
      $("#match-count").text(auctionPage.totalSize);
      let auctions = auctionPage.data
        .map(
          (auction) =>
            `<div class="individual-items" data-auction-id="${auction.id}">
           ${
             auction.status === "SOLD"
               ? '<div class="sold-stamp">SOLD</div>'
               : ""
           }
          <div class="image">
            <img src="${getImageUrl(auction.imageName)}" alt="thumbnail"/>
       </div>
       <div class="details">
            <h5>${auction.title}</h5>
            <p id="auction-type">${auction.type} Auction</p>
            <p id="bid-amount">
                <img src="/images/bid.png" alt="auction"/>
                <span>${
                  auction.minAskPrice ? auction.minAskPrice : "-"
                } Points</span>
            </p>
       </div> </div>`
        )
        .join("");

      $("#auctions").html(auctions);

      initializePagination(
        auctionPage.totalSize,
        auctionPage.pageSize,
        auctionPage.currentPage,
        populateAuctions
      );
    },
    true
  );
}
