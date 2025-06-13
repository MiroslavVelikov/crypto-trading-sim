import React from 'react';
import './MarketHeader.css';

interface MarketHeaderProps {
  userFullName: string;
  onMarket?: () => void;
  onBuy?: () => void;
  onSell?: () => void;
  onReset?: () => void;
  onWallet?: () => void;
  onLogout?: () => void;
  hideGreeting?: boolean;
}

const MarketHeader: React.FC<MarketHeaderProps> = ({
  userFullName,
  onMarket,
  onBuy,
  onSell,
  onReset,
  onWallet,
  onLogout,
  hideGreeting,
}) => (
  <header className="market-header">
    {!hideGreeting && (
      <div className="market-greeting">
        Hello, <span>{userFullName}</span>!
      </div>
    )}
    <div className="market-icons">
      <i className="fa fa-shopping-cart" title="Market" onClick={onMarket}></i>
      <i className="fa fa-plus" title="Buy" onClick={onBuy}></i>
      <i className="fa-solid fa-arrow-right-arrow-left" title="Sell" onClick={onSell}></i>
      <i className="fa-solid fa-clock-rotate-left" title="Reset" onClick={onReset}></i>
      <i className="fa-solid fa-wallet" title="Wallet" onClick={onWallet}></i>
      <i className="fa-solid fa-right-from-bracket" title="Logout" onClick={onLogout}></i>
    </div>
  </header>
);

export default MarketHeader;