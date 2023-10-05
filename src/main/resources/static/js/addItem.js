$(document).ready(function() {
  $('#item-category-search').on('keyup', function() {
    const query = $(this).val();

    if (query.length > 2) {
      get(
          `categories/suggestions?query=${query}`,
          populateCategorySuggestions,
          true,
      );
    } else {
      $('#category-suggestions').empty().hide();
    }
  });

  $(document).on('click', function(event) {
    if (!$(event.target).closest('#item-category-search').length) {
      $('#category-suggestions').empty().hide();
    }
  });

  $('#category-suggestions').on('click', '.suggestion-item', function() {
    $('#category-suggestions').empty().hide();
    const suggestion = $(this).data('suggestion');
    $('#item-category-search').val(suggestion);
  });

  $('#list-item-form').on('submit', function(e) {
    e.preventDefault();
    createAuction(true);
    $('#list-item-form button').hide();
  }).on('reset', function(e) {
    e.preventDefault();
    createAuction(false);
  });
});

function createAuction(isContinue) {
  const data = {
    category: $('#item-category-search').val(),
    title: $('#auctionTitle').val(),
    type: $('#auction-type').val(),
    description: $('#auction-description').val(),
    isDraft: true,
  };

  const sellerId = getUser().id;

  post(
      `sellers/${sellerId}/auctions`,
      data,
      (auction) => {
        const thumbNail = $('#thumbnailImage');
        if (
            thumbNail &&
            thumbNail.length > 0 &&
            thumbNail[0].files &&
            thumbNail[0].files.length > 0
        ) {
          const file = thumbNail[0].files[0];
          postFile(
              `sellers/${sellerId}/auctions/${auction.id}/image`,
              file,
              (auction) => {
                console.log('Uploaded auction image');
                proceedToNextPage(isContinue, auction);
              },
              true,
          );
        } else {
          proceedToNextPage(isContinue, auction);
        }

      },
      true,
  );
}

function proceedToNextPage(isContinue, auction) {

  if (isContinue) {

    const dutchAuctionAdditionalConfig = `<label for="lowerPriceOnSeconds">Lower price on (seconds)</label>
    <input
        type="number"
        name="lowerPriceOnSeconds"
        id="lowerPriceOnSeconds"
    />
    <label for="lowerPriceByPoints">Lower price by (points)</label>
    <input
        type="number"
        name="lowerPricebyPoints"
        id="lowerPriceByPoints"
    />`;

    const configHTML = `
    <form action="#" id="auction-config-form">
      <h3>${auction.type} Auction</h3>
      <label for="minimumBidAmount">Minimum bid amount</label>
      <input
          type="number"
          name="minimumBidAmount"
          id="minimumBidAmount"
      />
      <label for="itemCount">Item count</label>
      <input type="number" name="itemCount" id="itemCount"/>

      ${auction.type === 'Dutch' ? dutchAuctionAdditionalConfig : ''}
      
      <label for="scheduled-date">Publish on</label>
      <input type="date" id="published-date" name="scheduled-date">
      
      <section class="submit-button">
        <button type="submit">Save as draft</button>
        <button type="reset">Publish</button>
      </section>
   
    </form>`;

    $('#auction-config').
        append(configHTML).
        on('submit', '#auction-config-form', function(e) {
          e.preventDefault();
          updateAuction(auction, false);
        }).
        on('reset', '#auction-config-form', function(e) {
          e.preventDefault();
          updateAuction(auction, true);
        });
  } else {
    location.href = '/web/my-listings';
  }
}

function updateAuction(auction, publish) {
  const user = getUser();

  const data = {
    minAskPrice: $('#minimumBidAmount').val(),
    itemCount: $('#itemCount').val(),
    decrementSeconds: $('#lowerPriceOnSeconds').val(),
    lowerPriceByPoints: $('#decrementFactor').val(),
    scheduledDate: $('#published-date').val(),
    publish
  };

  post(`sellers/${user.id}/auctions/${auction.id}`, data,
      d => location.href = '/web/my-listings', true);
}

function populateCategorySuggestions(suggestions) {
  const suggestionDivs = suggestions.map(
      (suggestion) =>
          `<div class="suggestion-item" data-suggestion="${suggestion.tag}">${suggestion.tag}</div>`,
  ).join(' ');

  $('#category-suggestions').empty().append(suggestionDivs).show();
}
