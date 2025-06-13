import React, { useEffect, useState } from 'react';
import MarketHeader from '../../market/MarketHeader';
import type { Wallet } from '../../interfaces/Wallet';
import type WalletHoldings from '../../interfaces/WalletHoldings';
import { getCookie, TRADER_NAME_COOKIE, WALLET_ID_TOKEN, type UUID } from '../../../common/utils/utils';
import type Crypto from '../../interfaces/Crypto';
import { useNavigate } from 'react-router-dom';
import './SellCrypto.css'

const userFullName = getCookie(TRADER_NAME_COOKIE) || "User";

const ColoredValueWithArrow: React.FC<{ value: number; compareTo: number; suffix?: string }> = ({
  value,
  compareTo,
  suffix = '',
}) => {
  let color = '#232526';
  if (value > compareTo) color = '#16a34a';
  else if (value < compareTo) color = '#e53935';

  return (
    <span
      style={{
        color,
        fontWeight: 700,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '0.3em',
      }}
    >
      {value}
      {suffix}
      {value > compareTo && <span style={{ fontSize: '1.1em' }}>▲</span>}
      {value < compareTo && <span style={{ fontSize: '1.1em' }}>▼</span>}
    </span>
  );
};

const SellCryptoList: React.FC = () => {
  const [wallet, setWallet] = useState<WalletHoldings | null>(null);
  const [walletId, setWalletId] = useState<UUID | null>(null);
  const [balance, setBalance] = useState<number | null>(null);
  const [currentPrices, setCurrentPrices] = useState<Record<string, number>>({});
  const navigate = useNavigate();

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
    if (!wallet?.holdings) return;
    const fetchPrices = async () => {
      const prices: Record<string, number> = {};
      await Promise.all(
        wallet.holdings.map(async (h) => {
          try {
            const res = await fetch(`http://localhost:8080/market/public/${h.symbol.split("/")[0]}`);
            if (res.ok) {
              const data: Crypto = await res.json();
              prices[h.symbol] = data.price;
            }
          } catch {
            prices[h.symbol] = NaN;
          }
        })
      );
      setCurrentPrices(prices);
    };

    fetchPrices();
  }, [wallet]);

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
        <table className="sell-crypto-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Symbol</th>
              <th>Unit</th>
              <th>Bought at ($)</th>
              <th>Current price ($)</th>
              <th>Total</th>
              <th>Profit</th>
              <th>Sell</th>
            </tr>
          </thead>
          <tbody>
            {wallet?.holdings.map((h, idx) => (
              <tr key={h.symbol}>
                <td>{idx + 1}</td>
                <td>{h.symbol}</td>
                <td>{h.amount}</td>
                <td>{h.averagePrice.toFixed(2)}</td>
                <td>
                  {currentPrices[h.symbol] !== undefined ? (
                    <ColoredValueWithArrow value={currentPrices[h.symbol]} compareTo={h.averagePrice} />
                  ) : (
                    '...'
                  )}
                </td>
                <td>{(h.amount * currentPrices[h.symbol]).toFixed(2)}</td>
                <td>
                  {currentPrices[h.symbol] !== undefined ? (
                    <ColoredValueWithArrow
                      value={Number((((currentPrices[h.symbol] - h.averagePrice) / h.averagePrice) * 100).toFixed(2))}
                      compareTo={0}
                      suffix="%"
                    />
                  ) : (
                    '...'
                  )}
                </td>
                <td>
                  <button className="sell-crypto-btn" onClick={() => {
                    navigate(`/market/${h.symbol.split('/')[0]}/sell`);
                  }}>Sell</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default SellCryptoList;