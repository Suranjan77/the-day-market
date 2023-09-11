$(document).ready(function() {
  $('#item-category-search').on('keyup', function() {
    const query = $(this).val();

    if (query.length > 2) {
      get(`categories/suggestions?query=${query}`, populateCategorySuggestions,
          true);
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

  post(`sellers/${sellerId}/auctions`, data, auction => {

    const thumbNail = $('#thumbnailImage');
    if (thumbNail && thumbNail.length > 0 && thumbNail[0].files &&
        thumbNail[0].files.length > 0) {
      const file = thumbNail[0].files[0];
      postFile(`sellers/${sellerId}/auctions/${auction.id}/image`, file,
          auction => console.log('Uploaded auction image'), true,
      );
    }

    if (isContinue) {
      switch (data.type) {
        case 'English':
          location.href = `/web/english-auction-config?auctionId=${auction.id}`;
          break;
        case 'Dutch':
          location.href = `/web/dutch-auction-config?auctionId=${auction.id}`;
          break;
        case 'Sealed':
          location.href = `/web/sealed-bid-auction-config?auctionId=${auction.id}`;
          break;
      }
    } else {
      location.href = '/web/market';
    }
  }, true);

}

function populateCategorySuggestions(suggestions) {

  const suggestionDivs = suggestions.map(
      suggestion => `<div class="suggestion-item" data-suggestion="${suggestion.tag}">${suggestion.tag}</div>`).
      join(' ');

  $('#category-suggestions').empty().append(suggestionDivs).show();
}