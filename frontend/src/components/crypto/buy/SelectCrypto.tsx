import MarketHeader from '../../market/MarketHeader';
import './BuyCrypto.css';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { getCookie, TRADER_NAME_COOKIE } from '../../../common/utils/utils';

const availableCryptos = [
  { symbol: 'BTC', name: 'Bitcoin' },
  { symbol: 'ETH', name: 'Ethereum' },
  { symbol: 'USDT', name: 'Tether' },
  { symbol: 'XRP', name: 'Ripple' },
  { symbol: 'SOL', name: 'Solana' },
  { symbol: 'ADA', name: 'Cardano' },
  { symbol: 'DOGE', name: 'Dogecoin' },
  { symbol: 'TRX', name: 'TRON' },
  { symbol: 'WBTC', name: 'Wrapped Bitcoin' },
  { symbol: 'XLM', name: 'Stellar' },
  { symbol: 'AVAX', name: 'Avalanche' },
  { symbol: 'LINK', name: 'Chainlink' },
  { symbol: 'BCH', name: 'Bitcoin Cash' },
  { symbol: 'DOT', name: 'Polkadot' },
  { symbol: 'SUI', name: 'Sui' },
  { symbol: 'TON', name: 'Toncoin' },
  { symbol: 'SHIB', name: 'Shiba Inu' },
  { symbol: 'AAVE', name: 'Aave' },
  { symbol: 'FIL', name: 'Filecoin' },
  { symbol: 'ETC', name: 'Ethereum Classic' },
];

const userFullName = getCookie(TRADER_NAME_COOKIE) || "User";

const SelectCrypto = () => {
  const [selected, setSelected] = useState(availableCryptos[0].symbol);
  const navigate = useNavigate();

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
          <h2 className="buy-cripto-title">Select Crypto to Buy</h2>
          <form
            className="buy-cripto-form"
            onSubmit={e => {
              e.preventDefault();
              navigate(`/market/${selected}/buy`);
            }}
          >
            <div className="buy-cripto-row">
              <label htmlFor="crypto-select">Crypto</label>
              <select
                id="crypto-select"
                className="buy-cripto-input"
                value={selected}
                onChange={e => setSelected(e.target.value)}
              >
                {availableCryptos.map(c => (
                  <option key={c.symbol} value={c.symbol}>
                    {c.name} ({c.symbol})
                  </option>
                ))}
              </select>
            </div>
            <div className="buy-cripto-actions">
              <button type="submit" className="buy-cripto-btn buy" onSubmit={e => {
                  e.preventDefault();
                  navigate(`/market/${selected}/buy`);
                }}>
                Continue
              </button>
            </div>
          </form>
        </div>
      </div>
    );
};

export default SelectCrypto;