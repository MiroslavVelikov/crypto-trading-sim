import { useEffect, useState } from 'react';
import type Wallet from './Wallet';
import { WALLET_ID_TOKEN, type UUID } from '../../common/utils/utils';

const Reset: React.FC = () => {
    const [walletId, setWalletId] = useState<UUID | null>(null);
    
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
        } catch (error) {
          alert('Getting wallet failed. Please check your credentials and try again.');
        }
      };
      fetchWalletInfo();
    }, []);
    
    useEffect(() => {
      if (!walletId) return;
      fetch(`http://localhost:8080/api/wallets/reset`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          [WALLET_ID_TOKEN]: walletId as string,
        },
        credentials: 'include',
      })

      window.location.href = '/wallet';
    }, [walletId]);

    return <div></div>;
}

export default Reset;