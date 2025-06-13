import MarketHeader from '../../market/MarketHeader';
import './BuyCrypto.css';
import type Crypto from '../../interfaces/Crypto';
import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import type WalletTransactions from '../../interfaces/WalletTransactions';
import { getCookie, TRADER_NAME_COOKIE, WALLET_ID_TOKEN } from '../../../common/utils/utils';

const userFullName = getCookie(TRADER_NAME_COOKIE) || "User";

const BuyCrypto = () => {
  const [crypto, setCrypto] = useState<Crypto | null>(null);
  const [amount, setAmount] = useState(0);
  const { symbol } = useParams();
  const navigate = useNavigate();
  const [wallet, setWallet] = useState<WalletTransactions | null>(null);

  useEffect(() => {
    if (!symbol) return;
    fetch(`http://localhost:8080/market/public/${symbol}`)
      .then(res => res.json())
      .then(data => setCrypto(data))
      .catch(err => console.error(`Error loading ${symbol} cryptocurrency data:`, err));
  }, [symbol]);

  useEffect(() => {
    const fetchWalletInfo = async () => {
      try {
        fetch('http://localhost:8080/api/wallets', {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
          credentials: 'include',
        })
          .then(res => res.json())
          .then(data => setWallet(data))
          .catch(() => setWallet(null));;
      } catch (error) {
        alert('Getting wallet failed. Please check your credentials and try again.');
      }
    };
    fetchWalletInfo();
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      window.location.reload();
    }, 60000); // 60,000 ms = 1 minute

    return () => clearInterval(interval);
  }, []);

  const handleBuy = (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Add your sell request logic here
    //alert(`Selling ${amount} ${symbol} at $${price} per unit.`);
  };

  if (!crypto) return null;

  const totalPrice = amount * crypto.price;

  return (
    <div className="buy-cripto-bg">
      <MarketHeader
        userFullName={userFullName}
        onMarket={() => { navigate('/market') }}
        onBuy={() => { navigate('/market/select') }}
        onSell={() => { navigate('/market/sell') }}
        onReset={() => { navigate('/wallet/reset') }}
        onWallet={() => { navigate('/wallet'); }}
        onLogout={() => { navigate('/logout'); }}
      />
      <div className="buy-cripto-card">
        <h2 className="buy-cripto-title">Buy Crypto</h2>
        <div className="buy-cripto-balance">
          Balance: <span>${wallet?.balance.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
        </div>
        <hr />
        <form className="buy-cripto-form" onSubmit={async e => {
            e.preventDefault();
            if (!wallet || !crypto) return;

            try {
              const transactionRequest = {
                symbol: crypto.symbol,
                amount: amount,
                price: crypto.price,
                type: 'buy',
              };

              const response = await fetch('http://localhost:8080/api/wallets/holdings/buy', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                  [WALLET_ID_TOKEN]: String(wallet.id),
                },
                credentials: 'include',
                body: JSON.stringify(transactionRequest),
              });

              if (!response.ok) {
                const text = await response.text();
                alert(`Buy failed: ${response.status} - ${text}`);
                return;
              }

              const data = await response.json();
              alert('Buy successful!');
              window.location.reload();
            } catch (err) {
              alert('Buy request failed.');
            }
          }}>
          <div className="buy-cripto-row">
            <label>Crypto</label>
            <div className="buy-cripto-value">{crypto.name} ({crypto.symbol})</div>
          </div>
          <div className="buy-cripto-row">
            <label htmlFor="amount">Amount</label>
            <input
              id="amount"
              type="number"
              min="0"
              step="any"
              value={amount}
              onChange={e => setAmount(Number(e.target.value))}
              className="buy-cripto-input"
            />
          </div>
          <div className="buy-cripto-row">
            <label>Price (per unit)</label>
            <div className="buy-cripto-value">${crypto.price}</div>
          </div>
          <div className="buy-cripto-row">
            <label>Total buy price:</label>
            <div className="buy-cripto-value">
              ${totalPrice.toLocaleString(undefined, { minimumFractionDigits: 6, maximumFractionDigits: 6 })}
            </div>
          </div>
          <div className="buy-cripto-actions">
            <button type="submit" className="buy-cripto-btn buy">Buy</button>
            <button type="button" className="buy-cripto-btn cancel" onClick={() => navigate('/market/select')}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default BuyCrypto;