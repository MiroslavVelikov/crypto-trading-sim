import WebSocket from 'ws';

type KrakenTickerMessage = [
  number, // channelID
  {
    a: string[]; // ask [price, wholeLotVolume, lotVolume]
    b: string[]; // bid [price, wholeLotVolume, lotVolume]
    c: string[]; // last trade closed [price, lotVolume]
    v: string[]; // volume [today, last 24 hours]
    p: string[]; // volume weighted average price [today, last 24 hours]
    t: number[]; // number of trades [today, last 24 hours]
    l: string[]; // low [today, last 24 hours]
    h: string[]; // high [today, last 24 hours]
    o: string;   // today's opening price
  },
  string,    // pair name
];

function subscribeToKrakenTicker(pairs: string[]) {
  const ws = new WebSocket('wss://ws.kraken.com');

  ws.on('open', () => {
    console.log('WebSocket connection opened');

    const subscribeMsg = {
      event: 'subscribe',
      pair: pairs,
      subscription: { name: 'ticker' },
    };

    ws.send(JSON.stringify(subscribeMsg));
  });

  ws.on('message', (data) => {
    try {
      const msg = JSON.parse(data.toString());

      // Ignore heartbeat and system status messages
      if (msg.event) {
        if (msg.event === 'subscriptionStatus') {
          console.log('Subscription status:', msg);
        }
        return;
      }

      // Kraken ticker message format is an array, first element is channelID
      if (Array.isArray(msg) && msg.length >= 3) {
        const [channelID, tickerData, pair] = msg as KrakenTickerMessage;

        // Extract some useful info
        const lastTradePrice = tickerData.c[0];
        const volumeToday = tickerData.v[0];
        const highToday = tickerData.h[0];
        const lowToday = tickerData.l[0];

        console.log(
          `[${pair}] Last Price: ${lastTradePrice}, Volume Today: ${volumeToday}, High Today: ${highToday}, Low Today: ${lowToday}`
        );
      }
    } catch (err) {
      console.error('Error parsing message:', err);
    }
  });

  ws.on('error', (err) => {
    console.error('WebSocket error:', err);
  });

  ws.on('close', () => {
    console.log('WebSocket connection closed');
  });
}

// Example: Subscribe to BTC/USD, ETH/USD, and XRP/USD
const pairs = ['XBT/USD', 'ETH/USD', 'XRP/USD'];
subscribeToKrakenTicker(pairs);
