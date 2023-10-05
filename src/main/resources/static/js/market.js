function populateCategories() {
  get(
      'categories',
      (titledCategories) => {
        const categories = titledCategories.map(
            (titledCategory) => `
                                    <h5>${titledCategory.title}</h5>
                                    ${titledCategory.categories.map(
                category => `<div class="individual-list">
              <input
          type='checkbox'
          name="${replaceSpaceByUnderScore(
                    titledCategory.fieldName,
                )}"
                                    value="${replaceSpaceByUnderScore(
                    category.tag,
                )}"
                                    id='${titledCategory.fieldName +
                category.tag}'
                                    class="filter-buttons"
                            /><label for="${titledCategory.fieldName +
                category.tag}">${
                    category.tag
                }</label>
                        </div>`).join(' ')}
                                    `,
        ).join(' ');

        $('#categories-list').
            append(categories).
            on('change', ':checkbox', function() {
              let checkedValues = [];
              $('.filter-buttons:checked').
                  each(function() {
                    const fieldName = $(this).attr('name');
                    checkedValues.push(`${fieldName}=${$(this).val()}`);
                  });
              populateAuctions(1, checkedValues);
            });
      },
      true,
  );
}

$(document).ready(function() {
  // const marketStatus = get("")
  // $("#market-status").

  populateAuctions(1, []);

  $('#auctions').on('click', '.individual-items', function() {
    const auctionId = $(this).data('auction-id');
    location.href = `/web/auction?auctionId=${auctionId}`;
  });

  get('categories/sort', (sorts) => {
    let sortItems = sorts.map(
        sortItem => `<option value="${sortItem.tag}" class="sort-item">${sortItem.tag}</option>`).
        join(' ');

    sortItems = '<option value="None" class="sort-item">Sort by Reputation</option> ' +
        sortItems;

    $('#sort-auctions').empty().append(sortItems).on('click');
  }, true);

  populateCategories();
});

function replaceSpaceByUnderScore(value) {
  return value.replace(/ /g, '_');
}

function populateAuctions(page, filters) {
  let filterPart = '';
  if (filters !== []) {
    const parts = filters.join(';');
    filterPart = `&filters=${parts}`;
  }
  const url = `auctions/today?page=${page}&size=6${filterPart}`;
  get(
      url,
      (auctionPage) => {
        $('#match-count').text(auctionPage.totalSize);
        if (auctionPage.data) {
          let auctions = auctionPage.data.map(
              (auction) =>
                  `<div class="individual-items" data-auction-id="${auction.id}">
           ${
                      auction.status === 'SOLD'
                          ? '<div class="sold-stamp">SOLD</div>'
                          : ''
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
                      auction.minAskPrice ? auction.minAskPrice : '-'
                  } Points</span>
            </p>
            <div id="seller-details">
                <h5>By</h5>
                <div>${auction.seller.name}</div>
                <img src="${getReputationIcon(auction.seller.reputation)}" alt="Reputation --" id="reputation-icon"></img>
            </div>
       </div> </div>`,
          ).join('');

          $('#auctions').html(auctions);

          initializePagination(
              auctionPage.totalSize,
              auctionPage.pageSize,
              auctionPage.currentPage,
              populateAuctions,
              [filters],
          );
        }
      },
      true,
  );
}
