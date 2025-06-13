import type { UUID } from "../../common/utils/utils";

export interface Wallet {
    id: UUID; // UUID
    balance: number;
};