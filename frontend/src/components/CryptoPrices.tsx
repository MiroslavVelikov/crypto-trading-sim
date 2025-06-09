import useKrakenPrices from '../hooks/useKrakenPrices';

const CryptoPrices = () => {
  const prices = useKrakenPrices();

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Live Cryptocurrency Prices</h2>
      <table className="w-full border-collapse">
        <thead>
          <tr className="text-left border-b">
            <th>Symbol</th>
            <th>Price (USD)</th>
            <th>24h Change</th>
          </tr>
        </thead>
        <tbody>
          {Object.entries(prices).map(([symbol, data]) => (
            <tr key={symbol} className="border-b">
              <td>{symbol}</td>
              <td>${parseFloat(data.price).toFixed(2)}</td>
              <td>{parseFloat(data.change).toFixed(2)}%</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CryptoPrices;