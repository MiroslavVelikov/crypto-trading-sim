import React, { useEffect, useState, useRef } from 'react';
// @ts-ignore
import SockJS from 'sockjs-client/dist/sockjs.min.js';
// @ts-ignore
import Stomp from 'stompjs';
import './Market.css';
import { useNavigate } from 'react-router-dom';
import MarketHeader from './MarketHeader';
import { getCookie, TRADER_NAME_COOKIE } from '../../common/utils/utils';
import type Crypto from '../interfaces/Crypto';

const userFullName = getCookie(TRADER_NAME_COOKIE) || "User";

const splitIntoColumns = (arr: Crypto[], columns: number) => {
  const result: Crypto[][] = Array.from({ length: columns }, () => []);
  arr.forEach((item, idx) => {
    result[idx % columns].push(item);
  });
  return result;
};

const Market: React.FC = () => {
  const [cryptos, setCryptos] = useState<Crypto[]>([]);
  const stompClientRef = useRef<any>(null);
  const navigate = useNavigate();
  
  useEffect(() => {
    fetch('http://localhost:8080/market/public/load')
      .then(res => res.json())
      .then((data: Crypto[]) => setCryptos(data))
      .catch(err => console.error('Error loading initial market data:', err));

    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);
    stompClientRef.current = stompClient;

    stompClient.connect({}, () => {
      stompClient.subscribe('/market/public', (message: any) => {
        const data: Crypto = JSON.parse(message.body);
        setCryptos(prev =>
          prev.map(crypto => {
            if (crypto.symbol === data.symbol) {
              return {
                ...crypto,
                prevPrice: crypto.price,
                price: data.price,
              };
            }
            return crypto;
          })
        );
      });
    });

    return () => {
      if (stompClientRef.current && stompClientRef.current.connected) {
        stompClientRef.current.disconnect();
      }
    };
  }, []);

  const columns = splitIntoColumns(cryptos, 4);

  return (
    <div className="market-container">
      <MarketHeader
        userFullName={userFullName}
        onMarket={() => { navigate('/market') }}
        onBuy={() => { navigate('/market/select') }}
        onSell={() => { navigate('/market/sell') }}
        onReset={() => { navigate('/wallet/reset') }}
        onWallet={() => { navigate('/wallet'); }}
        onLogout={() => { navigate('/logout'); }}
      />
      <main className="market-main">
        <div className="market-tables-grid">
          {columns.map((column, colIdx) => (
            <table className="market-table" key={colIdx}>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th>Symbol</th>
                  <th>Price ($)</th>
                </tr>
              </thead>
              <tbody>
                {column.map((crypto, idx) => {
                  const indexOrder = idx + 1 + colIdx * column.length;

                  return (
                    <tr key={crypto.symbol}>
                      <td>{indexOrder}</td>
                      <td>{crypto.name}</td>
                      <td>{crypto.symbol}</td>
                      <td>{crypto.price}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          ))}
        </div>
      </main>
    </div>
  );
};

export default Market;
