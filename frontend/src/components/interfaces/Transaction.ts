import type { UUID } from "../../common/utils/utils";

export interface Transaction {
    id: UUID;
    symbol: string;
    amount: number;
    price: number;
    transactionType: string;
};
