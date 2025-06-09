import { useEffect, useState } from 'react';

type TickerPrice = {
  [symbol: string]: {
    price: string;
    change: string;
  };
};

const useKrakenPrices = () => {
  const [prices, setPrices] = useState<TickerPrice>({});

  useEffect(() => {
    let ws: WebSocket;
    
    const initWebSocket = () => {
      ws = new WebSocket('wss://ws.kraken.com/');
    
      ws.onopen = () => {
        ws.send(JSON.stringify({
          event: 'subscribe',
          pair: [
            'XBT/USD', 'ETH/USD', 'SOL/USD', 'XRP/USD',
            'ADA/USD', 'DOT/USD', 'DOGE/USD', 'LTC/USD'
          ],
          subscription: { name: 'ticker' }
        }));
      };
    
      ws.onmessage = (event) => {
        const data = JSON.parse(event.data);
        if (Array.isArray(data) && data.length === 3) {
          const [, ticker, symbol] = data;
          if (ticker?.c && ticker?.p) {
            setPrices(prev => ({
              ...prev,
              [symbol]: {
                price: ticker.c[0],
                change: ticker.p[1]
              }
            }));
          }
        }
      };
    };
  
    const rAF = requestAnimationFrame(initWebSocket);
  
    return () => {
      cancelAnimationFrame(rAF);
      if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close();
      }
    };
  }, []);

  return prices;
};

export default useKrakenPrices;
