$(document).ready(function() {
  const user = getUser();

  $('#sell-points').on('click', function() {
    $('#show-buttons-section').hide();
    $('#points-form').
        empty().
        append(getBuySellForm(false)).
        on('keyup', '#sellAmount', function() {
          let amt = $(this).val();
          $('#total-pay').empty().html(`&#163;${amt}`);
        }).ready(function() {
      refreshUserPoints(user);
    }).on('click', '#submit-payment', function(e) {
      e.preventDefault();
      const data = {
        bankAccountDetails: {
          accountName: $('#accountName').val(),
          accountNumber: $('#accountNumber').val(),
          sortCode: $('#sortCode').val(),
        },
        points: $('#sellAmount').val(),
      };
      post(`payments/sell?userId=${user.id}`, data, data => {
        refreshUserPoints(user);
        location.href = '/web/market';
      }, true);
    });
  });

  $('#buy-points').on('click', function() {
    $('#show-buttons-section').hide();
    $('#points-form').
        empty().
        append(getBuySellForm(true)).
        on('keyup', '#purchaseAmount', function() {
          let amt = $(this).val();
          $('#total-pay').empty().html(`&#163;${amt}`);
        }).
        ready(function() {
          refreshUserPoints(user);
        }).
        on('click', '#submit-payment', function(e) {
          e.preventDefault();
          let amount = $('#purchaseAmount').val();
          let cardNumber = $('#cardNumber').val();
          let cardName = $('#cardName').val();
          let cvv = $('#cvv').val();
          let expiryAt = $('#expiryAt').val();

          const data = {
            creditCardDetails: {
              cardNumber,
              cvv,
              expiryDate: expiryAt,
              nameOnCard: cardName,
            },
            amount,
          };

          post(`payments/buy?userId=${user.id}`, data, data => {
            refreshUserPoints(user);
            location.href = '/web/market';
          }, true);
        });
  });
});

function refreshUserPoints(user) {
  get(`users/${user.id}/points`,
      points => $('#user-points').empty().text(points.points), true);
}

function getBuySellForm(isBuy) {
  return `<form action="" id="buy-points-form">
            ${isBuy ? buyPointsForm() : sellPointsForm()}
  <section class="button">
    <button id="submit-payment">Submit Payment</button>
  </section>
</form>`;
}

function buyPointsForm() {
  return `<div class="current-points">
  <h4>Current Points</h4>
  <h4 id="user-points">500</h4>
</div>
<label for="purchaseAmount">Purchase Amount</label>
<input
    type="number"
    name="purchaseAmount"
    placeholder="Purchase amount"
    id="purchaseAmount"
    required
/>
<div class="total-amount">
  <h4>Total</h4>
  <h4 id="total-pay">&#163;0.00</h4>
</div>
<label for="cardName">Name on card</label>
<input
    type="text"
    name="cardName"
    placeholder="Card Name"
    id="cardName"
    required
/>
<label for="cardNumber">Card number</label>
<input
    type="number"
    name="cardNumber"
    placeholder="Card number"
    id="cardNumber"
    required
/>
<section class="card-validity">
  <div class="cvv">
    <label for="cvv">CVV</label>
    <input
        type="number"
        name="cvv"
        placeholder="cvv"
        maxlength="4"
        id="cvv"
        required
    />
  </div>
  <div class="expiry-date">
    <label for="expiryAt">Expiry at</label>
    <input
        type="date"
        name="expiryAt"
        placeholder="MM/YY"
        id="expiryAt"
        required
    />
  </div>
</section>`;
}

function sellPointsForm() {
  return `<div class="current-points">
  <h4>Current Points</h4>
  <h4 id="user-points">500</h4>
</div>
<label for="sellAmount">Amount to Sell</label>
<input
    type="number"
    name="sellAmount"
    placeholder="Selling amount"
    id="sellAmount"
    required
/>
<div class="total-amount">
  <h4>You will receive</h4>
  <h4 id="total-pay">&#163;0.00</h4>
</div>
<label for="accountName">Account Name</label>
<input
    type="text"
    name="accountName"
    placeholder="Account Name"
    id="accountName"
    required
/>
<label for="sortCode">Sort code</label>
<input
    type="number"
    name="sortCode"
    placeholder="Sort Code"
    id="sortCode"
    required
/>
<label for="accountNumber">Account number</label>
<input
    type="number"
    name="accountNumber"
    placeholder="Account number"
    id="accountNumber"
    required
/>`;
}