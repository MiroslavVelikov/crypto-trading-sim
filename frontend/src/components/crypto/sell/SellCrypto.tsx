import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import MarketHeader from '../../market/MarketHeader';
import './SellCrypto.css';
import type { Wallet } from '../../interfaces/Wallet';
import { getCookie, TRADER_NAME_COOKIE, WALLET_ID_TOKEN, type UUID } from '../../../common/utils/utils';
import type WalletHoldings from '../../interfaces/WalletHoldings';

const userFullName = getCookie(TRADER_NAME_COOKIE) || "User";

const SellCrypto: React.FC = () => {
  const { symbol } = useParams<{ symbol: string }>();
  const navigate = useNavigate();
  const [amount, setAmount] = useState(0);
  const [price, setPrice] = useState<number | null>(null);
  const [wallet, setWallet] = useState<WalletHoldings | null>(null);
  const [walletId, setWalletId] = useState<UUID | null>(null);
  const [balance, setBalance] = useState<number | null>(null);
  const holding = wallet?.holdings.find(h => h.symbol === symbol + '/USD');

  useEffect(() => {
        const fetchWalletInfo = async () => {
          try {
            const response = await fetch('http://localhost:8080/api/wallets', {
              method: 'GET',
              headers: { 'Content-Type': 'application/json' },
              credentials: 'include',
            });
    
            if (!response.ok) {
              const text = await response.text();
              throw new Error(`Getting wallet failed: ${response.status} - ${text}`);
            }
    
            const walletInfo: Wallet = await response.json();
            setWalletId(walletInfo.id);
            setBalance(walletInfo.balance);
          } catch (error) {
            alert('Getting wallet failed. Please check your credentials and try again.');
          }
        };
        fetchWalletInfo();
      }, []);

  useEffect(() => {
        if (!walletId) return;
        fetch(`http://localhost:8080/api/wallets/holdings`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            [WALLET_ID_TOKEN]: walletId as string,
          },
          credentials: 'include',
        })
          .then(res => res.json())
          .then(data => setWallet(data))
          .catch(() => setWallet(null));
      }, [walletId]);

  useEffect(() => {
    if (!symbol) return;
    fetch(`http://localhost:8080/market/public/${symbol}`)
      .then(res => res.json())
      .then(data => setPrice(data.price))
      .catch(() => setPrice(null));
  }, [symbol]);

  useEffect(() => {
    const interval = setInterval(() => {
      window.location.reload();
    }, 60000); // 60,000 ms = 1 minute

    return () => clearInterval(interval);
  }, []);

  const total = price && amount ? (price * amount) : 0;

  const handleSell = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const transactionRequest = {
        symbol: symbol + '/USD',
        amount: amount,
        price: price,
        type: 'sell',
      };

      const response = await fetch('http://localhost:8080/api/wallets/holdings/sell', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          [WALLET_ID_TOKEN]: String(walletId),
        },
        credentials: 'include',
        body: JSON.stringify(transactionRequest),
      });

      if (!response.ok) {
        const text = await response.text();
        alert(`Sell failed: ${response.status} - ${text}`);
        return;
      }

      alert(`Selling ${amount} ${symbol} at $${price} per unit.`);
      window.location.reload();
    } catch (err) {
      alert('Sell request failed.');
    }
  };

  if (!holding) {
    return (
      <div className="sell-crypto-bg">
        <MarketHeader userFullName="Your Name" />
        <div className="sell-crypto-card">
          <h2>Sell Crypto</h2>
          <div style={{ color: 'red', padding: '2rem', textAlign: 'center' }}>
            You do not own any {symbol}.
          </div>
          <button className="sell-crypto-btn cancel" onClick={() => navigate('/market/sell')}>
            Back
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="sell-crypto-bg">
      <MarketHeader
        userFullName={userFullName}
        onMarket={() => { navigate('/market') }}
        onBuy={() => { navigate('/market/select') }}
        onSell={() => { navigate('/market/sell') }}
        onReset={() => { navigate('/wallet/reset') }}
        onWallet={() => { navigate('/wallet'); }}
        onLogout={() => { navigate('/logout'); }}
      />
      <div className="sell-crypto-card">
        <h2>Sell Crypto</h2>
        <div className="sell-crypto-balance">
          Balance:{' '}
          <span>
            {balance !== null && balance !== undefined ? balance.toFixed(2) : '...'} $
          </span>
        </div>
        <form className="sell-crypto-form" onSubmit={handleSell}>
          <div className="sell-crypto-row">
            <label>Crypto</label>
            <div className="sell-crypto-value">
              {holding.symbol}
            </div>
          </div>
          <div className="sell-crypto-row">
            <label htmlFor="amount">Amount (max: {holding.amount})</label>
            <input
              id="amount"
              type="number"
              min="0"
              max={holding.amount}
              step="any"
              value={amount}
              onChange={e => {
                const val = Number(e.target.value);
                if (val > holding.amount) {
                  setAmount(holding.amount);
                } else if (val < 0) {
                  setAmount(0);
                } else {
                  setAmount(val);
                }
              }}
              className="sell-crypto-input"
            />
          </div>
          <div className="sell-crypto-row">
            <label>Current price (per unit)</label>
            <div className="sell-crypto-value">
              {price !== null ? `$${price.toFixed(2)}` : '...'}
            </div>
          </div>
          <div className="sell-crypto-row">
            <label>Total sell price:</label>
            <div className="sell-crypto-value">
              {price !== null ? `$${total.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}` : '...'}
            </div>
          </div>
          <div className="sell-crypto-actions">
            <button type="submit" className="sell-crypto-btn sell" disabled={!amount || amount > holding.amount}>
              Sell
            </button>
            <button type="button" className="sell-crypto-btn cancel" onClick={() => navigate('/market/sell')}>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SellCrypto;