import React, { useEffect, useState } from 'react';
import MarketHeader from '../market/MarketHeader';
import { getCookie, TRADER_NAME_COOKIE, TRADER_USERNAME_COOKIE, WALLET_ID_TOKEN, type UUID } from '../../common/utils/utils';
import './Wallet.css';
import type WalletTransactions from '../interfaces/WalletTransactions';
import type { Wallet } from '../interfaces/Wallet';
import { useNavigate } from 'react-router-dom';

const userFullName = getCookie(TRADER_NAME_COOKIE) || "User";

const Wallet: React.FC = () => {
  const [wallet, setWallet] = useState<WalletTransactions | null>(null);
  const [walletId, setWalletId] = useState<UUID | null>(null);
  const [balance, setBalance] = useState<number | null>(null);
  const username = getCookie(TRADER_USERNAME_COOKIE) || 'User';	
  const [firstName, lastName] = userFullName.split(' ');
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
    fetch(`http://localhost:8080/api/wallets/transactions`, {
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

  return (
    <div className="wallet-container">
      <MarketHeader
        userFullName={userFullName}
        onMarket={() => { navigate('/market') }}
        onBuy={() => { navigate('/market/select') }}
        onSell={() => { navigate('/market/sell') }}
        onReset={() => { navigate('/wallet/reset') }}
        onWallet={() => { navigate('/wallet'); }}
        onLogout={() => { navigate('/logout'); }}
      />
      <main className="wallet-main">
        <div className="wallet-card wallet-card-grid">
          <div className="wallet-card-left">
            <div className="wallet-user-icon">
              <i className="fa fa-user fa-3x" aria-hidden="true"></i>
            </div>
            <div className="wallet-username">
              #{username ? username : '...'}
            </div>
            <div className="wallet-names">
              <div>{firstName ? firstName : '...'}</div>
              <div className="wallet-lastname">{lastName ? lastName : ''}</div>
            </div>
          </div>
          <div className="wallet-card-right">
            <div className="wallet-balance">
              <span>Wallet balance</span>
              <div className="wallet-balance-amount">
                {balance !== null && balance !== undefined ? balance.toFixed(2) : '...'} $
              </div>
            </div>
            <div className="wallet-history">
              <span className="wallet-history-title">Transaction history</span>
              <div className="wallet-history-list">
                {wallet && wallet.transactions && wallet.transactions.length > 0 ? (
                  <table className="wallet-history-table">
                    <thead>
                      <tr>
                        <th>Unit</th>
                        <th>Symbol</th>
                        <th>Price ($)</th>
                        <th>Transaction type</th>
                      </tr>
                    </thead>
                    <tbody>
                      {wallet.transactions.map(tx => (
                        <tr key={tx.id}>
                          <td>{tx.amount}</td>
                          <td>{tx.symbol}</td>
                          <td>{tx.price}</td>
                          <td>{tx.transactionType}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                ) : (
                  <div className="wallet-history-empty">No transactions</div>
                )}
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Wallet;