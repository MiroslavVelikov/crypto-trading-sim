import type { Transaction } from "./Transaction";

type UUID = string;

export default interface WalletTransactions {
    id: UUID;
    balance: number;
    fullName: string;
    walletId: UUID;
    transactions: Transaction[];
}