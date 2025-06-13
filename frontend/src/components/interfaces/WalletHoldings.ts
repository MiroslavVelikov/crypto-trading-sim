import type { Holding } from "./Holding";

type UUID = string;

export default interface WalletHoldings {
    id: UUID;
    balance: number;
    fullName: string;
    walletId: UUID;
    holdings: Holding[];
}