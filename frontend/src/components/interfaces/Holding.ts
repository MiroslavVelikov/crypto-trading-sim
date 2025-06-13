import type { UUID } from "../../common/utils/utils";

export interface Holding {
    id: UUID;
    symbol: string;
    amount: number;
    averagePrice: number;
};
