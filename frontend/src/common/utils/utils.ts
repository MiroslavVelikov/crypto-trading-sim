export function getCookie(name: string) {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
  return match ? decodeURIComponent(match[2]) : '';
}

export const TRADER_ID_TOKEN = 'traderId';
export const TRADER_NAME_COOKIE = 'traderName';
export const TRADER_USERNAME_COOKIE = 'traderUsername';

export const WALLET_ID_TOKEN = 'walletId';

export type UUID = string;

